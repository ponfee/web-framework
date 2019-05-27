package cn.ponfee.web.framework.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * http://localhost:8000/tpl/hello
 * http://localhost:8000/tpl/hello.ftl
 * http://localhost:8000/tpl/hello.vm
 */
@Controller
@RequestMapping("/tpl")
public class TplController {

    /**
     * hello      ->  jsp
     * hello.ftl  ->  freemarker
     * hello.vm   ->  velocity
     * @param suffix
     * @param model
     * @return
     */
    //@RequestMapping(path = "/hello{suffix:(\\.[a-zA-Z0-9]+)?}")
    //@RequestMapping(path = "/hello{suffix:\\.[a-zA-Z0-9]+}")
    @RequestMapping(path = "/hello{suffix:.*}")
    public String hello(@PathVariable("suffix") String suffix, Model model) {
        model.addAttribute("title", "欢迎");
        model.addAttribute("content", "光临");
        return "hello" + suffix;
    }

    @RequestMapping(path = "/test")
    public String test(Model model) {
        model.addAttribute("title", "欢迎");
        model.addAttribute("content", "光临");
        return "hello";
    }
}
