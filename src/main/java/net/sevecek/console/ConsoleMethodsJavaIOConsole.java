package net.sevecek.console;

import java.io.*;
import java.nio.charset.*;

class ConsoleMethodsJavaIOConsole extends AbstractConsoleMethods implements ConsoleMethods {

    @Override
    protected int readPhysicalChar() throws IOException {
        return System.console().reader().read();
    }


    public String readLine() {
        return System.console().readLine();
    }


    public String readLine(String message, Object... args) {
        return System.console().readLine(message, args);
    }


    public char[] readPassword() {
        return System.console().readPassword();
    }


    public char[] readPassword(String message, Object... args) {
        return System.console().readPassword(message, args);
    }

    //-------------------------------------------------------------------

    public void print(char c) {
        PrintWriter writer = System.console().writer();
        writer.print(c);
        writer.flush();
    }


    public void print(String text) {
        PrintWriter writer = System.console().writer();
        writer.print(text);
        writer.flush();
    }


    public void println() {
        PrintWriter writer = System.console().writer();
        writer.println();
        writer.flush();
    }


    public void println(char c) {
        PrintWriter writer = System.console().writer();
        writer.println(c);
        writer.flush();
    }


    public void println(String text) {
        PrintWriter writer = System.console().writer();
        writer.println(text);
        writer.flush();
    }


    public void printf(String format, Object... args) {
        System.console().printf(format, args).flush();
    }


    @Override
    public void setInputCharset(String charsetName) {
    }


    @Override
    public String getInputCharset() {
        try {
            Charset cs = (Charset) readPrivateField(System.console(), "cs");
            return cs.name();
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Unsupported System.console() object", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to inspect Unsupported System.console() object", e);
        }
    }


    @Override
    public String getOutputCharset() {
        return getInputCharset();
    }


    @Override
    public void setOutputCharset(String charsetName) {
    }
}
