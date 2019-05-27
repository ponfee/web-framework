package cn.ponfee.web.framework.model;

/**
 * RBAC
 * The model class for table t_user_role
 * 
 * @author Ponfee
 */
public class UserRole implements java.io.Serializable {

    private static final long serialVersionUID = 7149542458908172859L;

    private Long userId; // 用户ID
    private Long roleId; // 角色ID

    public UserRole() {}

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
