package net.sevecek.jdbclogging;

import java.io.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.sql.*;

public class LogDataSource implements DataSource {

    private DataSource original;
    private String user;
    private String password;
    private String url;


    public LogDataSource(DataSource original) {
        this.original = original;
    }


    public LogDataSource() {
    }


    public void setDataSourceClassName(String className) {
        try {
            original = (DataSource) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUser() {
        return user;
    }


    public void setUser(String user) {
        this.user = user;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public Connection getConnection() throws SQLException {
        Connection orig = original.getConnection();
        Connection proxy = (Connection) Proxy.newProxyInstance(
                orig.getClass().getClassLoader(),
                original.getClass().getInterfaces(),
                new LogInvocationHandler(orig));
        return proxy;
    }


    public Connection getConnection(String username, String password) throws SQLException {
        Connection orig = original.getConnection(username, password);
        Connection proxy = (Connection) Proxy.newProxyInstance(
                orig.getClass().getClassLoader(),
                original.getClass().getInterfaces(),
                new LogInvocationHandler(orig));
        return proxy;
    }


    public PrintWriter getLogWriter() throws SQLException {
        return original.getLogWriter();
    }


    public void setLogWriter(PrintWriter out) throws SQLException {
        original.setLogWriter(out);
    }


    public void setLoginTimeout(int seconds) throws SQLException {
        original.setLoginTimeout(seconds);
    }


    public int getLoginTimeout() throws SQLException {
        return original.getLoginTimeout();
    }


    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return original.getParentLogger();
    }


    public <T> T unwrap(Class<T> iface) throws SQLException {
        return original.unwrap(iface);
    }


    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return original.isWrapperFor(iface);
    }


    private static class LogInvocationHandler implements InvocationHandler {

        private static int totalCount = 0;

        private Connection original;
        private int ordinal;


        public LogInvocationHandler(Connection original) {
            this.ordinal = totalCount++;
            this.original = original;
        }


        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.printf("Connection %d: %s%s%n",
                    ordinal, method.getName(), Arrays.asList(args));
            Object result = method.invoke(original, args);
            return result;
        }
    }
}
