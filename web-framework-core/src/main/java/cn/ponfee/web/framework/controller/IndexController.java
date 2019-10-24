package cn.ponfee.web.framework.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private @Value("${home.page:/static/page/index.html}") String homePage;

    @GetMapping("/")
    public String home() {
        return "redirect:" + homePage;
    }

}
