package cn.ponfee.web.framework.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.URLTemplateLoader;

/**
 * load from URL
 * 
 * @author Ponfee
 */
class ExtendedURLTemplateLoader extends URLTemplateLoader {
    private final Map<String, URL> templates = new HashMap<>();

    @Override
    protected URL getURL(String url) {
        URL _url = templates.get(url);
        if (_url == null) try {
            _url = new URL(url);
            templates.put(url, _url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return _url;
    }
}
