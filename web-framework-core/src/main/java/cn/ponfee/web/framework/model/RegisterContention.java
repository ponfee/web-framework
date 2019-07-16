package cn.ponfee.web.framework.model;

/**
 * The model class for table t_register_contention
 * 
 * @author Ponfee
 */
public class RegisterContention implements java.io.Serializable {

    private static final long serialVersionUID = -7495851037267445803L;

    private Long id;
    private String attr;
    private String ckey;
    private String cval;

    public RegisterContention() {}

    public RegisterContention(String attr, String ckey, String cval) {
        this.attr = attr;
        this.ckey = ckey;
        this.cval = cval;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
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
