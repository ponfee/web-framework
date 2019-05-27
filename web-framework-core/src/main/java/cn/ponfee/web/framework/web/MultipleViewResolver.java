package cn.ponfee.web.framework.web;

import java.util.Locale;
import java.util.Map;

import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import code.ponfee.commons.web.WebContext;
import code.ponfee.commons.web.WebUtils;

/**
 * 多类型的的页面处理
 * TODO
 * 
 * @author Ponfee
 */
public class MultipleViewResolver extends WebApplicationObjectSupport implements ViewResolver {
    private String defaultViewResolverName = null;
    private Map<String, ViewResolver> resolvers;

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        // 根据返回的view-name后缀名来处理：return hello.suffix;
        /*for (Iterator<Entry<String, ViewResolver>> iter = resolvers.entrySet().iterator(); iter.hasNext();) {
            ViewResolver viewResolver = iter.next().getValue();
            View view = viewResolver.resolveViewName(viewName, locale);
            if (view != null) return view;
        }
        return null;*/

        // 根据请求路径的后缀名来处理
        ViewResolver viewResolver = resolvers.get(WebUtils.getUrlSuffix(WebContext.getRequest()));
        if (viewResolver == null) {
            viewResolver = resolvers.get(defaultViewResolverName);
        }
        return viewResolver.resolveViewName(viewName, locale);

    }

    public String getDefaultViewResolverName() {
        return defaultViewResolverName;
    }

    public void setDefaultViewResolverName(String defaultViewResolverName) {
        this.defaultViewResolverName = defaultViewResolverName;
    }

    public Map<String, ViewResolver> getResolvers() {
        return resolvers;
    }

    public void setResolvers(Map<String, ViewResolver> resolvers) {
        this.resolvers = resolvers;
    }

}
