package net.sevecek.console;

import java.io.*;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

class ConsoleMethodsSystemInOut extends AbstractConsoleMethods implements ConsoleMethods {

    private Charset systemInCharset;
    private Charset systemOutCharset;
    private BufferedReader systemInReader = null;
    private WeakReference<InputStream> systemInForWhichWeHaveReader = null;


    @Override
    protected int readPhysicalChar() throws IOException {
        return getBufferedReaderForSystemIn().read();
    }


    @Override
    public String readLine() {
        try {
            return getBufferedReaderForSystemIn().readLine();
        } catch (IOException e) {
            throw translateException(e);
        }
    }


    @Override
    public String readLine(String message, Object... args) {
        printf(message, args);
        return readLine();
    }


    @Override
    public char[] readPassword() {
        return readLine().toCharArray();
    }


    @Override
    public char[] readPassword(String message, Object... args) {
        printf(message, args);
        return readPassword();
    }

    //-------------------------------------------------------------------


    @Override
    public void print(char c) {
        System.out.print(c);
    }


    @Override
    public void print(String text) {
        System.out.print(text);
    }


    @Override
    public void println() {
        System.out.println();
    }


    @Override
    public void println(char c) {
        System.out.println(c);
    }


    @Override
    public void println(String text) {
        System.out.println(text);
    }


    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }


    @Override
    public synchronized String getInputCharset() {
        return systemInCharset.name();
    }


    @Override
    public synchronized void setInputCharset(String charsetName) {
        if (charsetName != null) {
            systemInCharset = Charset.forName(charsetName);
        } else {
            systemInCharset = Charset.defaultCharset();
        }
        systemInReader = null;
    }


    @Override
    public synchronized String getOutputCharset() {
        return systemOutCharset.name();
    }


    @Override
    public synchronized void setOutputCharset(String charsetName) {
        try {
            if (charsetName != null) {
                systemOutCharset = Charset.forName(charsetName);
            } else {
                systemOutCharset = Charset.defaultCharset();
            }
            OutputStream originalSystemOut = System.out;
            if (originalSystemOut instanceof CustomPrintStream) {
                originalSystemOut = ((CustomPrintStream) originalSystemOut).getOriginalOutputStream();
            }
            System.setOut(new CustomPrintStream(originalSystemOut, systemOutCharset.name()));
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedCharsetException(charsetName);
        }
    }


    @SuppressWarnings("ObjectEquality")
    private synchronized BufferedReader getBufferedReaderForSystemIn() {
        InputStream currentSystemIn = System.in;
        if (systemInReader == null || systemInForWhichWeHaveReader == null
                || systemInForWhichWeHaveReader.get() != currentSystemIn) {
            systemInForWhichWeHaveReader = new WeakReference<>(currentSystemIn);
            if (systemInCharset != null) {
                systemInReader = new BufferedReader(new InputStreamReader(currentSystemIn, systemInCharset));
            } else {
                systemInReader = new BufferedReader(new InputStreamReader(currentSystemIn));
            }
        }
        return systemInReader;
    }
}
