package cn.ponfee.web.framework.model;

import java.util.Date;
import java.util.List;

/**
 * RBAC
 * The model class for table t_role
 * 
 * @author Ponfee
 */
public class Role implements java.io.Serializable {

    private static final long serialVersionUID = -3319412966327949360L;
    public static final int STATUS_DISABLE = 0; // 不可用
    public static final int STATUS_ENABLE = 1; // 可用

    private Long id; // 主键ID
    private String roleCode; // 角色代码
    private String roleName; // 角色名称
    private Integer status; // 状态：1不可用；2可用；
    private Long createBy;
    private Date createTm;
    private Long updateBy;
    private Date updateTm;
    private Integer version;

    private List<String> permitIds; // 权限ID列表集合
    //private Map<String, Object> map;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public List<String> getPermitIds() {
        return permitIds;
    }

    public void setPermitIds(List<String> permitIds) {
        this.permitIds = permitIds;
    }

}
