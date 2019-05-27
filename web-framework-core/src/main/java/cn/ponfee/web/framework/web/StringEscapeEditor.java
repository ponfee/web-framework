package cn.ponfee.web.framework.web;

import java.beans.PropertyEditorSupport;

import org.apache.commons.text.StringEscapeUtils;

/**
 * XSS解决方案之一：spring mvc controller添加输入过滤
 * 使用方法：放到controller父类中
 * -------------------------------
 *     @InitBinder
 *     public void initBinder(WebDataBinder binder) {
 *         binder.registerCustomEditor(String.class, new StringEscapeEditor());
 *     }
 * -------------------------------
 * @author fupf
 */
public class StringEscapeEditor extends PropertyEditorSupport {
    private boolean escapeHTML = true;

    private boolean escapeJavaScript = true;

    private boolean escapeSQL = true;

    public StringEscapeEditor() {}

    public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript, boolean escapeSQL) {
        this.escapeHTML = escapeHTML;
        this.escapeJavaScript = escapeJavaScript;
        this.escapeSQL = escapeSQL;
    }

    @Override
    public void setAsText(String text) {
        String value = text;
        if (escapeHTML) {
            value = StringEscapeUtils.escapeHtml4(value);
        }
        if (escapeJavaScript) {
            value = StringEscapeUtils.escapeEcmaScript(value);
        }
        if (escapeSQL) {
            value = StringEscapeUtils.escapeJava(value);
        }

        setValue(value);
    }

    @Override
    public String getAsText() {
        return getValue() == null ? "" : getValue().toString();
    }
}
