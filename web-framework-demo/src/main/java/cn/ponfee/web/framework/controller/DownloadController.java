package cn.ponfee.web.framework.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import code.ponfee.commons.web.WebUtils;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @RequestMapping("/normal")
    public void normal(HttpServletResponse resp) throws FileNotFoundException {
        WebUtils.response(resp, new FileInputStream("d:/procyon-decompiler-0.5.30.jar"), "test.zip");
    }

    @RequestMapping("/gzip")
    public void gzip(HttpServletResponse resp) throws FileNotFoundException {
        WebUtils.response(resp, new FileInputStream("d:/procyon-decompiler-0.5.30.jar"), "test.zip", "UTF-8", true);
    }
}
