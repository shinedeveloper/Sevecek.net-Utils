package net.sevecek.console;

import java.io.*;
import java.util.*;

public class CustomPrintStream extends PrintStream {

    public CustomPrintStream(OutputStream original, String charsetName) throws UnsupportedEncodingException {
        super(original, true, charsetName);
    }

    public PrintStream getOriginalOutputStream() {
        return (PrintStream) out;
    }

}
