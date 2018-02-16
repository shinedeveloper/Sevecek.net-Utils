package net.sevecek.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;

abstract class AbstractConsoleMethods implements ConsoleMethods {

    protected abstract int readPhysicalChar() throws IOException;

    public abstract String readLine();

    public abstract String readLine(String message, Object... args);

    public abstract char[] readPassword();

    public abstract char[] readPassword(String message, Object... args);


    protected int readPhysicalCodePoint() throws IOException {
        int firstCharOrMinusOne = readPhysicalChar();
        if (firstCharOrMinusOne == -1) {
            return -1;
        }
        char firstChar = (char) firstCharOrMinusOne;
        if (!Character.isHighSurrogate(firstChar) && !Character.isLowSurrogate(firstChar)) {
            return (int) firstChar;
        }
        if (Character.isLowSurrogate(firstChar)) {
            throw new IllegalStateException("Only Unicode low (trailing) surrogate was received but no high surrogate");
        }
        int secondCharOrMinusOne = readPhysicalChar();
        if (secondCharOrMinusOne == -1) {
            throw new IllegalStateException("Only Unicode high (leading) surrogate was received and then the input was closed");
        }
        char secondChar = (char) secondCharOrMinusOne;
        return Character.toCodePoint(firstChar, secondChar);
    }


    @Override
    public char readChar() {
        try {
            int characterOrMinusOne;
            do {
                characterOrMinusOne = readPhysicalChar();
            } while (!isPartOfWord(characterOrMinusOne));
            return (char) characterOrMinusOne;
        } catch (IOException e) {
            throw translateException(e);
        }
    }


    @Override
    public int readCodePoint() {
        try {
            int codePointOrMinusOne;
            do {
                codePointOrMinusOne = readPhysicalCodePoint();
            } while (!isPartOfWord(codePointOrMinusOne));
            return codePointOrMinusOne;
        } catch (IOException e) {
            throw translateException(e);
        }
    }


    @Override
    public String readWord() {
        try {
            StringBuilder builder = new StringBuilder();

            int oneChar = readPhysicalCodePoint();
            while (!isPartOfWord(oneChar)) {
                oneChar = readPhysicalCodePoint();
            }

            while (isPartOfWord(oneChar)) {
                builder.appendCodePoint(oneChar);
                oneChar = readPhysicalCodePoint();
            }

            return builder.toString();
        } catch (IOException e) {
            throw translateException(e);
        }
    }


    protected boolean isPartOfWord(int ch) {
        if (ch == -1) {
            throw new IllegalStateException("TextConsole is already closed");
        }
        return ch > ' ' && !Character.isSpaceChar(ch);
    }


    protected RuntimeException translateException(Exception ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        }
        return new RuntimeException(ex);
    }

    //-----------------------------------------------------------------------

    @Override
    public boolean readBoolean() {
        return Boolean.parseBoolean(readWord());
    }


    @Override
    public int readInt() {
        long number = readLong();
        if (number > Integer.MAX_VALUE) {
            throw new NumberFormatException("Number is too big: " + number + ". Maximum number is " + Integer.MAX_VALUE);
        }
        if (number < Integer.MIN_VALUE) {
            throw new NumberFormatException("Number is too small: " + number + ". Minimum number is " + Integer.MIN_VALUE);
        }
        return (int) number;
    }


    @Override
    public long readLong() {
        String word = readWord();
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        Number number = parseLongUsingNumberFormatWithFallback(word, numberFormat);
        return number.longValue();
    }


    @Override
    public double readDouble() {
        String word = readWord();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        Number number = parseDoubleUsingNumberFormatWithFallback(word, numberFormat);
        return number.doubleValue();
    }


    private Number parseLongUsingNumberFormatWithFallback(String text, NumberFormat numberFormat) {
        Number number = parseNumberUsingNumberFormat(text, numberFormat);
        if (number != null) {
            return number;
        }
        try {
            number = Long.valueOf(text);
            return number;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number '" + text + "'");
        }
    }

    private Number parseDoubleUsingNumberFormatWithFallback(String text, NumberFormat numberFormat) {
        Number number = parseNumberUsingNumberFormat(text, numberFormat);
        if (number != null) {
            return number;
        }
        try {
            number = Double.valueOf(text);
            return number;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number '" + text + "'");
        }
    }

    private Number parseNumberUsingNumberFormat(String text, NumberFormat numberFormat) {
        if (text.length() == 0) {
            throw new NumberFormatException("Invalid empty number");
        }
        ParsePosition parseValidation = new ParsePosition(0);
        Number number = numberFormat.parse(text, parseValidation);
        if (parseValidation.getIndex() == text.length()) {
            return number;
        } else {
            return null;
        }
    }

    //------------------------------------------------------------------------

    public abstract void print(String text);

    public abstract void print(char c);

    public abstract void println();

    public abstract void println(char c);

    public abstract void println(String text);

    public abstract void printf(String format, Object... args);


    @Override
    public void print(boolean b) {
        print(Boolean.toString(b));
    }


    @Override
    public void print(int i) {
        String text = NumberFormat.getIntegerInstance().format((long) i);
        print(text);
    }


    @Override
    public void print(long l) {
        String text = NumberFormat.getIntegerInstance().format(l);
        print(text);
    }


    @Override
    public void print(double d) {
        String text = NumberFormat.getNumberInstance().format(d);
        print(text);
    }


    @Override
    public void print(Object obj) {
        if (obj instanceof Integer || obj instanceof Byte || obj instanceof Short) {
            print(((Number) obj).intValue());
            return;
        }
        if (obj instanceof Long) {
            print(((Long) obj).longValue());
            return;
        }
        if (obj instanceof Double || obj instanceof Float) {
            print(((Number) obj).doubleValue());
            return;
        }
        String text = obj.toString();
        print(text);
    }


    @Override
    public void println(boolean b) {
        println(Boolean.toString(b));
    }


    @Override
    public void println(int i) {
        String text = NumberFormat.getIntegerInstance().format(i);
        println(text);
    }


    @Override
    public void println(long l) {
        String text = NumberFormat.getIntegerInstance().format(l);
        println(text);
    }


    @Override
    public void println(double d) {
        String text = NumberFormat.getNumberInstance().format(d);
        println(text);
    }


    @Override
    public void println(Object obj) {
        if (obj instanceof Integer || obj instanceof Byte || obj instanceof Short) {
            println(((Number) obj).intValue());
            return;
        }
        if (obj instanceof Long) {
            println(((Long) obj).longValue());
            return;
        }
        if (obj instanceof Double || obj instanceof Float) {
            println(((Number) obj).doubleValue());
            return;
        }
        String text = obj.toString();
        println(text);
    }


    @Override
    public void printfln(String format, Object... args) {
        printf(format + "%n", args);
    }

    public Object readPrivateField(Object subject, String name) throws NoSuchFieldException, IllegalAccessException, SecurityException {
        Class<?> subjectClass = subject.getClass();
        Field inField = null;
        while (inField == null && subjectClass != null) {
            try {
                inField = subjectClass.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                subjectClass = subjectClass.getSuperclass();
            }
        }
        if (inField == null) throw new NoSuchFieldException(MessageFormat.format("There is no field {0} in class {1}", name, subject.getClass().getName()));

        boolean originalAccessible = inField.isAccessible();
        inField.setAccessible(true);
        Object value;
        try {
            value = inField.get(subject);
        } finally {
            inField.setAccessible(originalAccessible);
        }
        return value;
    }
}
