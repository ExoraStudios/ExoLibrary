package com.exorastudios.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SlotParser {

    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\[)?(\\d+)-(\\d+)(]?)");
    private static final Pattern SINGLE_PATTERN = Pattern.compile("^\\d+$");

    private SlotParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<Integer> parse(String slotStr) {
        List<Integer> slots = new ArrayList<>();
        if (slotStr == null || slotStr.isEmpty()) return slots;

        String[] parts = slotStr.replaceAll("\\s", "").split(",");

        for (String part : parts) {
            Matcher rangeMatcher = RANGE_PATTERN.matcher(part);
            if (rangeMatcher.matches()) {
                int start = Integer.parseInt(rangeMatcher.group(2));
                int end = Integer.parseInt(rangeMatcher.group(3));
                for (int i = start; i <= end; i++) {
                    slots.add(i);
                }
            } else if (SINGLE_PATTERN.matcher(part).matches()) {
                slots.add(Integer.parseInt(part));
            }
        }
        return slots;
    }
}
