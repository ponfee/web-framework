package cn.ponfee.web.framework.model;

/**
 * The model class for table t_register_contention
 * 
 * @author Ponfee
 */
public class RegisterContention implements java.io.Serializable {

    private static final long serialVersionUID = -7495851037267445803L;

    private Long id;
    private String type;
    private String ckey;
    private String cval;

    public RegisterContention() {}

    public RegisterContention(String type, String ckey, String cval) {
        this.type = type;
        this.ckey = ckey;
        this.cval = cval;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey;
    }

    public String getCval() {
        return cval;
    }

    public void setCval(String cval) {
        this.cval = cval;
    }

}
