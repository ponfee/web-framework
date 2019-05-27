package cn.ponfee.web.framework.startup;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.ponfee.web.framework.enums.RoleEnum;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.service.IRoleService;
import code.ponfee.commons.util.SpringContextHolder;

/**
 * spring容器启动完成后执行
 * 
 * @author Ponfee
 */
@Component
public class SpringStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private static long roleManager;
    private static long roleGeneral;

    /**
     * spring初始化完成后执行一次
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // check the container is root container
        if (event.getApplicationContext().getParent() == null) {
            // do something for application init
            //System.out.println("*******************SpringStartupListener init*******************");
            IRoleService roleService = SpringContextHolder.getBean(IRoleService.class);
            Role manager = roleService.getByRoleCode(RoleEnum.MANAGER.name()).getData();
            Role general = roleService.getByRoleCode(RoleEnum.GENERAL.name()).getData();
            roleManager = manager != null ? manager.getId() : 0;
            roleGeneral = general != null ? general.getId() : 0;
        }
    }

    public static long roleManager() {
        return roleManager;
    }

    public static long roleGeneral() {
        return roleGeneral;
    }
}
