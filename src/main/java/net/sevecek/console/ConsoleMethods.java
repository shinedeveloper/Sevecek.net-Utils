package net.sevecek.console;

import java.io.PrintWriter;


interface ConsoleMethods {

    boolean readBoolean();
    int readInt();
    long readLong();
    double readDouble();
    char readChar();
    int readCodePoint();
    String readWord();
    String readLine();
    String readLine(String message, Object... args);
    char[] readPassword();
    char[] readPassword(String message, Object... args);

    //-------------------------------------------------------------

    void print(boolean b);
    void print(int i);
    void print(long l);
    void print(double d);
    void print(char c);
    void print(String text);
    void print(Object x);
    void println();
    void println(boolean b);
    void println(int i);
    void println(long l);
    void println(double d);
    void println(char c);
    void println(String text);
    void println(Object x);
    void printf(String format, Object... args);
    void printfln(String format, Object... args);

    void setInputCharset(String charsetName);
    String getInputCharset();

    String getOutputCharset();

    void setOutputCharset(String charsetName);
}
