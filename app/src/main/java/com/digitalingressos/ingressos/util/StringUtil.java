package com.digitalingressos.ingressos.util;

import androidx.annotation.StringDef;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {

    private StringBuilder builder;

    public StringUtil() {
        builder = new StringBuilder();
    }

    public StringUtil appendWithSpace(String str) {
        if (builder.length() > 0) {
            builder.append(" ");
        }
        if (str.length() > 0) {
            builder.append(str);
        }
        return this;
    }

    public StringUtil appendWithDash(String str) {
        if (builder.length() > 0) {
            builder.append("-");
        }
        if (str.length() > 0) {
            builder.append(str);
        }
        return this;
    }

    public StringUtil append(String str) {
        builder.append(str);
        return this;
    }

    public StringUtil appendWithNewLine(String str) {
        if (builder.length() > 0) {
            builder.append("\n");
        }
        if (str.length() > 0) {
            builder.append(str);
        }
        return this;
    }

    public static String padLeft(String input, int length, char paddingChar) {
        StringBuilder sb = new StringBuilder(input);
        while (sb.length() < length) {
            sb.insert(0, paddingChar);
        }
        return sb.toString();
    }

    public static String padRight(String input, int length, char paddingChar) {
        StringBuilder sb = new StringBuilder(input);
        while (sb.length() < length) {
            sb.append(paddingChar);
        }
        return sb.toString();
    }

    public static String removerAcentos(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public String toString() {
        return builder.toString();
    }
}
