package com.exorastudios.library.util;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class MaterialParser {

    private MaterialParser() {}


    @Nullable
    public static Material parse(String input) {
        if (input == null || input.isEmpty()) return null;

        String normalized = input.trim()
                .toUpperCase(Locale.ENGLISH)
                .replace(' ', '_')
                .replace('-', '_');

        try {
            return Material.valueOf(normalized);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }


    public static Material parseOrDefault(String input, Material fallback) {
        Material mat = parse(input);
        return mat != null ? mat : fallback;
    }
}
