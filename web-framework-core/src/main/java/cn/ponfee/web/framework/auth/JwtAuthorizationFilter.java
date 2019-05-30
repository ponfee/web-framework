package cn.ponfee.web.framework.auth;

import static code.ponfee.commons.model.ResultCode.BAD_REQUEST;
import static code.ponfee.commons.model.ResultCode.FORBIDDEN;
import static code.ponfee.commons.model.ResultCode.NOT_FOUND;
import static code.ponfee.commons.model.ResultCode.OK;
import static code.ponfee.commons.model.ResultCode.REDIRECT;
import static code.ponfee.commons.model.ResultCode.UNAUTHORIZED;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.ImmutableMap;

import cn.ponfee.web.framework.model.User;
import cn.ponfee.web.framework.service.IUserService;
import cn.ponfee.web.framework.util.CommonUtils;
import cn.ponfee.web.framework.util.Constants;
import cn.ponfee.web.framework.web.CaptchaServlet;
import cn.ponfee.web.framework.web.AppContext;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.io.Files;
import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.limit.request.RedisRequestLimiter;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.web.WebUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * The Authorization Filter based Jwt, 
 * include authentication and url permission
 * 
 * @author Ponfee
 */
public class JwtAuthorizationFilter extends AuthorizationFilter {

    //private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private String loginAction; // the server login api
    private String logoutAction; // the server logout api
    private boolean loginWithCaptcha; // login whether use captcha
    private boolean passwordEncrypt; // password whether encrypt
    private String successUrl; // the default location a user is sent after login

    private JwtManager jwtManager;
    private IUserService userService;
    private JedisClient jedisClient;

    /**
     * 预处理
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response)
        throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        WebUtils.cors(req, resp);

        // 跨域时会先发送一个option请求，则直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            resp.setStatus(HttpStatus.OK.value());
            return true;
        }

        String requestURI = super.getPathWithinApplication(request);
        if (StringUtils.equals(this.loginAction, requestURI)) {
            return login(req, resp); // login process
        } else if (StringUtils.equals(this.logoutAction, requestURI)) {
            return logout(req, resp); // logout process
        }

        return super.preHandle(request, response);
    }

    /**
     * 权限认证
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, 
                                      Object mappedValue) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String requestURI = super.getPathWithinApplication(request);

        if (StringUtils.equals(super.getLoginUrl(), requestURI)) {
            return !checkLogined(req, resp);
        }

        /*if (UrlPermissionMatcher.isNotMapping(requestURI)) {
            return true; // is spring mvc controller url mapping, no need login
        }*/

        // verify the jwt for authentication
        Jws<Claims> jws;
        try {
            jws = jwtManager.verify(WebUtils.getCookie(req, WebUtils.AUTH_COOKIE));
        } catch (Exception e) {
            // verify fail: unlogin or jwt expire
            //logger.debug("Verify jwt occur exception.", e);
            String returnUrl = req.getServletPath();
            if (StringUtils.isNotBlank(req.getQueryString())) {
                returnUrl += "?" + req.getQueryString();
            }
            return redirectToLogin(req, resp, returnUrl, REDIRECT, "请登录");
        }

        // verify the permission for authorization
        String username = jws.getBody().getSubject();
        User user = userService.getByUsername(username).getData();
        String fail = (user == null)
                      ? "用户不存在"
                      : user.isDeleted()
                      ? "用户已删除"
                      : user.getStatus() == User.STATUS_DISABLE
                      ? "用户被锁定"
                      : null;
        if (fail != null) {
            return response(req, resp, UNAUTHORIZED, fail, super.getUnauthorizedUrl());
        }

        // check the user has spec url permission
        if (UrlPermissionMatcher.hasNotPermission(requestURI, user.getId())) {
            return response(req, resp, UNAUTHORIZED, super.getUnauthorizedUrl());
        }

