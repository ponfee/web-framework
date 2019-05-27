package cn.ponfee.web.framework.model;

/**
 * RBAC
 * The model class for table t_role_permit
 * 
 * @author Ponfee
 */
public class RolePermit implements java.io.Serializable {

    private static final long serialVersionUID = -4804633191946843822L;

    private Long roleId; // 角色ID
    private String permitId; // 权限ID

    public RolePermit() {}

    public RolePermit(Long roleId, String permitId) {
        super();
        this.roleId = roleId;
        this.permitId = permitId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getPermitId() {
        return permitId;
    }

    public void setPermitId(String permitId) {
        this.permitId = permitId;
    }

}
