package cn.ponfee.web.framework.enums;

/**
 * 角色
 * 
 * @author Ponfee
 */
public enum RoleEnum {

    MANAGER("管理员"), //
    GENERAL("普通用户"), //
    ;

    private final String desc;

    RoleEnum(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

}
