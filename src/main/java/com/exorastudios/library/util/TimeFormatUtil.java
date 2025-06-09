package com.exorastudios.library.util;

import java.util.concurrent.TimeUnit;

public final class TimeFormatUtil {

    private TimeFormatUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public enum FormatType {
        HMS,        // 01:10:10
        VERBOSE,    // 1h 10m 10s
        COMPACT     // 1h 10m or 10m 5s etc
    }

    public static String format(long millis, FormatType type) {
        if (millis < 0 || type == null) return "0s";

        return switch (type) {
            case HMS -> formatHMS(millis);
            case VERBOSE -> formatVerbose(millis);
            case COMPACT -> formatCompact(millis);
        };
    }

    private static String formatHMS(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private static String formatVerbose(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        StringBuilder builder = new StringBuilder();
        if (hours > 0) builder.append(hours).append("h ");
        if (minutes > 0 || hours > 0) builder.append(minutes).append("m ");
        builder.append(seconds).append("s");

        return builder.toString().trim();
    }

    private static String formatCompact(long millis) {
        long totalSeconds = millis / 1000;
        if (totalSeconds < 60) return totalSeconds + "s";

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        if (minutes < 60) return minutes + "m " + (seconds > 0 ? seconds + "s" : "");

        long hours = minutes / 60;
        minutes %= 60;
        return hours + "h" + (minutes > 0 ? " " + minutes + "m" : "");
    }
}
