package net.sevecek.util;

import java.util.*;

public class LongFormatter extends AbstractNumberFormatter {

    public LongFormatter() {
    }

    public LongFormatter(String pattern) {
        super(pattern);
    }

    public String print(Long number,
                        Locale locale) {
        return super.print(number, locale);
    }

    public String print(Long number) {
        return super.print(number);
    }

    @Override
    public Long parse(String text,
                         Locale locale) {
        return super.parse(text, locale).longValue();
    }

    @Override
    public Long parse(String text) {
        return super.parse(text).longValue();
    }
}
