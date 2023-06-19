package com.digitalingressos.ingressos.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class UtilMoney {

    public static BigDecimal parseMoneyToBigDecimal(String value, Locale locale) {
        String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());

        String cleanString = value.replaceAll(replaceable, "");

        return new BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR
        );
    }

    public static double parseIntToDouble(int value) {
        return (double) (value / 100.0);
    }

    public static double parseMoneyToDouble(String value, Locale locale) {

        BigDecimal bigDecimal = parseMoneyToBigDecimal(value, locale);
        return bigDecimal.doubleValue();
    }

    public static String parseDoubleToMoney(double value, Locale locale) {
        return NumberFormat.getCurrencyInstance(locale).format(value);
    }
}
