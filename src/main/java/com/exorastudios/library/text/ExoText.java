package com.exorastudios.library.text;

import net.md_5.bungee.api.ChatColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ExoText {

    private ExoText() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, String> MINI_TO_LEGACY = new HashMap<>();
    private static final Pattern MINI_TAG_PATTERN = Pattern.compile("<(/?)([a-zA-Z_]+)>");
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    static {

        MINI_TO_LEGACY.put("black", "0");
        MINI_TO_LEGACY.put("dark_blue", "1");
        MINI_TO_LEGACY.put("dark_green", "2");
        MINI_TO_LEGACY.put("dark_aqua", "3");
        MINI_TO_LEGACY.put("dark_red", "4");
        MINI_TO_LEGACY.put("dark_purple", "5");
        MINI_TO_LEGACY.put("gold", "6");
        MINI_TO_LEGACY.put("gray", "7");
        MINI_TO_LEGACY.put("dark_gray", "8");
        MINI_TO_LEGACY.put("blue", "9");
        MINI_TO_LEGACY.put("green", "a");
        MINI_TO_LEGACY.put("aqua", "b");
        MINI_TO_LEGACY.put("red", "c");
        MINI_TO_LEGACY.put("light_purple", "d");
        MINI_TO_LEGACY.put("yellow", "e");
        MINI_TO_LEGACY.put("white", "f");


        MINI_TO_LEGACY.put("reset", "r");
        MINI_TO_LEGACY.put("bold", "l");
        MINI_TO_LEGACY.put("italic", "o");
        MINI_TO_LEGACY.put("underlined", "n");
        MINI_TO_LEGACY.put("strikethrough", "m");
        MINI_TO_LEGACY.put("obfuscated", "k");
    }

    public static String parse(String message) {
        if (message == null || message.isEmpty()) return "";


        message = ChatColor.translateAlternateColorCodes('&', message);


        Matcher hexMatcher = HEX_PATTERN.matcher(message);
        while (hexMatcher.find()) {
            String hex = hexMatcher.group();
            ChatColor color = ChatColor.of(hex.substring(1));
            message = message.replace(hex, color.toString());
        }


        StringBuilder output = new StringBuilder();
        Matcher miniMatcher = MINI_TAG_PATTERN.matcher(message);
        int lastEnd = 0;

        for (; miniMatcher.find(); lastEnd = miniMatcher.end()) {
            output.append(message, lastEnd, miniMatcher.start());

            boolean isClosing = miniMatcher.group(1).equals("/");
            String tag = miniMatcher.group(2).toLowerCase();

            if (isClosing) {
                output.append("§r");
            } else {
                String legacyCode = MINI_TO_LEGACY.get(tag);
                if (legacyCode != null) {
                    output.append("§").append(legacyCode);
                }
            }
        }

        output.append(message.substring(lastEnd));
        return output.toString();
    }

    public static List<String> parseList(List<String> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(ExoText::parse).collect(Collectors.toList());
    }
}
