package cn.ponfee.web.framework.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * XSS漏洞过滤器
 * @author fupf
 */
public class XSSFilter implements Filter {

    private Charset charset;

    // 需要拦截的JS字符关键字
    private static final String[] SAFELESS = {
        "<script", "</script", "%3cscript", "%3c/script", "set-cookie", "<iframe", "</iframe",
        "<frame", "</frame", "%3ciframe", "%3c/iframe", "%3cframe", "%3c/frame", "javascript:", 
        "src=\"javascript:", "alert%2528", "alert%28", "alert("
    };

    @Override
    public void init(FilterConfig config) {
        String encoding = config.getInitParameter("encoding");
        try {
            charset = Charset.forName(encoding);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {}

    /**
     * XSS漏洞解决
     */
    public final class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
        HttpServletRequest originRequest = null; // 原生的request
        boolean isUpload; // 判断是否是上传（上传忽略）

        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            originRequest = request;

            if (!"POST".equalsIgnoreCase(request.getMethod())) {
                isUpload = false;
            } else {
                String type = request.getContentType();
                isUpload = (type != null && type.toLowerCase().startsWith("multipart/"));
            }
        }

        /**
         * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
         * 如果需要获得原始的值，则通过super.getParameter(name)来获取<br/>
         */
        @Override
        public String getParameter(String name) {
            return htmlEscape(super.getParameter(name));
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }

            for (int i = 0; i < values.length; i++) {
                values[i] = htmlEscape(values[i]);
            }
            return values;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> params = super.getParameterMap();
            if (params == null || params.isEmpty()) {
                return params;
            }

            Map<String, String[]> map = new HashMap<>(params.size() << 1);
            for (Iterator<Entry<String, String[]>> t = params.entrySet().iterator(); t.hasNext();) {
                Entry<String, String[]> entry = t.next();
                String[] value = entry.getValue();
                if (value != null) {
                    for (int n = value.length, i = 0; i < n; i++) {
                        value[i] = htmlEscape(value[i]);
                    }
                }
                map.put(entry.getKey(), value);
            }
            return map;
        }

        /**
         * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
         * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
         * getHeaderNames 也可能需要覆盖
         */
        @Override
        public String getHeader(String name) {
            return htmlEscape(super.getHeader(name));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (isUpload) {
                return super.getInputStream();
            }

            try (InputStream input = super.getInputStream()) {
                //String data = htmlEscape(IOUtils.toString(input, Charset.defaultCharset()));
                //InputStream stream = new ByteArrayInputStream(data.getBytes(Charset.defaultCharset()));
                String data = htmlEscape(IOUtils.toString(input, charset));
                InputStream stream = new ByteArrayInputStream(data.getBytes(charset));
                return new ServletInputStream() {
                    @Override
                    public int read() throws IOException {
                        return stream.read();
                    }

                    @Override
                    public boolean isFinished() {
                        return false;
                    }

                    @Override
                    public boolean isReady() {
                        return false;
                    }

                    @Override
                    public void setReadListener(ReadListener readListener) {
                        // nothing to do
                    }
                };
            }
        }

        /**
         * 获取最原始的request
         * @return
         */
        public HttpServletRequest getOriginRequest() {
            return originRequest;
        }

        /**
         * 获取最原始的request的静态方法
         * @return
         */
        public HttpServletRequest getOriginRequest(HttpServletRequest req) {
            if (req instanceof XssHttpServletRequestWrapper) {
                return ((XssHttpServletRequestWrapper) req).getOriginRequest();
            }
            return req;
        }

        /**
         * 转义成html实体
         * @param source
         * @return
         */
        private String htmlEscape(String source) {
            if (StringUtils.isBlank(source)) {
                return source;
            }

            for (String str : SAFELESS) {
                //source = source.replace(str, StringUtils.EMPTY);
                source = StringUtils.replaceIgnoreCase(source, str, StringUtils.EMPTY);
            }
            //return org.springframework.web.util.HtmlUtils.htmlEscape(source);
            //return freemarker.template.utility.StringUtil.XMLEncNA(str);
            //return org.apache.commons.text.StringEscapeUtils.escapeHtml4(str);
            //return org.springframework.web.util.HtmlUtils.htmlEscape(str);
            //return ESAPI.encoder().encodeForHTML(str);
            //return ESAPI.encoder().encodeForSQL(ORACLE_CODEC, param)

            StringBuilder builder = new StringBuilder(source.length());
            char c;
            for (int n = source.length(), i = 0; i < n; i++) {
                c = source.charAt(i);
                switch (c) {
                    case '<':
                        builder.append("&lt;");
                        break;
                    case '>':
                        builder.append("&gt;");
                        break;
                    /*case '"':
                        builder.append("&quot;");
                        break;
                    case '\'':
                        builder.append("&#39;");
                        break;
                    case '&':
                        builder.append("&amp;");
                        break;
                    case '%':
                        builder.append("&#37;");
                        break;
                    case ';':
                        builder.append("&#59;");
                        break;
                    case '(':
                        builder.append("&#40;");
                        break;
                    case ')':
                        builder.append("&#41;");
                        break;
                    case '+':
                        builder.append("&#43;");
                        break;*/
                    case 10:
                    case 13:
                        break;
                    default:
                        builder.append(c);
                        break;
                }
            }
            return builder.toString();
        }
    }

}
