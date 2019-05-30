package cn.ponfee.web.framework.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import code.ponfee.commons.web.WebUtils;

/**
 * 安全字符过滤，对可能攻击的非法字符进行处理，处理的漏洞包括： 
 *  > 加密会话（SSL）Cookie 中缺少Secure 属性 
 *  > sql注入 
 *  > 跨站点脚本编制 
 *  > 跨站点请求伪造（CSRF）
 * @author fupf
 */
public class SecurityFilter implements Filter {

    private static final String INVALID_PARAM = "invalid_param"; // 无效参数放入request attribute
    //private static final String SESSIONID = "jsessionid"; // 会话跟踪id

    // sql关键字
    private static final String[] INJ_STRS = {
        //"select ", " and ", " or ", " count ", " * ", " % ", " ; ", " , ", " ' ", " chr ", " char ", 
        "exec ", "insert ", "delete ", "update ", " mid ", " master ", "truncate ", "declare ", " - ", " + "
    };

    // 需要拦截的JS字符关键字
    private static final String[] SAFELESS = {
        "<script", "</script", "%3cscript", "%3c/script", "set-cookie", "<iframe", "</iframe",
        "<frame", "</frame", "%3ciframe", "%3c/iframe", "%3cframe", "%3c/frame", "<body", "</body",
        "%3cbody", "%3c/body", "javascript:", "src=\"javascript:", "<img", "%3cimg", " style='",
        "%20style='", "alert%2528", "alert%28", "alert(", "%3c", "%3e", "%3c/", "/%3e"
    };

    @Override
    public void init(FilterConfig cfg) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, 
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (!sqlInjectValidate(request) || !xssValidate(request) /* || !csrfValidate(request) */) {
            WebUtils.respJson((HttpServletResponse) resp, "请求参数非法：" + request.getAttribute(INVALID_PARAM));
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}

    /**
     * 漏洞解决：防止sql注入
     * @param request
     * @return
     */
    private boolean sqlInjectValidate(HttpServletRequest request) {
        Iterator<String[]> params = request.getParameterMap().values().iterator(); // 获取所有的表单参数
        while (params.hasNext()) {
            for (String str : params.next()) {
                str = str.toLowerCase(); //统一转为小写
                for (int j = 0; j < INJ_STRS.length; j++) {
                    if (str.indexOf(INJ_STRS[j]) > -1) {
                        request.setAttribute(INVALID_PARAM, INJ_STRS[j]);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 漏洞解决：跨站点脚本编制
     * @param request
     * @return
     */
    private boolean xssValidate(HttpServletRequest request) {
        if (!isSafe(request.getRequestURI())) {
            return false;
        }

        Enumeration<?> pms = request.getParameterNames();
        while (pms.hasMoreElements()) {
            String value = request.getParameter((String) pms.nextElement());
            if (!isSafe(value)) {
                request.setAttribute(INVALID_PARAM, value);
                return false;
            }
        }

        return true;
    }

    private boolean isSafe(String str) {
        if (StringUtils.isBlank(str)) {
            return true;
        }

        for (String s : SAFELESS) {
            if (str.toLowerCase().contains(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 漏洞解决：跨站点请求伪造（CSRF）
     * @param
     * @return
     * @throws
     */
    /*private boolean csrfValidate(HttpServletRequest request) {
        return request.getSession().getId().equals(request.getParameter("jsessionid"));
    }*/

    /**
     * 漏洞解决：加密会话（SSL）Cookie 中缺少Secure 属性
     * 注：慎用，SSL时才使用
     * @param
     * @return
     * @throws
     */
    /*private void httpsSecHandler(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = request.getCookies() == null ? null : request.getCookies()[0];
        if (cookie != null) {
            //cookie.setMaxAge(3600); cookie.setSecure(true); resp.addCookie(cookie);
            //Servlet 2.5不支持在Cookie上直接设置HttpOnly属性
            cookie.setPath("/");
            cookie.setSecure(true);
            String sessionid = request.getSession().getId();
            StringBuffer buf = new StringBuffer();
            buf.append("JSESSIONID=" + sessionid + "; ");
            //String contextPath = request.getContextPath();
            //buf.append("Path=" + contextPath + "; ");
            buf.append("Path=/; ");
            buf.append("Secure; ");
            buf.append("HttpOnly; ");
            //Calendar cal = Calendar.getInstance(); cal.add(Calendar.HOUR, 1); 
            //Date date = cal.getTime(); 
            //Locale locale = Locale.CHINA; 
            //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale); 
            //buf.append("Expires=" + sdf.format(date));
            response.setHeader("SET-COOKIE", buf.toString());
        }
    }*/

    /**
     * 转向错误页面
     * @param request
     * @param response
     * @param errMsg
     * @throws ServletException
     * @throws IOException
     */
    /*private void forwardErrPage(HttpServletRequest request, HttpServletResponse response, 
                                String errMsg) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.setAttribute("errMsg", errMsg);
        request.getRequestDispatcher("/res/page/403.jsp").forward(request, response);
    }*/
}
