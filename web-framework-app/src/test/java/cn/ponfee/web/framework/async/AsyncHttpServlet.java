package cn.ponfee.web.framework.async;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet异步处理
 * 
 * @author Ponfee
 */
@WebServlet(urlPatterns = { "/demoAsyncLink", "/demoAsyncLink2" }, asyncSupported = true)
public class AsyncHttpServlet extends HttpServlet {
    private static final long serialVersionUID = 4617227991063927036L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setHeader("Cache-Control", "private");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Connection", "Keep-Alive");
        response.setHeader("Proxy-Connection", "Keep-Alive");
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<div>Start ...</div>");
        out.flush();

        AsyncContext asyncContext = request.startAsync(request, response);

        new CounterThread(asyncContext).start();
    }

    private static class CounterThread extends Thread {
        private AsyncContext asyncContext;

        public CounterThread(AsyncContext asyncContext) {
            this.asyncContext = asyncContext;
        }

        @Override
        public void run() {
            int num = 0;
            int max = 100;
            int interval = 1000;

            // 必须设置过期时间，否则将会出连接过期，线程无法运行完毕异常
            asyncContext.setTimeout((max + 1) * interval);
            PrintWriter out = null;

            try {
                try {
                    out = asyncContext.getResponse().getWriter();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (true) {
                    out.println("<div>" + (num++) + "</div>");
                    out.flush();

                    if (num >= max) {
                        break;
                    }

                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (out != null) {
                out.println("<div>Done !</div>");
                out.flush();
                out.close();
            }

            asyncContext.complete();
        }
    }
}
