package net.sevecek.console;

import java.awt.Color;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;

abstract class AbstractConsoleMethods implements ConsoleMethods {

    protected abstract int readPhysicalChar() throws IOException;

    @Override
    public abstract String readLine();

    @Override
    public abstract String readLine(String message, Object... args);

    @Override
    public abstract char[] readPassword();

    @Override
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

    @Override
    public abstract void print(String text);

    @Override
    public abstract void print(char c);

    @Override
    public abstract void println();

    @Override
    public abstract void println(char c);

    @Override
    public abstract void println(String text);

    @Override
    public abstract void printf(String format, Object... args);


    @Override
    public void print(boolean b) {
        print(Boolean.toString(b));
    }


    @Override
    public void print(int i) {
        String text = NumberFormat.getIntegerInstance().format(i);
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


    @Override
    public void setTextColor(Color color) {
        if (color != null) {
            int ansiColor = findNearestAnsiColor(color);
            print("\u001b[38;5;" + ansiColor + "m");
        } else {
            print("\u001b[0m");
        }
    }


    @Override
    public void setBackgroundColor(Color color) {
        if (color != null) {
            int ansiColor = findNearestAnsiColor(color);
            print("\u001b[48;5;" + ansiColor + "m");
        } else {
            print("\u001b[0m");
        }
    }


    private int findNearestAnsiColor(Color color) {
        int nearestIndex = 0;
        double nearestDistance = (
                Math.abs(ansiColors[0].r - color.getRed())
                + Math.abs(ansiColors[0].g - color.getGreen())
                + Math.abs(ansiColors[0].b - color.getBlue())) / 3.0;

        for (int i=1; i<ansiColors.length; i++) {
            Rgb ansiColor = ansiColors[i];
            double distance = (
                    Math.abs(ansiColor.r - color.getRed())
                    + Math.abs(ansiColor.g - color.getGreen())
                    + Math.abs(ansiColor.b - color.getBlue())) / 3.0;
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = i;
            }
        }
        return nearestIndex;
    }


