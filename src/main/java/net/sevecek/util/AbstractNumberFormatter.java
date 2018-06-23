package net.sevecek.util;

import java.lang.reflect.*;
import java.text.*;
import java.util.Locale;

public class AbstractNumberFormatter {

    private boolean lenient = false;
    private String pattern;

    public AbstractNumberFormatter() {
    }

    public AbstractNumberFormatter(String pattern) {
        this.pattern = pattern;
    }

    public String print(Number number,
                        Locale locale) {
        return getNumberFormat(locale).format(number);
    }

    public String print(Number number) {
        return print(number, resolveLocale());
    }

    public Number parse(String text,
                        Locale locale) {
        NumberFormat format = getNumberFormat(locale);
        ParsePosition position = new ParsePosition(0);
        Number number = format.parse(text, position);
        if (position.getErrorIndex() != -1) {
            throw new NumberFormatException("Invalid number " + text + " at position " + position.getIndex());
        }
        if (!this.lenient) {
            if (text.length() != position.getIndex()) {
                // indicates a part of the string that was not parsed
                throw new NumberFormatException("Invalid number " + text + " at position " + position.getIndex());
            }
        }
        return number;
    }

    public Number parse(String text) {
        return parse(text, resolveLocale());
    }

    public void setLenient(boolean newValue) {
        lenient = newValue;
    }

    public NumberFormat getNumberFormat(java.util.Locale locale) {
        NumberFormat format = NumberFormat.getInstance(locale);
        if (!(format instanceof DecimalFormat)) {
            if (this.pattern != null) {
                throw new IllegalStateException("Cannot support pattern for non-DecimalFormat: " + format);
            }
            return format;
        }
        DecimalFormat decimalFormat = (DecimalFormat) format;
        decimalFormat.setParseBigDecimal(true);
        if (this.pattern != null) {
            decimalFormat.applyPattern(this.pattern);
        }
        return decimalFormat;
    }

    public void setPattern(java.lang.String newValue) {
        this.pattern = newValue;
    }

    private Locale resolveLocale() {
        try {
            Class<?> localeContextHolderClass = Thread.currentThread().getContextClassLoader().loadClass(
                    "org.springframework.context.i18n.LocaleContextHolder");
            Method getLocaleMethod = localeContextHolderClass.getMethod("getLocale");
            Object locale = getLocaleMethod.invoke(null);
            return (Locale) locale;
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ex) {
            return Locale.getDefault();
        }
    }

}
