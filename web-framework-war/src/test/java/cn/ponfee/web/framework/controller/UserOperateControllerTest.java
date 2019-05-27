package cn.ponfee.web.framework.controller;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import cn.ponfee.web.framework.HttpConstants;
import cn.ponfee.web.framework.model.User;

import code.ponfee.commons.http.ContentType;
import code.ponfee.commons.http.Http;
import code.ponfee.commons.json.Jsons;

/**
 * @author Ponfee
 */
public class UserOperateControllerTest {

    @Test(timeout = 999999999)
    public void modifyInfo() {
        User user = new User();
        //user.setPassword("admin123");
        //user.setMobilePhone("12345687925");
        user.setNickname("管理员");
        Http http = Http.post(HttpConstants.URL + "/user/ops/modifyinfo")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(user))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void modifyname() {
        Map<String, Object> map = ImmutableMap.of("nickname","管理员");
        Http http = Http.post(HttpConstants.URL + "/user/ops/modifyname")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(map))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void modifyphone() {
        Map<String, Object> map = ImmutableMap.of("mobilePhone","13418467598");
        Http http = Http.post(HttpConstants.URL + "/user/ops/modifyphone")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(map))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void modifypwd() {
        Map<String, Object> map = ImmutableMap.of("password","admin123");
        Http http = Http.post(HttpConstants.URL + "/user/ops/modifypwd")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(map))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void myaddr() {
        Http http = Http.get(HttpConstants.URL + "/user/ops/myaddr")
            .addHeader(HttpConstants.COOKIE)
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void myrole() {
        Http http = Http.get(HttpConstants.URL + "/user/ops/myrole")
            .addHeader(HttpConstants.COOKIE)
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void myinfo() {
        Http http = Http.get(HttpConstants.URL + "/user/ops/myinfo")
            .addHeader(HttpConstants.COOKIE)
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void mypermits() {
        Http http = Http.get(HttpConstants.URL + "/user/ops/mypermits")
            .addHeader(HttpConstants.COOKIE)
            ;
        print(http);
    }

    public static void print(Http http) {
        System.out.println(http.request());
        System.out.println(http.getRespHeaders());
    }
}
