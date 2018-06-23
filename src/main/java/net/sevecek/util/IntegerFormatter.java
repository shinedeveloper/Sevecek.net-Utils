package net.sevecek.util;

import java.util.*;

public class IntegerFormatter extends AbstractNumberFormatter {

    public IntegerFormatter() {
    }

    public IntegerFormatter(String pattern) {
        super(pattern);
    }

    public String print(Integer number,
                        Locale locale) {
        return super.print(number, locale);
    }

    public String print(Integer number) {
        return super.print(number);
    }

    @Override
    public Integer parse(String text,
                         Locale locale) {
        return super.parse(text, locale).intValue();
    }

    @Override
    public Integer parse(String text) {
        return super.parse(text).intValue();
    }
}
