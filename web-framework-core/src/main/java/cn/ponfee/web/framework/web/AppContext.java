package cn.ponfee.web.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import cn.ponfee.web.framework.model.User;

/**
 * The web context holder
 * 
 * @author Ponfee
 */
public class AppContext {

    private static final String CURRENT_USER = "current_user";

    public static void currentUser(User user) {
        getRequest().setAttribute(CURRENT_USER, user.mask());
    }

    public static User currentUser() {
        return (User) getRequest().getAttribute(CURRENT_USER);
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}
