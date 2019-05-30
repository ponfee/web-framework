package cn.ponfee.web.framework.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.DataType;

/**
 * Redis key model
 * 
 * @author Ponfee
 */
public class RedisKey implements java.io.Serializable, Comparable<Object> {

    private static final long serialVersionUID = -134253108762088374L;

    private String key;
    private DataType type;
    private String expire;

    public RedisKey() {}

    public RedisKey(String key, DataType type, long expire) {
        this.key = key;
        this.type = type;
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
    public int compareTo(Object o) {
        if (o instanceof RedisKey) {
            return this.getKey().compareTo(((RedisKey) o).getKey());
        } else if (o instanceof String) {
            return this.getKey().compareTo((String) o);
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RedisKey) {
            return this.getKey().equals(((RedisKey) o).getKey());
        } else if (o instanceof CharSequence) {
            return StringUtils.equals(this.getKey(), (CharSequence) o);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return key == null ? 0 : key.hashCode();
    }

    public String[] split(String str) {
        return this.getKey().split(str);
    }

    public boolean contains(String str) {
        return this.getKey().contains(str);
    }

    public boolean startsWith(String str) {
        return this.getKey().startsWith(str);
    }

    public boolean endsWith(String str) {
        return this.getKey().endsWith(str);
    }

    public boolean equals(String str) {
        return this.getKey().equals(str);
    }

}
