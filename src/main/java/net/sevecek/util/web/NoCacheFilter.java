package net.sevecek.util.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (resp instanceof HttpServletResponse) {
            HttpServletResponse response = (HttpServletResponse) resp;
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "must-revalidate");
            response.addHeader("Expires", "Mon, 1 Jan 1970 10:00:00 GMT");
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing
    }

    @Override
    public void destroy() {
        // Nothing
    }

}
