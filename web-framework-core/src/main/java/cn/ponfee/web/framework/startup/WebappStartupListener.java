//package cn.ponfee.web.framework.startup;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//
///**
// * web容器启动后执行
// * 
// * @author Ponfee
// */
//@WebListener
//public class WebappStartupListener implements ServletContextListener {
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println(sce.getServletContext().getAttribute("javax.servlet.context.tempdir"));
//        System.out.println("*******************WebappStartupListener init*******************");
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//
//    }
//
//}
