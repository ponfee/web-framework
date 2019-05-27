package cn.ponfee.web.framework.controller.test;

import code.ponfee.commons.io.Files;
import code.ponfee.commons.web.WebContext;
import com.google.common.collect.ImmutableList;
import cn.ponfee.web.framework.model.test.Article;
import cn.ponfee.web.framework.util.FreeMarkerTemplateUtils;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http://localhost:8000/news/newsList
 */
@Controller
@RequestMapping("/news")
public class NewsController {

    // test data
    private static final List<Article> ARTICLES = ImmutableList.of(
       new Article(20160701, "不明真相的美国人被UFO惊呆了 其实是长征7号", "据美国《洛杉矶时报》报道，当地时间周三晚(北京时间周四)，在美国中西部的犹他州、内华达州、加利福利亚州，数千人被划过夜空的神秘火球吓到"),
       new Article(20160702, "法国巴黎圣母院为教堂恐袭案遇害神父举行大弥撒", "而据美国战略司令部证实，其实这是中国长征七号火箭重新进入大气层，刚好经过加利福利亚附近。"),
       new Article(20160703, "日东京知事候选人小池百合子回击石原：浓妆可以", "然而昨晚的美国人民可不明真相，有些人甚至怀疑这些火球是飞机解体，还有些人猜测是流星雨。"),
       new Article(20160704, "日资慰安妇基金在首尔成立 韩国示威者闯入抗议", "美国战略司令部发言人表示，到目前为止还没有任何受损报告，他说类似物体通常在大气中就会消失，这也解释了为何出现一道道光痕，这一切都并未造成什么威胁。"),
       new Article(20160705, "中日关系正处十字路口日应寻求减少与华冲突", "中国长征七号火箭6月25日在海南文昌航天发射中心首次发射，并成功升空进入轨道。有学者指出长征七号第二级火箭一直在地球低轨运行，一个月后重新进入大气层。")
    );

    public static final String NEWS_LIST = "/static/page/list";

    public static final String NEWS_CONTENT = "/static/page/list/content";

    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 列表页
     * @return
     */
    @RequestMapping("/newsList")
    public String getNewsListPage(Model model) {
        // 模板需要数据
        Map<String, Object> articleData = new HashMap<>();
        articleData.put("articles", ARTICLES);

        // 模板生成静态页目录
        String basePath = WebContext.getRequest().getSession().getServletContext().getRealPath("/");
        String htmlDir = basePath + NEWS_LIST;
        String htmlName = "/newsList.html";
        String htmlPath = htmlDir + htmlName;

        // 生成 静态新闻列表页
        Template template = FreeMarkerTemplateUtils.load4conf(freeMarkerConfigurer.getConfiguration(), "newsList.ftl");
        //模板生成 HTML 内容
        String htmlText = FreeMarkerTemplateUtils.print(template, articleData);
        //生成静态页 列表页
        saveToFile(htmlText, htmlPath);
        // 生成静态内容页
        getNewsContentPage(WebContext.getRequest(), WebContext.getResponse());
        // 跳转至静态页
        return "redirect:../page/list/newsList.html";
    }

    /**
     * 内容页
     * 
     * @param request
     * @param response
     * @return
     */
    private void getNewsContentPage(HttpServletRequest request, HttpServletResponse response) {
        // 模板需要数据
        Map<String, Object> articleData = new HashMap<>();

        // 模板生成静态页目录
        String htmlDir = request.getSession().getServletContext().getRealPath("/") + NEWS_CONTENT;

        //循环生成列表页中的内容页
        for (Article article : ARTICLES) {
            //内容页数据
            articleData.put("article", article);
            //内容页名称
            String htmlName = "/" + article.getId() + ".html";
            //内容页路径
            String htmlPath = htmlDir + htmlName;

            // 获得 模板
            Template template = null;
            // 获得 模板转换的 html字符串
            String htmlText = null;
            try {
                template = freeMarkerConfigurer.getConfiguration().getTemplate("newsContent.ftl");
                htmlText = FreeMarkerTemplateUtils.print(template, articleData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveToFile(htmlText, htmlPath);
            
        }
    }

    private void saveToFile(String text, String htmlPath) {
        File file = Files.touch(htmlPath);
        try (FileOutputStream out = new FileOutputStream(file)) {
            IOUtils.write(text, out, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
