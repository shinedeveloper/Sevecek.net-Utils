package net.sevecek.util;

import java.text.*;
import java.util.*;

public class TextUtils {

    public static int convertIntFromStringOnDesktop(String text) {
        return convertIntFromStringOnServer(text, Locale.getDefault());
    }


    public static int convertIntFromStringOnServer(String text, Locale language) {
        long value = convertLongFromStringOnServer(text, language);
        if (value > Integer.MAX_VALUE) {
            throw new ApplicationPublicException("Value_{0}_is_too_large", value);
        }
        if (value < Integer.MIN_VALUE) {
            throw new ApplicationPublicException("Value_{0}_is_too_small", value);
        }
        return (int) value;
    }


    public static long convertLongFromStringOnDesktop(String text) {
        return convertLongFromStringOnServer(text, Locale.getDefault());
    }


    public static long convertLongFromStringOnServer(String text, Locale language) {
        text = text.trim();
        NumberFormat numberFormat = NumberFormat.getIntegerInstance(language);
        ParsePosition pos = new ParsePosition(0);
        Number number = numberFormat.parse(text, pos);
        if (pos.getIndex() != text.length()) {
            try {
                long alternativeNumber = Long.parseLong(text);
                return alternativeNumber;
            } catch (NumberFormatException e) {
                throw new ApplicationPublicException(e, "Text_{0}_cannot_be_converted_to_an_int_or_long", text);
            }
        }
        return number.longValue();
    }


    public static double convertDoubleFromStringOnDesktop(String text) {
        return convertDoubleFromStringOnServer(text, Locale.getDefault());
    }


    public static double convertDoubleFromStringOnServer(String text, Locale language) {
        text = text.trim();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(language);
        ParsePosition pos = new ParsePosition(0);
        Number number = numberFormat.parse(text, pos);
        if (pos.getIndex() != text.length()) {
            try {
                double alternativeNumber = Double.parseDouble(text);
                return alternativeNumber;
            } catch (NumberFormatException e) {
                throw new ApplicationPublicException(e, "Text_{0}_cannot_be_converted_to_a_number", text);
            }
        }
        return number.doubleValue();
    }


    public static String convertIntToStringOnDesktop(int number) {
        return convertIntToStringOnServer(number, Locale.getDefault());
    }


    public static String convertIntToStringOnServer(int number, Locale language) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance(language);
        return numberFormat.format(number);
    }


    public static String convertLongToStringOnDesktop(long number) {
        return convertLongToStringOnServer(number, Locale.getDefault());
    }


    public static String convertLongToStringOnServer(long number, Locale language) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance(language);
        return numberFormat.format(number);
    }


    public static String convertDoubleToStringOnDesktop(double number, int maxFractionDigits) {
        return convertDoubleToStringOnServer(number, maxFractionDigits, Locale.getDefault());
    }


    public static String convertDoubleToStringOnServer(double number, int maxFractionDigits, Locale language) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(language);
        numberFormat.setMaximumFractionDigits(maxFractionDigits);
        return numberFormat.format(number);
    }
}
