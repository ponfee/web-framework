package cn.ponfee.web.framework.controller;

import java.io.File;
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
public class UserManageControllerTest {

    // -----------------------------------------------------管理员
    @Test(timeout = 999999999)
    public void page() {
        Http http = Http.get(HttpConstants.URL + "/user/mg/page")
            .addHeader(HttpConstants.COOKIE)
            //.addParam("nickname", "nickname")
            //.addParam("status", "0")
            .addParam("pageNum", 1)
            .addParam("pageSize", 20)
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void add() {
        User user = new User();
        user.setUsername("username777");
        user.setMobilePhone("13418467597");
        user.setNickname("nickname");
        System.out.println(Jsons.toJson(user));
        Http http = Http.post(HttpConstants.URL + "/user/mg/add")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(user))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void update() {
        User user = new User();
        user.setId(24L);
        user.setPassword("password1234");
        user.setMobilePhone("13418467598");
        user.setNickname("nickname2");
        System.out.println(Jsons.toJson(user));
        
        Http http = Http.post(HttpConstants.URL + "/user/mg/update")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(user))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void updatepwd() {
        Map<String, Object> params = ImmutableMap.of("userId", "21","password", "abcd1234");
        System.out.println(Jsons.toJson(params));
        Http http = Http.post(HttpConstants.URL + "/user/mg/updatepwd")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(params))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void changestatus() {
        Map<String, Object> params = ImmutableMap.of("userId", "21", "status", "0");
        System.out.println(Jsons.toJson(params));
        Http http = Http.post(HttpConstants.URL + "/user/mg/changestatus")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(params))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void delete() {
        long[] userIds = new long[] {13,16};
        System.out.println(Jsons.toJson(userIds));
        Http http = Http.post(HttpConstants.URL + "/user/mg/delete")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(userIds))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void updaterole() {
        Map<String, Object> params = ImmutableMap.of("userId", "21", "roleId", "1");
        System.out.println(Jsons.toJson(params));
        Http http = Http.post(HttpConstants.URL + "/user/mg/updaterole")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(params))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void userrole() {
        Http http = Http.get(HttpConstants.URL + "/user/mg/userrole")
            .addHeader(HttpConstants.COOKIE)
            .addParam("userId", "17")
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void importfile() {
        Http http = Http.get(HttpConstants.URL + "/user/mg/import")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON, "UTF-8")
            .addParam("orgTypeId", "1")
            //.addPart("file", "user-import.xlsx", new File("d:/用户批量导入模板.xlsx"))
            .addPart("file", "d:/test.csv", new File("d:/test.csv"))
            ;
        print(http);
    }

    public static void print(Http http) {
        System.out.println(http.request());
        System.out.println(http.getRespHeaders());
    }
}
