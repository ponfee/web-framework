package cn.ponfee.web.framework;

import static cn.ponfee.web.framework.util.FreeMarkerTemplateUtils.load4dir;
import static cn.ponfee.web.framework.util.FreeMarkerTemplateUtils.load4source;
import static cn.ponfee.web.framework.util.FreeMarkerTemplateUtils.load4url;
import static cn.ponfee.web.framework.util.FreeMarkerTemplateUtils.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;

import cn.ponfee.web.framework.model.test.Person;
import code.ponfee.commons.util.MavenProjects;

public class HelloFtlTester {

    @Test
    public void test1() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "world");
        String dir = MavenProjects.getTestResourcesPath("/ftl");
        print(load4dir(dir, "hello.ftl"), data, System.out);
    }
    
    @Test
    public void testMethod() {
        Map<String, Object> data = new HashMap<>();
        data.put("hello", "world");
        data.put("person", new Person("tom", 20));
        String dir = MavenProjects.getTestResourcesPath("/ftl");
        print(load4dir(dir, "method.ftl"), data, System.out);
    }
    
    @Test
    public void testMacro() {
        Map<String, Object> data = new HashMap<>();
        data.put("hello", "world");
        data.put("person", new Person("tom", 20));
        String dir = MavenProjects.getTestResourcesPath("/ftl");
        print(load4dir(dir, "macro.ftl"), data, System.out);
    }
    
    @Test
    public void testMode() {
        String dir = MavenProjects.getTestResourcesPath("/ftl");
        //new DefaultObjectWrapperBuilder(incompatibleImprovements).
        print(load4dir(dir, "macro.ftl"),  new Person("tom", 20), System.out);
    }
    
    @Test
    public void testSource() {
        Map<String, Object> data = new HashMap<>();
        data.put("hello", "world");
        data.put("person", new Person("tom", 20));
        String path = MavenProjects.getTestResourcesPath("ftl/macro.ftl");
        String source = readFileString(path);
        print(load4source(source),data, System.out);
        print(load4source(source),data, System.out);
        print(load4source(source),data, System.out);
        print(load4source(source),data, System.out);
        print(load4source(source),data, System.out);
        print(load4source(source),data, System.out);
    }
    
    @Test
    public void testUrl() {
        Map<String, Object> data = new HashMap<>();
        data.put("hello", "world");
        data.put("person", new Person("tom", 20));
        String url = "file:///"+MavenProjects.getTestResourcesPath("ftl/macro.ftl");
        url = "http://localhost:8080/test_web2/macro.ftl";
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
        print(load4url(url),data, System.out);
    }
    
    public static String readFileString(String path) {
        try {
            StringBuilder builder = new StringBuilder();
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\r\n");
            }
            scanner.close();
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
