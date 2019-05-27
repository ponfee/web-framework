package cn.ponfee.web.framework;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * 获取cookie：
 * 先用浏览器登录http://localhost:8100/login?username=admin&password=admin123
 * 再访问http://localhost:8100/test1，查看cookie
 * 
 * @author Ponfee
 */
public class HttpConstants {

    public static final String URL = "http://localhost:9999/";
    public static final Map<String, String> COOKIE = ImmutableMap.of(
        "Cookie",
        "auth_token=eyJqdGkiOiJlclk2UUdXWlN3U1d3dVhlRGdpTDRBIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTUzMDY0NTc3NSwicmZoIjoxNTMwMjg1Nzc1MTc4fQ.yHzBjKBRzUFCWWO_cd2F2OGrCqm6m7JYghbABtsukR0"
    );
}
