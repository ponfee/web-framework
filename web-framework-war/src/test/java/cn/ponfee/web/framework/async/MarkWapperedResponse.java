package cn.ponfee.web.framework.async;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class MarkWapperedResponse extends HttpServletResponseWrapper {
    private PrintWriter writer = null;
    private static final String MARKED_STRING = "<!--marked filter--->";

    public MarkWapperedResponse(HttpServletResponse resp) throws IOException {
        super(resp);
        writer = new MarkPrintWriter(super.getOutputStream());
    }

    @Override
    public PrintWriter getWriter() throws UnsupportedEncodingException {
        return writer;
    }

    private static class MarkPrintWriter extends PrintWriter {
        public MarkPrintWriter(OutputStream out) {
            super(out);
        }

        @Override
        public void flush() {
            super.flush();
            super.println(MARKED_STRING);
        }
    }
}
