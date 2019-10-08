package cn.ponfee.web.framework.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.model.RedisKey;
import cn.ponfee.web.framework.service.RedisManagerService;
import cn.ponfee.web.framework.service.impl.RedisManagerServiceImpl.MatchMode;
import code.ponfee.commons.export.HtmlExporter;
import code.ponfee.commons.export.Table;
import code.ponfee.commons.export.Thead;
import code.ponfee.commons.export.Tmeta;
import code.ponfee.commons.export.Tmeta.Align;
import code.ponfee.commons.export.Tmeta.Type;
import code.ponfee.commons.http.ContentType;
import code.ponfee.commons.io.Files;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.PaginationHtmlBuilder;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.reflect.BeanConverts;
import code.ponfee.commons.tree.BaseNode;
import code.ponfee.commons.util.Enums;
import code.ponfee.commons.web.WebUtils;

/**
 * Redis manager http api
 * 
 * @author Ponfee
 */
@RequestMapping("redis/mgr")
@RestController
public class RedisManagerController {

    private @Value("${web.context.path:}") String contextPath;

    private static final List<BaseNode<Integer, Thead>> THEADS = Arrays.asList(
        new BaseNode<>(1, 0, 1, new Thead("key",    new Tmeta(Type.CHAR, null, Align.LEFT,   true, null), null)),
        new BaseNode<>(2, 0, 2, new Thead("type",   new Tmeta(Type.CHAR, null, Align.CENTER, true, null), null)),
        new BaseNode<>(3, 0, 3, new Thead("expire", new Tmeta(Type.CHAR, null, Align.CENTER, true, null), null))
    );

    private static final List<String> EXPIRES = Arrays.asList("ALL", "INFINITY");

    private @Resource RedisManagerService service;

    @GetMapping("page")
    public Result<Page<RedisKey>> query4page(PageRequestParams params) {
        return Result.success(service.query4page(params));
    }

    @GetMapping("view")
    public void query4view(PageRequestParams params, HttpServletResponse resp) {
        Table<RedisKey> table = new Table<>(THEADS, rk -> BeanConverts.toArray(rk, "key", "type", "expire"));
        Page<RedisKey> page = service.query4page(params);
        page.process(row -> table.addRow(row));
        table.toEnd();
        try (HtmlExporter exporter = new HtmlExporter()) {
            exporter.build(table);
            PaginationHtmlBuilder builder = PaginationHtmlBuilder.newBuilder(
                "Redis Manager", contextPath + "/redis/mgr/view", page
            );
            builder.table(exporter.body())
                   .scripts(PaginationHtmlBuilder.CDN_JQUERY)
                   .form(buildForm(params))
                   .params(params)
                   .foot(buildFoot());
            WebUtils.response(resp, ContentType.TEXT_HTML.value(), builder.build(), Files.UTF_8);
        } 
    }

