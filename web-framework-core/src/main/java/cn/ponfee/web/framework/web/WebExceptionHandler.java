package cn.ponfee.web.framework.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;

import code.ponfee.commons.model.Result;
import code.ponfee.commons.web.AbstractWebExceptionHandler;
import code.ponfee.commons.web.WebUtils;

@ControllerAdvice
public class WebExceptionHandler extends AbstractWebExceptionHandler {

    @Override
    protected void handle(HttpServletRequest req, HttpServletResponse resp, String page, int code, String message) {
        if (LOGGER.isDebugEnabled() || WebUtils.isAjax(req)) {
            WebUtils.respJson(resp, new Result<>(code, message)); // resp.setStatus(code);
        } else {
            try {
                //req.getRequestDispatcher(page).forward(req, resp);
                resp.sendRedirect(resp.encodeRedirectURL(WebUtils.getContextPath(req) + page));
            } catch (IOException e) {
                LOGGER.error("Redirect page occur error.", e);
            }
        }
    }

}
