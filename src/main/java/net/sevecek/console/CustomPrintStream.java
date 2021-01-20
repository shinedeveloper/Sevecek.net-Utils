package net.sevecek.console;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

class CustomPrintStream extends PrintStream {

    public CustomPrintStream(OutputStream original, String charsetName) throws UnsupportedEncodingException {
        super(original, true, charsetName);
    }

    public PrintStream getOriginalOutputStream() {
        return (PrintStream) out;
    }
}
