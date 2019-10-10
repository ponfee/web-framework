package cn.ponfee.web.framework.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.google.common.base.Stopwatch;

import cn.ponfee.web.framework.model.RedisKey;
import code.ponfee.commons.concurrent.MultithreadExecutor;
import code.ponfee.commons.io.Closeables;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.util.Enums;

/**
 * Heavyweight redis manager service implementation
 * 
 * @author Ponfee
 */
//@Service("heavyweightRedisManagerService")
public class HeavyweightRedisManagerServiceImpl extends AbstractRedisManagerService {

    private static final int BATCH_SIZE = 2000;
    private static final int REFRESH_THRESHOLD_MILLIS = 15000;
    private static final Lock LOCK = new ReentrantLock();
    private static List<RedisKey> redisKeys = Collections.synchronizedList(new LinkedList<>()); // new CopyOnWriteArrayList<>();
    private static volatile long lastRefreshTime = 0;

    private @Resource ThreadPoolTaskExecutor taskExecutor;

    @Override
    public List<RedisKey> query4list(PageRequestParams params) {
        if (CollectionUtils.isEmpty(redisKeys)) {
            return null;
        }

        String keyword = params.getString("keyword"), matchmode = params.getString("matchmode");
        boolean ignoreKeyword = StringUtils.isEmpty(keyword);
        boolean ignoreExpire = !"INFINITY".equalsIgnoreCase(params.getString("expiretype"));

        if (ignoreKeyword && ignoreExpire) {
            return redisKeys; // query all
        }

        List<RedisKey> list = new LinkedList<>();
        for (RedisKey key : redisKeys) {
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
            switch (Enums.ofIgnoreCase(MatchMode.class, matchmode, MatchMode.EQUAL)) {
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

        return list;
    }

    @Override
    public void refresh() {
        if (System.currentTimeMillis() - lastRefreshTime > REFRESH_THRESHOLD_MILLIS) {
            refreshKeys();
        }
    }

    @Override
    protected void onAddOrUpdate(RedisKey redisKey) {
        redisKeys.remove(redisKey);
        redisKeys.add(redisKey);
    }

    @Override @SuppressWarnings("unlikely-arg-type")
    protected void onDelete(List<String> keys) {
        redisKeys.removeAll(keys);
    }

    // ------------------------------------------------------------------private methods
    @PostConstruct
    private void init() {
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
                    ScanOptions.scanOptions().match("*").count(BATCH_SIZE).build()
                );
                List<byte[]> binaryKeys = new ArrayList<>(BATCH_SIZE);
                for (int i = 0; cursor.hasNext(); i++) {
                    binaryKeys.add(cursor.next());
                    if (i == BATCH_SIZE) {
                        count.incrementAndGet();
                        service.submit(new AsnycBatch(binaryKeys));
                        i = 0;
                        binaryKeys = new ArrayList<>(BATCH_SIZE);
                    }
                }
                if (!binaryKeys.isEmpty()) {
                    count.incrementAndGet();
                    service.submit(new AsnycBatch(binaryKeys));
                }

                Closeables.log(cursor);
                return null;
            }));

            List<RedisKey> result = new LinkedList<>(); // count.get() * BATCH_SIZE
            MultithreadExecutor.join(service, count.get(), result::addAll);

            redisKeys.clear();
            redisKeys = Collections.synchronizedList(result);
            lastRefreshTime = System.currentTimeMillis();
            logger.info("Redis manager refresh key cost time: {}", watch.stop());
        } catch (Throwable e) {
            logger.info("Redis manager load key occur error.", e);
        } finally {
            LOCK.unlock();
        }
    }


    private final class AsnycBatch implements Callable<List<RedisKey>> {
        final List<byte[]> keys;

        AsnycBatch(List<byte[]> keys) {
            this.keys = keys;
        }

        @Override
        public List<RedisKey> call() {
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
            for (int i = 0, j = 0, n = keys.size(); i < n; i++, j += 2) {
                result.add(getAsString(keys.get(i), (DataType) list.get(j), (long) list.get(j + 1)));
            }
            return result;
        }
    }

}
