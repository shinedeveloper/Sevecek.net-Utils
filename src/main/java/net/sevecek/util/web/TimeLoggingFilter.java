package net.sevecek.util.web;

import java.io.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.Filter;

public class TimeLoggingFilter implements Filter {

    private static final Logger logger = Logger.getLogger(TimeLoggingFilter.class.getName());

    @Override
    public void init(FilterConfig config) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        long millisStart = System.currentTimeMillis();
        chain.doFilter(req, resp);
        long millisEnd = System.currentTimeMillis();
        logger.info("Request took: " + (millisEnd - millisStart));
    }


    @Override
    public void destroy() {
    }

}
