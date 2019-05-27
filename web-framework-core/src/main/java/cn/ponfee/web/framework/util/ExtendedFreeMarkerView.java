package cn.ponfee.web.framework.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 * 扩展FreeMarkerView
 * 
 * @author Ponfee
 */
public class ExtendedFreeMarkerView extends FreeMarkerView {

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        String basePath = getBasePath(request);
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        model.put("basePath", basePath);
        super.exposeHelpers(model, request);
    }

    private static String getBasePath(HttpServletRequest request) {
        String path = "//" + request.getServerName();
        int port = request.getServerPort();
        if (port != 80) path += ":" + port;
        return path + request.getContextPath();
    }
}
