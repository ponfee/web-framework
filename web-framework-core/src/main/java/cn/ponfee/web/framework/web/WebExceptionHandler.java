package cn.ponfee.web.framework.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;

import code.ponfee.commons.web.AbstractWebExceptionHandler;

@ControllerAdvice
public class WebExceptionHandler extends AbstractWebExceptionHandler {

    public WebExceptionHandler() {
        super("/static/page/401.html", "/static/page/500.html", "Server error.");
    }

    @Override
    protected void handErrorPage(HttpServletRequest req, HttpServletResponse resp, String page) {
        try {
            req.getRequestDispatcher(page).forward(req, resp);
            //resp.sendRedirect(resp.encodeRedirectURL(WebUtils.getContextPath(req) + page));
        } catch (IOException | ServletException e) {
            LOGGER.error("Forward page occur error.", e);
        }
    }

}
