package com.exorastudios.library.util;

public final class TimeUtil {

    private TimeUtil() {}

    public static long ticksToMillis(long ticks) {
        return ticks * 50;
    }

    public static long millisToTicks(long millis) {
        return millis / 50;
    }

    public static long secondsToTicks(long seconds) {
        return seconds * 20;
    }

    public static long ticksToSeconds(long ticks) {
        return ticks / 20;
    }

    public static long ticksToMinutes(long ticks) {
        return ticksToSeconds(ticks) / 60;
    }

    public static long minutesToTicks(long minutes) {
        return secondsToTicks(minutes * 60);
    }

    public static long ticksToHours(long ticks) {
        return ticksToMinutes(ticks) / 60;
    }

    public static long hoursToTicks(long hours) {
        return minutesToTicks(hours * 60);
    }

    public static long secondsToMillis(long seconds) {
        return seconds * 1000L;
    }

    public static long millisToSeconds(long millis) {
        return millis / 1000L;
    }

    public static long minutesToMillis(long minutes) {
        return minutes * 60_000L;
    }

    public static long millisToMinutes(long millis) {
        return millis / 60_000L;
    }

    public static long hoursToMillis(long hours) {
        return hours * 3_600_000L;
    }

    public static long millisToHours(long millis) {
        return millis / 3_600_000L;
    }
}
