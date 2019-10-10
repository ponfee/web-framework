package cn.ponfee.web.framework.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private @Value("${home.page:/page/index.html}") String homePage;

    @RequestMapping("/")
    public String home() {
        return "redirect:" + homePage;
    }

}
