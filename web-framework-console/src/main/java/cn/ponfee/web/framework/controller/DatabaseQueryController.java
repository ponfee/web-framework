package cn.ponfee.web.framework.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Throwables;

import cn.ponfee.web.framework.service.DatabaseQueryService;
import code.ponfee.commons.collect.Collects;
import code.ponfee.commons.data.lookup.MultipleDataSourceContext;
import code.ponfee.commons.data.lookup.MultipletCachedDataSource;
import code.ponfee.commons.export.HtmlExporter;
import code.ponfee.commons.export.Table;
import code.ponfee.commons.export.Thead;
import code.ponfee.commons.export.Tmeta;
import code.ponfee.commons.export.Tmeta.Align;
import code.ponfee.commons.export.Tmeta.Type;
import code.ponfee.commons.http.ContentType;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.PaginationHtmlBuilder;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.tree.BaseNode;
import code.ponfee.commons.util.SpringContextHolder;
import code.ponfee.commons.web.WebUtils;

/**
 * Database query http api
 * 
 * http://localhost:8100/database/query/view
 *   default  : select * from t_user
 *   secondary: select * from t_word_count
 * 
 * @author Ponfee
 */
@RequestMapping("database/query")
@RestController
public class DatabaseQueryController implements InitializingBean {

    private @Resource DatabaseQueryService service;

    
    @GetMapping("page")
    public Result<Page<Object[]>> query4page(PageRequestParams params) {
        return Result.success(
            service.query4page(params).map(Collects::map2array)
        );
    }

    // Oracle: select table_name from tabs
    //  Mysql: select table_name from INFORMATION_SCHEMA.TABLES
    @GetMapping("view")
    public void query4view(PageRequestParams params, HttpServletRequest req, HttpServletResponse resp) {
        Page<LinkedHashMap<String, Object>> page;
        String errorMsg = null;
        try {
            page = service.query4page(params);
        } catch (Throwable t) {
            page = Page.empty();
            errorMsg = Throwables.getRootCause(t).getMessage(); // Throwables.getStackTraceAsString(t);
        }
        Stream<String> head = CollectionUtils.isEmpty(page.getRows()) 
                            ? Arrays.stream(new String[] { "无查询结果" })
                            : page.getRows().get(0).keySet().stream();
        AtomicInteger id = new AtomicInteger(1);
        List<BaseNode<Integer, Thead>> heads = head.map(h -> {
            Tmeta tmeta = new Tmeta(Type.CHAR, null, Align.LEFT, true, null);
            return new BaseNode<>(id.getAndIncrement(), 0, new Thead(h, tmeta, null));
        }).collect(Collectors.toList());

        Table<Object[]> table = new Table<>(heads);
        table.setComment(errorMsg);
        page.forEach(row -> table.addRow(Collects.map2array(row)));
        table.toEnd();

        try (HtmlExporter exporter = new HtmlExporter()) {
            exporter.build(table);
            PaginationHtmlBuilder builder = PaginationHtmlBuilder.newBuilder(
                "Database Query", WebUtils.getContextPath(req) + "/database/query/view", page
            );
            builder.table(exporter.body())
                   .scripts(PaginationHtmlBuilder.CDN_JQUERY)
                   .form(buildForm(params))
                   .params(params);

            WebUtils.response(resp, ContentType.TEXT_HTML, 
                              builder.build(), StandardCharsets.UTF_8);
        } 
    }

    private String buildForm(PageRequestParams params) {
        StringBuilder builder = new StringBuilder(2048)
            .append("<select name=\"datasource\">\n");
        for (String datasource : MultipleDataSourceContext.listDataSourceNames()) {
            builder.append("<option value=\"").append(datasource).append("\"")
                   .append(datasource(params.getString("datasource"), datasource))
                   .append(">").append(datasource).append("</option>\n");
        }
        return builder.append("</select>\n")
            .append("<input type=\"submit\" value=\"search\"/><br/>\n")
            .append("<textarea name=\"sql\" rows=\"8\" cols=\"100\">")
            .append(params.getString("sql")).append("</textarea>")
            .toString();
    }

    private String datasource(String actual, String expect) {
        return expect.equals(actual) ? " selected=\"selected\"" : "";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        datasource.setUrl("");
        datasource.setUsername("");
        datasource.setPassword("");
        // 自动重连connectionErrorRetryAttempts次后：如果breakAfterAcquireFailure=true，则中断，否则循环往复再次重连connectionErrorRetryAttempts次 
        datasource.setConnectionErrorRetryAttempts(0); // 自动重连次数
        datasource.setBreakAfterAcquireFailure(true); // 是否中断重新

        MultipletCachedDataSource mcds = SpringContextHolder.getBean(MultipletCachedDataSource.class);
        mcds.addIfAbsent("test-dynamic-add", datasource, System.currentTimeMillis() + 12000000);
    }

}
