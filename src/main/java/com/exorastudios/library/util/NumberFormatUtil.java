package com.exorastudios.library.util;

import java.text.DecimalFormat;

public final class NumberFormatUtil {

    private static final DecimalFormat COMMA_FORMAT = new DecimalFormat("#,###");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.##");

    private static final String[] SUFFIXES = {
            "", "k", "m", "b", "t", "q", "Q", "s", "S", "o", "n", "d",
            "u", "D", "T", "Qa", "Qi", "Se", "Sp", "O", "N", "V", "X", "Z"
    };

    private NumberFormatUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public enum FormatType {
        RAW,
        STANDARD,
        FORMATTED
    }

    public static String format(String value, FormatType formatType) {
        if (value == null || formatType == null) {
            return "";
        }

        double number;
        try {
            number = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return formatType == FormatType.STANDARD ? value : "";
        }

        return switch (formatType) {
            case RAW -> String.valueOf(number);
            case STANDARD -> COMMA_FORMAT.format(number);
            case FORMATTED -> formatWithSuffix(number);
        };
    }

    private static String formatWithSuffix(double number) {
        if (number < 1000) {
            return DECIMAL_FORMAT.format(number);
        }

        int index = 0;
        while (number >= 1000 && index < SUFFIXES.length - 1) {
            number /= 1000.0;
            index++;
        }

        return DECIMAL_FORMAT.format(number) + SUFFIXES[index];
    }
}
