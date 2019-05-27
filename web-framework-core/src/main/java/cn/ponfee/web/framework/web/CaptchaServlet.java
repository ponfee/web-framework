package cn.ponfee.web.framework.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.util.concurrent.RateLimiter;

import code.ponfee.commons.collect.Collects;
import code.ponfee.commons.io.Files;
import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.limit.request.RedisRequestLimiter;
import code.ponfee.commons.limit.request.RequestLimiter;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.util.Captchas;
import code.ponfee.commons.util.ObjectUtils;
import code.ponfee.commons.util.SpringContextHolder;
import code.ponfee.commons.web.WebUtils;

/**
 * 验证码
 * 
 * @author Ponfee
 */
@WebServlet(urlPatterns = { "/captcha.json" }, loadOnStartup = 9, asyncSupported = true)
public class CaptchaServlet extends HttpServlet {

    private static final long serialVersionUID = 3982429681371445498L;

    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_PHONE = "phone";
    public static final String TYPE_EMAIL = "email";

    public static final String CAPTCID = "captcid";
    public static final String CAPTCNE = "captcne";
    public static final String CAPTCHA = "captcha";

    public static final String IMAGE_BASE64_PREFIX = "data:image/jpeg;base64,";

    // 限流（5000次/秒）：防止请求量过大导致服务及redis超负载
    private static final RateLimiter IMAGE_LIMITER = RateLimiter.create(5000);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setCharacterEncoding(Files.UTF_8);
        String type = Objects.toString(request.getParameter("type"), "");
        JedisClient jedis = SpringContextHolder.getBean(JedisClient.class);
        RedisRequestLimiter limiter = RedisRequestLimiter.create(jedis);
        String code;
        switch (type) {
            case TYPE_IMAGE:
                Result<?> result;
                if (IMAGE_LIMITER.tryAcquire()) {
                    code = RandomStringUtils.randomAlphanumeric(4);
                    String captcid = ObjectUtils.uuid22();
                    String captcne = RequestLimiter.buildNonce(code, captcid);
                    String captcha = generateImg(code);
                    limiter.cacheCaptcha(captcid, code, 120);
                    result = Result.success(
                        Collects.toMap(CAPTCID, captcid, CAPTCNE, captcne, CAPTCHA, captcha)
                    );
                } else {
                    result = Result.failure(ResultCode.REQUEST_TIMEOUT, "服务繁忙，请稍后再试！");
                }
                WebUtils.respJson(response, result);
                break;
            case TYPE_PHONE:
                code = RandomStringUtils.randomNumeric(6);
                // cache redis and send sms
                WebUtils.respJson(response, Result.SUCCESS);
                break;
            case TYPE_EMAIL:
                code = ObjectUtils.uuid22() + RandomStringUtils.randomAlphanumeric(8);
                // cache redis and send email
                WebUtils.respJson(response, Result.SUCCESS);
                break;
            default:
                break;
        }
    }

    private static String generateImg(String code) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Captchas.generate(80, out, code);
        return IMAGE_BASE64_PREFIX + Base64.getEncoder().encodeToString(out.toByteArray());
    }
}