    @PutMapping("set")
    public Result<Void> addOrUpdateRedisEntry(
        @RequestParam("key") String key, 
        @RequestParam("value") String value, 
        @RequestParam(value = "expire",    required = false) Long expire, 
        @RequestParam(value = "dataType",  required = false) String dataType, 
        @RequestParam(value = "valueType", required = false) String valueType
    ) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return Result.failure(ResultCode.BAD_REQUEST, "key or value cannot be null.");
        }
        service.addOrUpdateRedisEntry(key, value, expire, dataType, valueType);
        return Result.success();
    }

    @DeleteMapping("delete")
    public Result<Void> delete(@RequestBody String... keys) {
        service.delete(keys);
        return Result.success();
    }

    @PostMapping("refresh")
    public Result<Void> refresh() {
        service.refresh();
        return Result.success();
    }

    // ------------------------------------------------------------private methods
    private String buildForm(PageRequestParams params) {
        MatchMode matchmode = Enums.ofIgnoreCase(MatchMode.class, params.getString("matchmode"), MatchMode.HEAD);
        String expiretype = params.getString("expiretype");
        return new StringBuilder(2048)
            .append("<select name=\"matchmode\">\n")
            .append("  <option value=\"HEAD\"").append(matchmode(matchmode, MatchMode.HEAD)).append(">HEAD</option>\n")
            .append("  <option value=\"LIKE\"").append(matchmode(matchmode, MatchMode.LIKE)).append(">LIKE</option>\n")
            .append("  <option value=\"TAIL\"").append(matchmode(matchmode, MatchMode.TAIL)).append(">TAIL</option>\n")
            .append("  <option value=\"EQUAL\"").append(matchmode(matchmode, MatchMode.EQUAL)).append(">EQUAL</option>\n")
            .append("</select>\n")
            .append("<select name=\"expiretype\">\n")
            .append("  <option value=\"ALL\"").append(expiretype(expiretype, "ALL")).append(">ALL</option>\n")
            .append("  <option value=\"INFINITY\"").append(expiretype(expiretype, "INFINITY")).append(">INFINITY</option>\n")
            .append("</select>\n")
            .append("<input type=\"text\" name=\"keyword\" value=\"").append(params.getString("keyword")).append("\"/>\n")
            .append("<input type=\"submit\" value=\"search\"/>\n")
            .append("<input type=\"button\" onclick=\"refKey()\" value=\"refresh\" />")
            .append("<div style=\"width:100%;height:3px;\"></div>")
            .toString();
    }

    private String buildFoot() {
        return new StringBuilder(HtmlExporter.HORIZON)
            .append("<form method=\"DELETE\" name=\"delete\">")
            .append("<input type=\"text\" name=\"key\" />")
            .append("<input type=\"button\" onclick=\"delKey()\" value=\"delete\" />")
            .append("</form><br/>\n")

            .append(HtmlExporter.HORIZON)
            .append("<form method=\"PUT\" name=\"set\">")
            .append("<select name=\"dataType\">")
            .append("  <option value=\"string\">string</option>")
            .append("  <option value=\"list\">list</option>")
            .append("  <option value=\"set\">set</option>")
            .append("  <option value=\"zset\">zset</option>")
            .append("  <option value=\"hash\">hash</option>")
            .append("</select>")
            .append("<input type=\"text\" name=\"key\" /> &nbsp;&nbsp;&nbsp;")
            .append("<select name=\"valueType\">")
            .append("  <option value=\"raw\">raw</option>")
            .append("  <option value=\"b64\">b64</option>")
            .append("</select>")
            .append("<input type=\"text\" name=\"value\" /> &nbsp;&nbsp;&nbsp;")
            .append("expire:<input type=\"text\" name=\"expire\" /> &nbsp;&nbsp;&nbsp;")
            .append("<input type=\"button\" onclick=\"setKey()\" value=\"set\" />")
            .append("</form><br/>\n")
            .append(HtmlExporter.HORIZON)

            .append("\n<script>\n")

            .append("function delKey(){")
            .append("$.ajax({url:'"+contextPath+"/redis/mgr/delete',type:'DELETE',dataType:'json',contentType:'application/json;charset=utf-8',")
            .append("data:JSON.stringify([$(\"form[name='delete'] input[name='key']\").val()]),success:function(result){alert(result.msg)}});")
            .append("}\n")

            .append("function refKey(){")
            .append("$.ajax({url:'"+contextPath+"/redis/mgr/refresh',type:'POST',success:function(result){alert(result.msg)}});")
            .append("}\n")

            .append("function setKey(){")
            .append("$.ajax({url:'"+contextPath+"/redis/mgr/set',type:'PUT',dataType:'json',data:{")
            .append("key:$(\"form[name='set'] input[name='key']\").val(),")
            .append("value:$(\"form[name='set'] input[name='value']\").val(),")
            .append("expire:$(\"form[name='set'] input[name='expire']\").val(),")
            .append("dataType:$(\"form[name='set'] select[name='dataType']\").val(),")
            .append("valueType:$(\"form[name='set'] select[name='valueType']\").val()")
            .append("},success:function(result){alert(result.msg)}});")
            .append("}\n")

            .append("</script>")
            .toString();
    }

    private String matchmode(MatchMode actual, MatchMode expect) {
        return actual == expect ? " selected=\"selected\"" : "";
    }

    private String expiretype(String actual, String expect) {
        if (!EXPIRES.contains(actual) && "ALL".equals(expect)) {
            return " selected=\"selected\"";
        }
        return expect.equals(actual) ? " selected=\"selected\"" : "";
    }

}
