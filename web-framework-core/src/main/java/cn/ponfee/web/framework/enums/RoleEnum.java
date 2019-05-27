package cn.ponfee.web.framework.enums;

import cn.ponfee.web.framework.dto.UserDto;

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

    public static RoleEnum from(UserDto u) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.name().equalsIgnoreCase(u.getRoleCode())) {
                return role;
            }
        }
        return GENERAL;
    }
}
