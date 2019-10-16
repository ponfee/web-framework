package cn.ponfee.web.framework.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import cn.ponfee.web.framework.model.RedisKey;
import cn.ponfee.web.framework.service.RedisManagerService;
import code.ponfee.commons.json.Jsons;
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
public abstract class AbstractRedisManagerService implements RedisManagerService {

    protected static Logger logger = LoggerFactory.getLogger(AbstractRedisManagerService.class);
    private static final int MAX_VALUE_LENGTH = 200;

    protected @Resource RedisTemplate<String, Object> redis;

    protected abstract List<RedisKey> query4list(PageRequestParams params);

    protected abstract void onAddOrUpdate(RedisKey redisKey);

    protected abstract void onDelete(List<String> keys);

    @Override
    public Page<RedisKey> query4page(PageRequestParams params) {
        List<RedisKey> list = query4list(params);

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

        onAddOrUpdate(new RedisKey(key, type, value, expire0));
    }

    @Override
    public void delete(String... redisKeys) {
        if (ArrayUtils.isEmpty(redisKeys)) {
            return ;
        }

        List<String> keys = Arrays.asList(redisKeys);
        redis.delete(keys);
        onDelete(keys);
    }

    // ------------------------------------------------------------------others methods
    public RedisKey getAsString(RedisConnection conn, byte[] key) {
        return getAsString(key, conn.type(key), conn.ttl(key));
    }

    public RedisKey getAsString(byte[] key, DataType dataType, long ttl) {
        String value = null;
        switch (dataType) {
            case NONE:
                value = "[KEY NOT EXISTS]";
                ttl = -2;
                break;
            case STRING:
                try {
                    value = redis.execute((RedisCallback<String>) conn -> {
                        return deserializeAsString(redis.getValueSerializer(), conn.get(key));
                    });
                    if (value != null && value.length() > MAX_VALUE_LENGTH) {
                        value = value.substring(0, MAX_VALUE_LENGTH) + "...";
                    }
                } catch (Exception ex) {
                    logger.error("Get redis key value occur error.", ex);
                    value = Optional.ofNullable(value).orElse("[ERROR: " + ex.getMessage() + "]");
                }
                break;
            default:
                value = "[NOT STRING TYPE: " + dataType.name() + "]";
                break;
        }
        return new RedisKey(
            deserializeAsString(redis.getKeySerializer(), key), dataType, value, ttl
        );
    }

    private String deserializeAsString(RedisSerializer<?> serializer, byte[] data) {
        if (data == null) {
            return null;
        }
        if (serializer == null) {
            return new String(data);
        }
        try {
            Object o = serializer.deserialize(data);
            return o == null ? null : (o instanceof String) ? (String) o : Jsons.toJson(o);
        } catch (Exception e) {
            return new String(data);
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

    public enum MatchMode {
        LIKE {
            @Override
            public String build(String keyWildcard) {
                return StringUtils.isEmpty(keyWildcard)
                     ? ASTERISK
                     : ASTERISK + keyWildcard + ASTERISK;
            }
        },
        EQUAL {
            @Override
            public String build(String keyWildcard) {
                return keyWildcard;
            }
        },
        HEAD {
            @Override
            public String build(String keyWildcard) {
                return keyWildcard + ASTERISK;
            }
        },
        TAIL {
            @Override
            public String build(String keyWildcard) {
                return ASTERISK + keyWildcard;
            }
        };

        private static final String ASTERISK = "*";

        public abstract String build(String keyWildcard);
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
