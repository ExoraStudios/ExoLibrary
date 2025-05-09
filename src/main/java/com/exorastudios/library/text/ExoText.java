package com.exorastudios.library.text;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class ExoText {

    private static final Pattern hexPattern = Pattern.compile("<#[0-9a-fA-F]{6}>");
    private static final Map<String, String> tagToLegacy;

    static {
        Map<String, String> TAGS = new HashMap<>();

        TAGS.put("<black>", "§0");
        TAGS.put("<dark_blue>", "§1");
        TAGS.put("<dark_green>", "§2");
        TAGS.put("<dark_aqua>", "§3");
        TAGS.put("<dark_red>", "§4");
        TAGS.put("<dark_purple>", "§5");
        TAGS.put("<gold>", "§6");
        TAGS.put("<gray>", "§7");
        TAGS.put("<dark_gray>", "§8");
        TAGS.put("<blue>", "§9");
        TAGS.put("<green>", "§a");
        TAGS.put("<aqua>", "§b");
        TAGS.put("<red>", "§c");
        TAGS.put("<light_purple>", "§d");
        TAGS.put("<yellow>", "§e");
        TAGS.put("<white>", "§f");


        TAGS.put("<bold>", "§l");
        TAGS.put("<b>", "§l");
        TAGS.put("<italic>", "§o");
        TAGS.put("<i>", "§o");
        TAGS.put("<em>", "§o");
        TAGS.put("<underline>", "§n");
        TAGS.put("<u>", "§n");
        TAGS.put("<strikethrough>", "§m");
        TAGS.put("<st>", "§m");
        TAGS.put("<obfuscated>", "§k");
        TAGS.put("<obf>", "§k");
        TAGS.put("<magic>", "§k");
        TAGS.put("<reset>", "§r");


        String[] resetTags = {
                "</bold>", "</b>", "</italic>", "</i>", "</em>", "</underline>", "</u>",
                "</strikethrough>", "</st>", "</obfuscated>", "</obf>", "</magic>", "</color>",
                "</black>", "</dark_blue>", "</dark_green>", "</dark_aqua>", "</dark_red>",
                "</dark_purple>", "</gold>", "</gray>", "</dark_gray>", "</blue>", "</green>",
                "</aqua>", "</red>", "</light_purple>", "</yellow>", "</white>"
        };
        for (String tag : resetTags) {
            TAGS.put(tag, "§r");
        }

        tagToLegacy = Collections.unmodifiableMap(TAGS);
    }

    private ExoText() {
        throw new IllegalStateException("Utility class");
    }

    public static String parse(@Nullable String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length());
        int i = 0;

        while (i < text.length()) {
            char c = text.charAt(i);

            if (c == '<' && i + 1 < text.length()) {

                String hex = tryParseHex(text, i);
                if (hex != null) {
                    result.append(hex);
                    i += 8;
                    continue;
                }


                String tag = tryParseTag(text, i);
                if (tag != null) {
                    result.append(tagToLegacy.getOrDefault(tag, "<"));
                    i += tag.length();
                    if (!tagToLegacy.containsKey(tag)) {
                        i--;
                    }
                    continue;
                }

                result.append(c);
                i++;
            } else if (c == '&' && i + 1 < text.length()) {
                char next = text.charAt(i + 1);
                if (isLegacyCode(next)) {
                    result.append('§').append(Character.toLowerCase(next));
                    i += 2;
                } else {
                    result.append(c);
                    i++;
                }
            } else {
                result.append(c);
                i++;
            }
        }

        return result.toString();
    }

    private static @Nullable String tryParseHex(String text, int start) {
        if (start + 8 > text.length() || text.charAt(start + 1) != '#') {
            return null;
        }
        String substring = text.substring(start, Math.min(start + 9, text.length()));
        if (!hexPattern.matcher(substring).matches()) {
            return null;
        }
        StringBuilder hex = new StringBuilder(14).append('§').append('x');
        for (int i = 2; i < 8; i++) {
            hex.append('§').append(Character.toLowerCase(substring.charAt(i)));
        }
        return hex.toString();
    }

    private static @Nullable String tryParseTag(String text, int start) {
        int end = text.indexOf('>', start + 1);
        if (end == -1) {
            return null;
        }
        String tag = text.substring(start, end + 1);
        return tagToLegacy.containsKey(tag) ? tag : null;
    }

    private static boolean isLegacyCode(char c) {
        c = Character.toLowerCase(c);
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'k' && c <= 'o') || c == 'r' || c == 'x';
    }
}