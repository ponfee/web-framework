package cn.ponfee.web.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import code.ponfee.commons.io.Files;
import code.ponfee.commons.io.StringPrintWriter;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * FreeMarker Template Utils
 * 
 * @author Ponfee
 */
public final class FreeMarkerTemplateUtils {
    private FreeMarkerTemplateUtils() {}

    private static final TemplateLoader SOURCE_TEMPLATE_LOADER = new SourceTemplateLoader();
    private static final TemplateLoader URL_TEMPLATE_LOADER = new ExtendedURLTemplateLoader();
    private static final Version VERSION = Configuration.VERSION_2_3_28;

    // 加载指定ftl文件的模板
    public static Template load4class(String basepackage, String name) {
        Configuration cfg = new Configuration(VERSION);
        cfg.setClassForTemplateLoading(Thread.currentThread().getStackTrace()[2].getClass(), basepackage);
        return getTemplate(cfg, name);
    }

    public static Template load4dir(String basedir, String name) {
        Configuration cfg = new Configuration(VERSION);
        try {
            cfg.setDirectoryForTemplateLoading(new File(basedir));
            return getTemplate(cfg, name);
        } catch (IOException e) {
            throw new RuntimeException(basedir + " not found", e);
        }
    }

    public static Template load4source(String source) {
        Configuration cfg = new Configuration(VERSION);
        cfg.setTemplateLoader(SOURCE_TEMPLATE_LOADER);
        return getTemplate(cfg, source);
    }

    public static Template load4url(String url) {
        Configuration cfg = new Configuration(VERSION);
        cfg.setTemplateLoader(URL_TEMPLATE_LOADER);
        return getTemplate(cfg, url);
    }

    public static Template load4conf(Configuration conf, String name) {
        try {
            return conf.getTemplate(name);
        } catch (IOException e) {
            throw new RuntimeException("template " + name + " load error", e);
        }
    }

    public static void print(Template tpl, Object model, OutputStream out) {
        try {
            tpl.process(model, new PrintWriter(out));
            out.flush();
        } catch (TemplateException e) {
            throw new RuntimeException("template " + tpl.getName() + " process error", e);
        } catch (IOException e) {
            throw new RuntimeException("template " + tpl.getName() + " load error", e);
        }
    }

    public static String print(Template tpl, Object model) {
        try {
            StringPrintWriter writer = new StringPrintWriter(); // StringWriter
            tpl.process(model, writer);
            return writer.getString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("template " + tpl.getName() + " process error", e);
        }
    }

    // ---------------------------------private methods---------------------------------
    private static Template getTemplate(Configuration cfg, String name) {
        cfg.setDefaultEncoding(Files.UTF_8);
        try {
            cfg.setLocalizedLookup(false);
            return cfg.getTemplate(name, Files.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
