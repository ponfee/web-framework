package cn.ponfee.web.framework.controller;

import org.junit.Test;

import cn.ponfee.web.framework.HttpConstants;
import cn.ponfee.web.framework.model.Permit;

import code.ponfee.commons.http.ContentType;
import code.ponfee.commons.http.Http;
import code.ponfee.commons.json.Jsons;

/**
 * @author Ponfee
 */
public class PermitControllerTest {

    @Test(timeout = 999999999)
    public void add() {
        Permit permit = new Permit();
        permit.setPermitId("5000");
        permit.setParentId("parentId");
        permit.setPermitName("permitName");
        permit.setPermitType(1);
        permit.setPermitUrl("permitUrl");
        permit.setPermitIcon("permitIcon");
        permit.setOrders(1);
        permit.setStatus(1);
        System.out.println(Jsons.toJson(permit));
        Http http = Http.post(HttpConstants.URL + "/permit/add")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(permit))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void update() {
        Permit permit = new Permit();
        permit.setPermitId("5000");
        permit.setParentId("parentId");
        permit.setPermitName("permitName1");
        permit.setPermitType(1);
        permit.setPermitUrl("permitUrl1");
        permit.setPermitIcon("permitIcon1");
        permit.setOrders(2);
        permit.setStatus(0);
        System.out.println(Jsons.toJson(permit));
        
        Http http = Http.post(HttpConstants.URL + "/permit/update")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(permit))
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void delete() {
        String[] permits = new String[] {"5000","6000"};
        System.out.println(Jsons.toJson(permits));
        Http http = Http.post(HttpConstants.URL + "/permit/delete")
            .addHeader(HttpConstants.COOKIE)
            .contentType(ContentType.APPLICATION_JSON)
            .data(Jsons.toJson(permits))
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void get() {
        Http http = Http.get(HttpConstants.URL + "/permit/get")
            .addHeader(HttpConstants.COOKIE)
            .addParam("permitId", "4000")
            ;
        print(http);
    }

    @Test(timeout = 999999999)
    public void listAsTree() {
        Http http = Http.get(HttpConstants.URL + "/permit/list/tree")
            .addHeader(HttpConstants.COOKIE)
            ;
        print(http);
    }
    
    @Test(timeout = 999999999)
    public void listAsFlat() {
        Http http = Http.get(HttpConstants.URL + "/permit/list/flat")
            .addHeader(HttpConstants.COOKIE)
            ;
        print(http);
    }

    public static void print(Http http) {
        System.out.println(http.getRespHeaders());
        System.out.println(http.request());
    }
}