    private static final Rgb[] ansiColors = new Rgb[] {
        new Rgb(0,0,0),
        new Rgb(128,0,0),
        new Rgb(0,128,0),
        new Rgb(128,128,0),
        new Rgb(0,0,128),
        new Rgb(128,0,128),
        new Rgb(0,128,128),
        new Rgb(192,192,192),
        new Rgb(128,128,128),
        new Rgb(255,0,0),
        new Rgb(0,255,0),
        new Rgb(255,255,0),
        new Rgb(0,0,255),
        new Rgb(255,0,255),
        new Rgb(0,255,255),
        new Rgb(255,255,255),
        new Rgb(0,0,0),
        new Rgb(0,0,95),
        new Rgb(0,0,135),
        new Rgb(0,0,175),
        new Rgb(0,0,215),
        new Rgb(0,0,255),
        new Rgb(0,95,0),
        new Rgb(0,95,95),
        new Rgb(0,95,135),
        new Rgb(0,95,175),
        new Rgb(0,95,215),
        new Rgb(0,95,255),
        new Rgb(0,135,0),
        new Rgb(0,135,95),
        new Rgb(0,135,135),
        new Rgb(0,135,175),
        new Rgb(0,135,215),
        new Rgb(0,135,255),
        new Rgb(0,175,0),
        new Rgb(0,175,95),
        new Rgb(0,175,135),
        new Rgb(0,175,175),
        new Rgb(0,175,215),
        new Rgb(0,175,255),
        new Rgb(0,215,0),
        new Rgb(0,215,95),
        new Rgb(0,215,135),
        new Rgb(0,215,175),
        new Rgb(0,215,215),
        new Rgb(0,215,255),
        new Rgb(0,255,0),
        new Rgb(0,255,95),
        new Rgb(0,255,135),
        new Rgb(0,255,175),
        new Rgb(0,255,215),
        new Rgb(0,255,255),
        new Rgb(95,0,0),
        new Rgb(95,0,95),
        new Rgb(95,0,135),
        new Rgb(95,0,175),
        new Rgb(95,0,215),
        new Rgb(95,0,255),
        new Rgb(95,95,0),
        new Rgb(95,95,95),
        new Rgb(95,95,135),
        new Rgb(95,95,175),
        new Rgb(95,95,215),
        new Rgb(95,95,255),
        new Rgb(95,135,0),
        new Rgb(95,135,95),
        new Rgb(95,135,135),
        new Rgb(95,135,175),
        new Rgb(95,135,215),
        new Rgb(95,135,255),
        new Rgb(95,175,0),
        new Rgb(95,175,95),
        new Rgb(95,175,135),
        new Rgb(95,175,175),
        new Rgb(95,175,215),
        new Rgb(95,175,255),
        new Rgb(95,215,0),
        new Rgb(95,215,95),
        new Rgb(95,215,135),
        new Rgb(95,215,175),
        new Rgb(95,215,215),
        new Rgb(95,215,255),
        new Rgb(95,255,0),
        new Rgb(95,255,95),
        new Rgb(95,255,135),
        new Rgb(95,255,175),
        new Rgb(95,255,215),
        new Rgb(95,255,255),
        new Rgb(135,0,0),
        new Rgb(135,0,95),
        new Rgb(135,0,135),
        new Rgb(135,0,175),
        new Rgb(135,0,215),
        new Rgb(135,0,255),
        new Rgb(135,95,0),
        new Rgb(135,95,95),
        new Rgb(135,95,135),
        new Rgb(135,95,175),
        new Rgb(135,95,215),
        new Rgb(135,95,255),
        new Rgb(135,135,0),
        new Rgb(135,135,95),
        new Rgb(135,135,135),
        new Rgb(135,135,175),
        new Rgb(135,135,215),
        new Rgb(135,135,255),
        new Rgb(135,175,0),
        new Rgb(135,175,95),
        new Rgb(135,175,135),
        new Rgb(135,175,175),
        new Rgb(135,175,215),
        new Rgb(135,175,255),
        new Rgb(135,215,0),
        new Rgb(135,215,95),
        new Rgb(135,215,135),
        new Rgb(135,215,175),
        new Rgb(135,215,215),
        new Rgb(135,215,255),
        new Rgb(135,255,0),
        new Rgb(135,255,95),
        new Rgb(135,255,135),
        new Rgb(135,255,175),
        new Rgb(135,255,215),
        new Rgb(135,255,255),
        new Rgb(175,0,0),
        new Rgb(175,0,95),
        new Rgb(175,0,135),
        new Rgb(175,0,175),
        new Rgb(175,0,215),
        new Rgb(175,0,255),
        new Rgb(175,95,0),
        new Rgb(175,95,95),
        new Rgb(175,95,135),
        new Rgb(175,95,175),
        new Rgb(175,95,215),
        new Rgb(175,95,255),
        new Rgb(175,135,0),
        new Rgb(175,135,95),
        new Rgb(175,135,135),
        new Rgb(175,135,175),
        new Rgb(175,135,215),
        new Rgb(175,135,255),
        new Rgb(175,175,0),
        new Rgb(175,175,95),
        new Rgb(175,175,135),
        new Rgb(175,175,175),
        new Rgb(175,175,215),
        new Rgb(175,175,255),
        new Rgb(175,215,0),
        new Rgb(175,215,95),
        new Rgb(175,215,135),
        new Rgb(175,215,175),
        new Rgb(175,215,215),
        new Rgb(175,215,255),
        new Rgb(175,255,0),
        new Rgb(175,255,95),
        new Rgb(175,255,135),
        new Rgb(175,255,175),
        new Rgb(175,255,215),
        new Rgb(175,255,255),
        new Rgb(215,0,0),
        new Rgb(215,0,95),
        new Rgb(215,0,135),
        new Rgb(215,0,175),
        new Rgb(215,0,215),
        new Rgb(215,0,255),
        new Rgb(215,95,0),
        new Rgb(215,95,95),
        new Rgb(215,95,135),
        new Rgb(215,95,175),
        new Rgb(215,95,215),
        new Rgb(215,95,255),
        new Rgb(215,135,0),
        new Rgb(215,135,95),
        new Rgb(215,135,135),
        new Rgb(215,135,175),
        new Rgb(215,135,215),
        new Rgb(215,135,255),
        new Rgb(215,175,0),
        new Rgb(215,175,95),
        new Rgb(215,175,135),
        new Rgb(215,175,175),
        new Rgb(215,175,215),
        new Rgb(215,175,255),
        new Rgb(215,215,0),
        new Rgb(215,215,95),
        new Rgb(215,215,135),
        new Rgb(215,215,175),
        new Rgb(215,215,215),
        new Rgb(215,215,255),
        new Rgb(215,255,0),
        new Rgb(215,255,95),
        new Rgb(215,255,135),
        new Rgb(215,255,175),
        new Rgb(215,255,215),
        new Rgb(215,255,255),
        new Rgb(255,0,0),
        new Rgb(255,0,95),
        new Rgb(255,0,135),
        new Rgb(255,0,175),
        new Rgb(255,0,215),
        new Rgb(255,0,255),
        new Rgb(255,95,0),
        new Rgb(255,95,95),
        new Rgb(255,95,135),
        new Rgb(255,95,175),
        new Rgb(255,95,215),
        new Rgb(255,95,255),
        new Rgb(255,135,0),
        new Rgb(255,135,95),
        new Rgb(255,135,135),
        new Rgb(255,135,175),
        new Rgb(255,135,215),
        new Rgb(255,135,255),
        new Rgb(255,175,0),
        new Rgb(255,175,95),
        new Rgb(255,175,135),
        new Rgb(255,175,175),
        new Rgb(255,175,215),
        new Rgb(255,175,255),
        new Rgb(255,215,0),
        new Rgb(255,215,95),
        new Rgb(255,215,135),
        new Rgb(255,215,175),
        new Rgb(255,215,215),
        new Rgb(255,215,255),
        new Rgb(255,255,0),
        new Rgb(255,255,95),
        new Rgb(255,255,135),
        new Rgb(255,255,175),
        new Rgb(255,255,215),
        new Rgb(255,255,255),
        new Rgb(8,8,8),
        new Rgb(18,18,18),
        new Rgb(28,28,28),
        new Rgb(38,38,38),
        new Rgb(48,48,48),
        new Rgb(58,58,58),
        new Rgb(68,68,68),
        new Rgb(78,78,78),
        new Rgb(88,88,88),
        new Rgb(98,98,98),
        new Rgb(108,108,108),
        new Rgb(118,118,118),
        new Rgb(128,128,128),
        new Rgb(138,138,138),
        new Rgb(148,148,148),
        new Rgb(158,158,158),
        new Rgb(168,168,168),
        new Rgb(178,178,178),
        new Rgb(188,188,188),
        new Rgb(198,198,198),
        new Rgb(208,208,208),
        new Rgb(218,218,218),
        new Rgb(228,228,228),
        new Rgb(238,238,238)
    };


    private static class Rgb {
        public Rgb(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        final int r;
        final int g;
        final int b;
    }
}
