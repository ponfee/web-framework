package cn.ponfee.web.framework.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions.ScanOptionsBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.google.common.base.Stopwatch;

import cn.ponfee.web.framework.model.RedisKey;
import cn.ponfee.web.framework.service.RedisManagerService;
import code.ponfee.commons.concurrent.MultithreadExecutor;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageBoundsResolver;
import code.ponfee.commons.model.PageBoundsResolver.PageBounds;
import code.ponfee.commons.model.PageHandler;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.util.Enums;

/**
 * Redis manager service implementation
 * 
 * @author Ponfee
 */
@Service
public class RedisManagerServiceImpl implements RedisManagerService {

    private static Logger logger = LoggerFactory.getLogger(RedisManagerServiceImpl.class);

    private static final int BATCH_SIZE = 2000;
    private static final int REFRESH_THRESHOLD_MILLIS = 15000;
    private static final Lock LOCK = new ReentrantLock();
    // new CopyOnWriteArrayList<>();
    private static List<RedisKey> keys = Collections.synchronizedList(new LinkedList<>());
    private static volatile long lastRefreshTime = 0;

    private @Resource RedisTemplate<String, Object> redis;
    private @Resource ThreadPoolTaskExecutor taskExecutor;

    @Override
    public Page<RedisKey> query4page(PageRequestParams params) {
        if (CollectionUtils.isEmpty(keys)) {
            return Page.of(Collections.emptyList());
        }

        List<RedisKey> list;
        String keyword = params.getString("keyword");
        String matchmode = params.getString("matchmode");
        boolean ignoreKeyword = StringUtils.isEmpty(keyword);
        boolean ignoreExpire = !"INFINITY".equalsIgnoreCase(params.getString("expiretype"));
        if (ignoreKeyword && ignoreExpire) {
            list = keys; // query all
        } else {
            list = new LinkedList<>();
            for (RedisKey key : keys) {
                // *：query infinity expire
                if (ignoreKeyword) {
                    if ("INFINITY".equalsIgnoreCase(key.getExpire())) {
                        list.add(key);
                    }
                    continue;
                }

                // *：query keyword
                if (!ignoreExpire && !"INFINITY".equalsIgnoreCase(key.getExpire())) {
                    continue; // query infinity expire and key is not infinity
                }

                // *：normal
                switch (Enums.ofIgnoreCase(MatchMode.class, matchmode, MatchMode.HEAD)) {
                    case LIKE:
                        if (key.contains(keyword)) {
                            list.add(key);
                        }
                        break;
                    case HEAD:
                        if (key.startsWith(keyword)) {
                            list.add(key);
                        }
                        break;
                    case TAIL:
                        if (key.endsWith(keyword)) {
                            list.add(key);
                        }
                        break;
                    default:
                        if (key.equals(keyword)) {
                            list.add(key);
                        }
                        break;
                }
            }
        }

        if (CollectionUtils.isEmpty(list)) {
            return Page.of(Collections.emptyList());
        }

        PageBounds bounds = PageBoundsResolver.resolve(
            params.getPageNum(), params.getPageSize(), list.size()
        );
        List<RedisKey> data = list.subList(
            (int) bounds.getOffset(), (int) bounds.getOffset() + bounds.getLimit()
        );
        com.github.pagehelper.Page<RedisKey> page = new com.github.pagehelper.Page<>(
            PageHandler.computePageNum(bounds.getOffset(), params.getPageSize()), params.getPageSize()
        );
        page.addAll(data);
        page.setPages(PageHandler.computeTotalPages(bounds.getTotal(), params.getPageSize()));
        page.setTotal(bounds.getTotal());
        page.setStartRow((int) bounds.getOffset());
        page.setEndRow((int) bounds.getOffset() + bounds.getLimit());
        return Page.of(page);
    }

    @Override
    public void addOrUpdateRedisEntry(String key, String value, Long expire, 
                                      String dataType, String valueType) {
        Object value0 = parseValue(value, valueType);
        long expire0 = Optional.ofNullable(expire)
                               .filter(n -> n != null && n > 0)
                               .orElse(-1L);
        DataType type = parseDataType(key, dataType);
        switch (type) {
            case STRING:
                if (expire0 > 0) {
                    redis.opsForValue().set(key, value0, expire0, TimeUnit.SECONDS);
                } else {
                    redis.opsForValue().set(key, value0);
                }
                break;

            case LIST:
                // TODO
                break;

            case SET:
                // TODO
                break;

            case ZSET:
                // TODO
                break;

            case HASH:
                // TODO
                break;

            default:
                throw new UnsupportedOperationException("Unsupported redis type: " + type.name());
        }
        RedisKey redisKey = new RedisKey(key, type, expire0);
        keys.remove(redisKey);
        keys.add(redisKey);
    }

