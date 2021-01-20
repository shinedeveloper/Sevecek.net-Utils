package net.sevecek.console;

import java.io.IOException;
import java.io.PrintWriter;

class ConsoleMethodsJavaIOConsole extends AbstractConsoleMethods implements ConsoleMethods {

    @Override
    protected int readPhysicalChar() throws IOException {
        return System.console().reader().read();
    }


    @Override
    public String readLine() {
        return System.console().readLine();
    }


    @Override
    public String readLine(String message, Object... args) {
        return System.console().readLine(message, args);
    }


    @Override
    public char[] readPassword() {
        return System.console().readPassword();
    }


    @Override
    public char[] readPassword(String message, Object... args) {
        return System.console().readPassword(message, args);
    }

    //-------------------------------------------------------------------

    @Override
    public void print(char c) {
        PrintWriter writer = System.console().writer();
        writer.print(c);
        writer.flush();
    }


    @Override
    public void print(String text) {
        PrintWriter writer = System.console().writer();
        writer.print(text);
        writer.flush();
    }


    @Override
    public void println() {
        PrintWriter writer = System.console().writer();
        writer.println();
        writer.flush();
    }


    @Override
    public void println(char c) {
        PrintWriter writer = System.console().writer();
        writer.println(c);
        writer.flush();
    }


    @Override
    public void println(String text) {
        PrintWriter writer = System.console().writer();
        writer.println(text);
        writer.flush();
    }


    @Override
    public void printf(String format, Object... args) {
        System.console().printf(format, args).flush();
    }


    @Override
    public String getInputCharset() {
        return null;
    }


    @Override
    public void setInputCharset(String charsetName) {
    }


    @Override
    public String getOutputCharset() {
        return null;
    }


    @Override
    public void setOutputCharset(String charsetName) {
    }
}
