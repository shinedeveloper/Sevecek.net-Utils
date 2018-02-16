package net.sevecek.console;

import java.io.*;
import java.lang.ref.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.text.*;

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
            // detectIDESystemInEncoding();
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


    /*
     * Ugly hack to detect original operating system encoding
     * in all versions of NetBeans
     * and IntelliJ IDEA versions 10.5 or less.
     * Known to work on Sun/Oracle JDK 6, 7, 8.
     * How about OpenJDK? IBM? Excelsior JET? JRockit?
     */
    private void detectIDESystemInEncoding() {
        InputStream currentSystemIn = System.in;
        if (currentSystemIn instanceof BufferedInputStream) {
            try {
                InputStream systemIn = (InputStream) readPrivateField(currentSystemIn, "in");

                if (systemIn instanceof FileInputStream) {
                    FileDescriptor fileDescriptor = ((FileInputStream) systemIn).getFD();
                    readPrivateField(fileDescriptor, "fd");
                    Field fdField = fileDescriptor.getClass().getDeclaredField("fd");
                    boolean originalAccessibleFd = fdField.isAccessible();
                    fdField.setAccessible(true);
                    int fileDescriptorInt;
                    try {
                        fileDescriptorInt = fdField.getInt(fileDescriptor);
                    } finally {
                        fdField.setAccessible(originalAccessibleFd);
                    }
                    if (fileDescriptorInt == -1) {
                        // We are using piped input stream.
                        // Override the default encoding (UTF-8) to operating system default (Win1250)
                        String originalEncoding = System.getProperty("sun.jnu.encoding");
                        if (originalEncoding != null && !originalEncoding.isEmpty()) {
                            systemInCharset = Charset.forName(originalEncoding);
                        }
                    }
                }
            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) {
            } catch (IOException e) {
            }
        }
    }


    private synchronized BufferedReader getBufferedReaderForSystemIn() {
        InputStream currentSystemIn = System.in;
        if (systemInReader == null || systemInForWhichWeHaveReader == null
                || systemInForWhichWeHaveReader.get() != currentSystemIn) {
            systemInForWhichWeHaveReader = new WeakReference<InputStream>(currentSystemIn);
            systemInReader = new BufferedReader(new InputStreamReader(currentSystemIn, systemInCharset));
        }
        return systemInReader;
    }

}
