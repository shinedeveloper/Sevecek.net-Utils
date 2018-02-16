package net.sevecek.util.web;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AcceptHeaderAccordingToUrlExtensionFilter implements Filter {

    private FilterConfig filterConfig;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            request = new HttpServletRequestWrapper((HttpServletRequest) request) {
                @Override
                public String getHeader(String name) {
                    HttpServletRequest request1 = (HttpServletRequest) getRequest();
                    if (!name.equalsIgnoreCase("Accept")) {
                        return request1.getHeader(name);
                    }
                    String pathInfo = request1.getPathInfo();
                    if (pathInfo.endsWith(".xml")) {
                        return "application/xml";
                    }
                    if (pathInfo.endsWith(".json")) {
                        return "application/json";
                    }
                    return request1.getHeader(name);
                }


                @Override
                public Enumeration<String> getHeaders(String name) {
                    HttpServletRequest request1 = (HttpServletRequest) getRequest();
                    if (!name.equalsIgnoreCase("Accept")) {
                        return request1.getHeaders(name);
                    }
                    String pathInfo = request1.getPathInfo();
                    if (pathInfo.endsWith(".xml")) {
                        return new SingleStringEnumeration("application/xml");
                    }
                    if (pathInfo.endsWith(".json")) {
                        return new SingleStringEnumeration("application/json");
                    }
                    return request1.getHeaders("Accept");
                }
            };
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }


    private static class SingleStringEnumeration implements Enumeration<String> {

        int currentPosition = 0;
        private String singleValue;


        public SingleStringEnumeration(String singleValue) {
            this.singleValue = singleValue;
        }


        @Override
        public boolean hasMoreElements() {
            return currentPosition == 0;
        }


        @Override
        public String nextElement() {
            currentPosition = 1;
            return singleValue;
        }
    }
}