        // ---------------------------------------------------------authorization success
        // check whether renew jwt
        String renewJwt = (String) jws.getBody().get(JwtManager.RENEW_JWT);
        if (renewJwt != null) {
            WebUtils.addCookie(
                resp, WebUtils.AUTH_COOKIE, renewJwt, Constants.ROOT_PATH, jwtManager.getJwtExpSeconds()
            );
        }
        AppContext.currentUser(user);
        return true; // authorization success
    }

    /**
     * 权限认证失败后的处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest req, ServletResponse resp) throws IOException {
        //return super.onAccessDenied(req, resp);
        return false; // direct return false
    }

    /**
     * Do login Action
     * 
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    private boolean login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (checkLogined(req, resp)) {
            return false;
        }

        String returnUrl = req.getParameter(Constants.RETURN_URL);

        if (!verifyCaptcha(req, resp)) {
            return redirectToLogin(req, resp, returnUrl, BAD_REQUEST, "验证码错误");
        }

        String username = req.getParameter("username");
        if (StringUtils.isBlank(username)) {
            return redirectToLogin(req, resp, returnUrl, BAD_REQUEST, "用户名不能为空");
        }

        String password = req.getParameter("password");
        if (StringUtils.isBlank(password)) {
            return redirectToLogin(req, resp, returnUrl, BAD_REQUEST, "密码不能为空");
        }

        if (passwordEncrypt) {
            try {
                password = CommonUtils.decryptPassword(password);
            } catch (Exception ignored) {
                return redirectToLogin(req, resp, returnUrl, BAD_REQUEST, "无效的密码");
            }
        }

        // 校验是否登录频繁
        String loginTraceKey = "lg:fl:" + username;
        RedisRequestLimiter limiter = RedisRequestLimiter.create(jedisClient);
        if (limiter.countAction(loginTraceKey) > 5) {
            return redirectToLogin(req, resp, returnUrl, FORBIDDEN, "登录频繁，请稍后再试");
        }

        User user = userService.getByUsername(username).getData();
        if (user == null || !CommonUtils.checkPassword(password, user.getPassword())) {
            if (user != null) {
                limiter.recordAction(loginTraceKey, 120); // 防止密码枚举攻击
            }
            return redirectToLogin(req, resp, returnUrl, NOT_FOUND, "用户名或密码错误");
        }

        String fail = user.isDeleted() ? "用户已删除" : (user.getStatus() == User.STATUS_DISABLE) ? "用户被锁定" : null;
        if (fail != null) {
            return response(req, resp, UNAUTHORIZED, fail, super.getLoginUrl());
        }

        // create a jwt
        String jwt = jwtManager.create(username);

        // revoke the oldness jwt
        jwtManager.revoke(WebUtils.getCookie(req, WebUtils.AUTH_COOKIE));

        // response the header cookie
        WebUtils.addCookie(
            resp, WebUtils.AUTH_COOKIE, jwt, Constants.ROOT_PATH, jwtManager.getJwtExpSeconds()
        );

        // --------------------------------------------------------login success
        // 重置登录失败次数
        limiter.resetAction(loginTraceKey);

        String redirectUrl = Optional.ofNullable(returnUrl).filter(StringUtils::isNotBlank).orElse(successUrl);
        return response(req, resp, OK, redirectUrl); // login success
    }

    private boolean checkLogined(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jwt = WebUtils.getCookie(req, WebUtils.AUTH_COOKIE);
        if (StringUtils.isNotBlank(jwt)) {
            // 判断用户是否已经登录
            try {
                String username = jwtManager.verify(jwt).getBody().getSubject();
                User user = userService.getByUsername(username).getData();
                if (user != null && !user.isDeleted() && user.getStatus() == User.STATUS_ENABLE) {
                    response(req, resp, REDIRECT, "用户已登录，如需切换账户请先退出", successUrl);
                    return true;
                } else {
                    doLogout(req, resp);
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    /**
     * Logout
     * 
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    private boolean logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doLogout(req, resp);
        return response(req, resp, OK, super.getLoginUrl()); // logout success
    }

    private void doLogout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        super.getSubject(req, resp).logout();
        if (session != null) {
            session.invalidate();
        }
        jwtManager.revoke(WebUtils.getCookie(req, WebUtils.AUTH_COOKIE));
        WebUtils.delCookie(req, resp, WebUtils.AUTH_COOKIE);
    }

    /**
     * Verify the login captcha
     * 
     * @param req
     * @param resp
     * @return
     */
    private boolean verifyCaptcha(HttpServletRequest req, HttpServletResponse resp) {
        if (!loginWithCaptcha) {
            return true;
        }

        String captcid = req.getParameter(CaptchaServlet.CAPTCID);
        String captcne = req.getParameter(CaptchaServlet.CAPTCNE);
        String captcha = req.getParameter(CaptchaServlet.CAPTCHA);

        return StringUtils.isNoneBlank(captcid, captcne, captcha)
            && RedisRequestLimiter.verifyNonce(captcne, captcha, captcid)
            && RedisRequestLimiter.create(jedisClient).checkCaptcha(captcid, captcha);
    }

    private boolean redirectToLogin(HttpServletRequest req, HttpServletResponse resp,
                                    String returnUrl, ResultCode code, String msg) throws IOException {
        boolean isRootPath = StringUtils.isBlank(returnUrl) || Constants.ROOT_PATH.equals(returnUrl);
        String redirectUrl = isRootPath ? super.getLoginUrl() : HttpParams.buildUrlPath(
            super.getLoginUrl(), Files.UTF_8, ImmutableMap.of(Constants.RETURN_URL, returnUrl)
        );
        return response(req, resp, code, msg, redirectUrl);
    }

    private boolean response(HttpServletRequest req, HttpServletResponse resp,
                             ResultCode rc, String redirectUrl) throws IOException {
        return response(req, resp, rc, rc.getMsg(), redirectUrl);
    }

    /**
     * Responses to client
     * 
     * @param req  the HttpServletRequest
     * @param resp the HttpServletResponse
     * @param rc   the ResultCode
     * @param msg  the tip message
     * @param redirectUrl the redirect url for client
     * @return because is the final response to client, so direct {@code return false; }
     * @throws IOException if occur IOException at response to client
     */
    private boolean response(HttpServletRequest req, HttpServletResponse resp, 
                             ResultCode rc, String msg, String redirectUrl) throws IOException {
        redirectUrl = org.apache.shiro.web.util.WebUtils.getContextPath(req) + redirectUrl;
        if (WebUtils.isAjax(req)) {
            WebUtils.respJson(resp, new Result<>(rc.getCode(), msg, redirectUrl));
        } else {
            if (redirectUrl.equals(successUrl) || !ResultCode.isSuccessCode(rc.getCode())) {
                redirectUrl = HttpParams.buildUrlPath(redirectUrl, Files.UTF_8, ImmutableMap.of("msg", msg));
            }
            resp.sendRedirect(resp.encodeRedirectURL(redirectUrl));
        }
        return false;
    }

    // --------------------------------------------------------------------------------------------getter/setter
    public void setLoginAction(String loginAction) {
        this.loginAction = loginAction;
    }

    public void setLogoutAction(String logoutAction) {
        this.logoutAction = logoutAction;
    }

    public void setLoginWithCaptcha(boolean loginWithCaptcha) {
        this.loginWithCaptcha = loginWithCaptcha;
    }

    public void setPasswordEncrypt(boolean passwordEncrypt) {
        this.passwordEncrypt = passwordEncrypt;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public void setJwtManager(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setJedisClient(JedisClient jedisClient) {
        this.jedisClient = jedisClient;
    }

}