    @Override
    @SuppressWarnings("unlikely-arg-type")
    public void delete(String... redisKeys) {
        if (ArrayUtils.isEmpty(redisKeys)) {
            return ;
        }
        List<String> list = Arrays.asList(redisKeys);
        redis.delete(list);
        keys.removeAll(list);
    }

    @Override
    public void refresh() {
        if (System.currentTimeMillis() - lastRefreshTime > REFRESH_THRESHOLD_MILLIS) {
            refreshKeys();
        }
    }

    // ------------------------------------------------------------------private methods
    private @PostConstruct void init() {
        refreshKeys(); // 初始化调用一次
    }

    private void refreshKeys() {
        if (!LOCK.tryLock()) {
            return;
        }
        try {
            CompletionService<List<RedisKey>> service = new ExecutorCompletionService<>(
                taskExecutor.getThreadPoolExecutor()
            );
            Stopwatch watch = Stopwatch.createStarted();

            AtomicInteger count = new AtomicInteger(0);
            redis.execute((RedisCallback<Void>) (conn -> {
                Cursor<byte[]> cursor = conn.scan(
                    new ScanOptionsBuilder().match("*").count(BATCH_SIZE).build()
                );
                List<byte[]> binaryKeys = new ArrayList<>(BATCH_SIZE);
                for (int i = 0; cursor.hasNext(); i++) {
                    binaryKeys.add(cursor.next());
                    if (i == BATCH_SIZE) {
                        count.incrementAndGet();
                        service.submit(new AsnycBatch(redis, binaryKeys));
                        i = 0;
                        binaryKeys = new ArrayList<>(BATCH_SIZE);
                    }
                }
                if (!binaryKeys.isEmpty()) {
                    count.incrementAndGet();
                    service.submit(new AsnycBatch(redis, binaryKeys));
                }
                return null;
            }));

            List<RedisKey> result = new LinkedList<>(); // count.get() * BATCH_SIZE
            MultithreadExecutor.join(service, count.get(), result::addAll);

            keys.clear();
            keys = Collections.synchronizedList(result);
            lastRefreshTime = System.currentTimeMillis();
            logger.info("Redis manager refresh key cost time: {}", watch.stop());
        } catch (Throwable e) {
            logger.info("Redis manager load key occur error.", e);
        } finally {
            LOCK.unlock();
        }
    }

    private DataType parseDataType(String key, String dataType) {
        DataType actual = Optional.ofNullable(redis.type(key))
                                  .filter(this::isNotNull)
                                  .orElse(DataType.STRING);
        if (StringUtils.isEmpty(dataType)) {
            return actual;
        }
        DataType expect = null;
        try {
            expect = DataType.fromCode(dataType);
        } catch (Exception e) {
        }
        return Optional.ofNullable(expect)
                       .filter(this::isNotNull)
                       .orElse(actual);
    }

    private boolean isNotNull(DataType type) {
        return type != null && type != DataType.NONE;
    }

    private static Object parseValue(String value, String valueType) {
        return Enums.ofIgnoreCase(ValueType.class, valueType, ValueType.RAW)
                    .parse(value);
    }

    private static final class AsnycBatch implements Callable<List<RedisKey>> {
        RedisTemplate<String, Object> redis;
        List<byte[]> keys;

        AsnycBatch(RedisTemplate<String, Object> redis, List<byte[]> keys) {
            this.redis = redis;
            this.keys = keys;
        }

        @Override
        public List<RedisKey> call() {
            StringRedisSerializer serial = (StringRedisSerializer) redis.getKeySerializer();
            List<Object> list = redis.executePipelined(
                (RedisCallback<Void>) (conn -> {
                    for (byte[] key : keys) {
                        conn.type(key);
                        conn.ttl(key);
                    }
                    return null;
                })
            );

            List<RedisKey> result = new ArrayList<>(keys.size());
            for (int i = 0, j = 0, n = keys.size(); i < n; i++, j+=2) {
                result.add(new RedisKey(
                    serial.deserialize(keys.get(i)), 
                    (DataType) list.get(j), 
                    (Long) list.get(j + 1)
                ));
            }
            return result;
        }
    }

    public enum MatchMode {
        LIKE, HEAD, TAIL, EQUAL
    }

    private enum ValueType {
        RAW, //
        B64() {
            public @Override Object parse(String value) {
                return StringUtils.isEmpty(value)
                       ? ArrayUtils.EMPTY_BYTE_ARRAY
                       : Base64.getUrlDecoder().decode(value);
            }
        };

        public Object parse(String value) {
            return value;
        }
    }

}
