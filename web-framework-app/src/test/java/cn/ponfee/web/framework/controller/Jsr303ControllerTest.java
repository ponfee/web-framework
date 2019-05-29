package cn.ponfee.web.framework.controller;

import org.junit.Test;

import cn.ponfee.web.framework.HttpConstants;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.util.RegexUtils;

/**
 * @author Ponfee
 */
public class Jsr303ControllerTest {

    @Test(timeout = 999999999)
    public void createArticle() {
        Http http = Http.post(HttpConstants.URL + "/test/article")
            .addHeader(HttpConstants.COOKIE)
            .addParam("title", "abcd")
            .addParam("content", "1111111111111")
            .addParam("email", "fupengfe163@163.com")
            .addParam("createDate", "2017-10-10")
            ;
        System.out.println(http.request());
    }

    public static void main(String[] args) {
        System.out.println(RegexUtils.isEmail("fdsafds@153.com"));
    }
    
    
}
