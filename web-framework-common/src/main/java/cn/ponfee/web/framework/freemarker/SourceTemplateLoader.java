package cn.ponfee.web.framework.freemarker;

import code.ponfee.commons.jce.digest.DigestUtils;
import freemarker.cache.StringTemplateLoader;

/**
 * Load from source string
 * 
 * @author Ponfee
 */
class SourceTemplateLoader extends StringTemplateLoader {
    private static final String NAME_PREFIX = "source_";

    @Override
    public Object findTemplateSource(String source) {
        String name = NAME_PREFIX + DigestUtils.md5Hex(source);
        Object tpl = super.findTemplateSource(name);
        if (tpl == null) {
            super.putTemplate(name, source, 0);
            tpl = super.findTemplateSource(name);
        }
        return tpl;
    }

}
