package cn.ponfee.web.framework.model;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import code.ponfee.commons.tree.BaseNode;
import code.ponfee.commons.tree.TreeNode;

/**
 * RBAC role model
 * 
 * This class for database table t_permit
 * 
 * @author Ponfee
 */
public class Permit implements java.io.Serializable {

    private static final long serialVersionUID = -2785926701672694539L;

    private static final Comparator<? super TreeNode<String, Permit>> COMPARATOR = 
        TreeNode.comparingThenComparingNid(Permit::getOrders);

    public static final int STATUS_DISABLE = 0; // 不可用
    public static final int STATUS_ENABLE = 1; // 可用

    public static final int TYPE_MENU = 1; // 菜单
    public static final int TYPE_BUTTON = 2; // 按钮

    private String permitId;    // 权限ID
    private String parentId;    // 父权限ID
    private String permitName;  // 权限名称
    private Integer permitType; // 权限类型
    private String permitUrl;   // 权限URL
    private String permitIcon;  // 权限图标
    private Integer orders;     // 次序
    private Integer status;     // 状态：1不可用；2可用；
    private Long createBy;
    private Date createTm;
    private Long updateBy;
    private Date updateTm;
    private Integer version;

    // -----------------------------------------------------------getter/setter
    public String getPermitId() {
        return permitId;
    }

    public void setPermitId(String permitId) {
        this.permitId = permitId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPermitName() {
        return permitName;
    }

    public void setPermitName(String permitName) {
        this.permitName = permitName;
    }

    public String getPermitIcon() {
        return permitIcon;
    }

    public void setPermitIcon(String permitIcon) {
        this.permitIcon = permitIcon;
    }

    public Integer getPermitType() {
        return permitType;
    }

    public void setPermitType(Integer permitType) {
        this.permitType = permitType;
    }

    public String getPermitUrl() {
        return permitUrl;
    }

    public void setPermitUrl(String permitUrl) {
        this.permitUrl = permitUrl;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
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

    public static TreeNode<String, Permit> buildTree(List<Permit> permits) {
        TreeNode<String, Permit> root = TreeNode.of(TreeNode.DEFAULT_ROOT_ID, COMPARATOR);
        root.mount(permits.stream().map(Permit::toNode).collect(Collectors.toList()));
        return root;
    }

    private static BaseNode<String, Permit> toNode(Permit p) {
        return new BaseNode<>(
            p.getPermitId(), p.getParentId(), 
            p.getStatus() == Permit.STATUS_ENABLE, p
        );
    }
}
