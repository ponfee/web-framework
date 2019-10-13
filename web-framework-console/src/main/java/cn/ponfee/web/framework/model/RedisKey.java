package cn.ponfee.web.framework.model;

import java.util.Objects;

import org.springframework.data.redis.connection.DataType;

/**
 * Redis key model
 * 
 * @author Ponfee
 */
public class RedisKey implements java.io.Serializable, Comparable<RedisKey> {

    private static final long serialVersionUID = -134253108762088374L;

    private String key;
    private DataType type;
    private String value;
    private String expire;

    public RedisKey() {}

    public RedisKey(String key) {
        this(key, DataType.NONE, null, -2);
    }

    public RedisKey(String key, DataType type, String value, long expire) {
        Objects.requireNonNull(key);
        this.key = key;
        this.type = type;
        this.value = value;
        this.expire = expire == -2 
                    ? "EXPIRED" 
                    : expire == -1 
                    ? "INFINITY" 
                    : String.valueOf(expire);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public void setExpire(long expire) {
        this.expire = String.valueOf(expire);
    }

    @Override
    public int compareTo(RedisKey o) {
        return this.key.compareTo(o.key);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RedisKey) {
            return this.key.equals(((RedisKey) o).key);
        } else if (o instanceof CharSequence) {
            return this.key.equals(((CharSequence) o).toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    public boolean contains(String str) {
        return this.key.contains(str);
    }

    public boolean startsWith(String str) {
        return this.key.startsWith(str);
    }

    public boolean endsWith(String str) {
        return this.key.endsWith(str);
    }

    public boolean equals(CharSequence o) {
        return this.key.equals(o.toString());
    }
}
