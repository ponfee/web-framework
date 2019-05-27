package cn.ponfee.web.framework.web;

import java.io.File;
import java.util.Locale;
import org.springframework.web.servlet.view.JstlView;

/**
 * 自定义JstlView
 * 
 * <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
 *   <property name="viewClass" value="cn.ponfee.web.framework.web.ExtendedJstlView" />
 *   <property name="prefix" value="/WEB-INF/view/jsp/" />
 *   <property name="suffix" value=".jsp" />
 *   <property name="order" value="1" />
 * </bean>
 * 
 * @author Ponfee
 */
public class ExtendedJstlView extends JstlView {

    @Override
    public boolean checkResource(Locale locale) {
        File file = new File(this.getServletContext().getRealPath("/") + getUrl());
        return file.exists();
    }
}
