package cn.ponfee.web.framework.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * session监听
 * @author fupf
 */
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        SessionContext.addSession(event.getSession());
    }

    /**
     * session销毁前调用的方法
     * @param event
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        SessionContext.removeSession(event.getSession());
    }

    public static class SessionContext {
        private static final Map<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<>();

        private SessionContext() {}

        public static void addSession(HttpSession session) {
            SESSION_MAP.put(session.getId(), session);
        }

        public static void removeSession(HttpSession session) {
            SESSION_MAP.remove(session.getId());
        }

        public static HttpSession getSession(String sessionId) {
            return SESSION_MAP.get(sessionId);
        }
    }
}
