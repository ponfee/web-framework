package cn.ponfee.web.framework.dto;

/**
 * The user data transfer object
 * 
 * @author Ponfee
 */
public class UserDto implements java.io.Serializable {

    private static final long serialVersionUID = 2257719461207975861L;

    private Long id; // 用户ID
    private String username; // 用户名
    private String nickname; // 昵称（姓名）
    private String mobilePhone; // 手机号码

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
