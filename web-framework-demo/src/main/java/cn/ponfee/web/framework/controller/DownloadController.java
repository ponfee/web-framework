package cn.ponfee.web.framework.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import code.ponfee.commons.http.ContentType;
import code.ponfee.commons.util.Captchas;
import code.ponfee.commons.web.WebUtils;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @RequestMapping("/file")
    public void normal(HttpServletResponse resp, @RequestParam(value = "isGzip", defaultValue = "false") boolean isGzip)
        throws FileNotFoundException {
        WebUtils.response(resp, new FileInputStream("d:/pushlet-userunicast.jar"), "长连接包.zip", "UTF-8", isGzip);
    }

    @RequestMapping("/image")
    public void image(HttpServletResponse resp, @RequestParam(value = "isGzip", defaultValue = "false") boolean isGzip) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Captchas.generate(80, out, "123456");
        WebUtils.response(resp, out.toByteArray(), ContentType.IMAGE_JPEG.value(), false);
    }
}
