package cn.ponfee.web.framework.model;

/**
 * The model class for table t_register_contention
 * 
 * @author Ponfee
 */
public class RegisterContention implements java.io.Serializable {

    private static final long serialVersionUID = -7495851037267445803L;

    private Long id;
    private String typ; // 类型type
    private String key; // 键key
    private String val; // 值value

    public RegisterContention() {}

    public RegisterContention(String typ, String key, String val) {
        this.typ = typ;
        this.key = key;
        this.val = val;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

}
