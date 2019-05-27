package cn.ponfee.web.framework.model;

import java.util.Date;
import java.util.List;

/**
 * The model class for table t_user
 *
 * @author Ponfee
 */
public class User implements java.io.Serializable {

    private static final long serialVersionUID = 6316130995574942484L;

    public static final int STATUS_DISABLE = 0;
    public static final int STATUS_ENABLE = 1;

    private Long id; // 主键ID
    private String username; // 用户名
    private String password; // 密码：Bcrypt(SHA1(password))
    private String nickname; // 昵称（姓名）
    private String mobilePhone; // 手机号码
    private Integer status; // 状态：0禁用；1启用；
    private boolean deleted; // 是否已被删除：true是；false否；
    private Long createBy; // 创建人
    private Date createTm; // 创建时间
    private Long updateBy; // 更新人
    private Date updateTm; // 更新时间
    private Integer version; // 版本号（从1开始）

    // ------------------------------------------------非表字段
    private List<Role> roles; // 用户拥有的角色集（目前一个用户只对应一个角色）
    private List<Long> roleIds; // 用户角色ID集（目前一个用户只对应一个角色）
    private List<String> permits; // 用户拥有的权限ID集

    private String originPassword; // 原密码

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTm() {
        return updateTm;
    }

    public void setUpdateTm(Date updateTm) {
        this.updateTm = updateTm;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<String> getPermits() {
        return permits;
    }

    public void setPermits(List<String> permits) {
        this.permits = permits;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public User mask() {
        this.password = null;
        return this;
    }

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

}
