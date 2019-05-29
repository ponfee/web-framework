package cn.ponfee.web.framework.controller;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import cn.ponfee.web.framework.HttpConstants;
import cn.ponfee.web.framework.model.Role;

import code.ponfee.commons.http.ContentType;
import code.ponfee.commons.http.Http;
import code.ponfee.commons.json.Jsons;

/**
 * @author Ponfee
 */
public class RoleControllerTest {

    @Test(timeout = 999999999)
    public void add() {
        Role role = new Role();
        role.setRoleCode("TEST");
        role.setRoleName("roleName");
        role.setStatus(1);
        role.setPermitIds(Lists.newArrayList("1000","3000","2000"));
        System.out.println(Jsons.toJson(role));
        Http http = Http.post(HttpConstants.URL + "/role/add")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(role))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void update() {
        Role role = new Role();
        role.setId(17L);
        role.setRoleCode("TEST1");
        role.setRoleName("roleName");
        role.setStatus(0);
        role.setPermitIds(Lists.newArrayList("5000","6000","2000"));
        System.out.println(Jsons.toJson(role));
        
        Http http = Http.post(HttpConstants.URL + "/role/update")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(role))
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void deleteroleid() {
        Map<String, Object> map = ImmutableMap.of("roleId","17");
        Http http = Http.post(HttpConstants.URL + "/role/delete/roleid")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(map))
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void deleterolecode() {
        Map<String, Object> map = ImmutableMap.of("roleCode","TEST1");
        Http http = Http.post(HttpConstants.URL + "/role/delete/rolecode")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(map))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void updatepermits() {
        Map<String, Object> params = ImmutableMap.of("roleId", "17","permitIds", new String[] {"3000","7000"});
        System.out.println(Jsons.toJson(params));
        Http http = Http.post(HttpConstants.URL + "/role/updatepermits")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(params))
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void permits() {
        Http http = Http.get(HttpConstants.URL + "/role/permits")
            .addHeader(HttpConstants.COOKIE)
            .addParam("roleId", "19")
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void getroleid() {
        Http http = Http.get(HttpConstants.URL + "/role/get/roleid")
            .addHeader(HttpConstants.COOKIE)
            .addParam("roleId", "19")
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void getrolecode() {
        Http http = Http.get(HttpConstants.URL + "/role/get/rolecode")
            .addHeader(HttpConstants.COOKIE)
            .addParam("roleCode", "TEST")
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void query4page() {
        Http http = Http.get(HttpConstants.URL + "/role/query4page")
            .addHeader(HttpConstants.COOKIE)
            .addParam("pageNum", 1)
            .addParam("pageSize", 3)
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void listall() {
        Http http = Http.get(HttpConstants.URL + "/role/listall")
            .addHeader(HttpConstants.COOKIE)
            ;
        print(http);
    }
    public static void print(Http http) {
        System.out.println(http.request());
        System.out.println(http.getRespHeaders());
    }
}
