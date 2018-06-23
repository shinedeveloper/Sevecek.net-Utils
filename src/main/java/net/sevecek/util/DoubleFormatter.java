package net.sevecek.util;

import java.util.*;

public class DoubleFormatter extends AbstractNumberFormatter {

    public DoubleFormatter() {
    }

    public DoubleFormatter(String pattern) {
        super(pattern);
    }

    public String print(Double number,
                        Locale locale) {
        return super.print(number, locale);
    }

    public String print(Double number) {
        return super.print(number);
    }

    @Override
    public Double parse(String text,
                        Locale locale) {
        return super.parse(text, locale).doubleValue();
    }

    @Override
    public Double parse(String text) {
        return super.parse(text).doubleValue();
    }
}
