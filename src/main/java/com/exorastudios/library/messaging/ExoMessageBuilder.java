package com.exorastudios.library.messaging;

import com.exorastudios.library.logging.ExoLogger;
import com.exorastudios.library.text.ExoText;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExoMessageBuilder {

    public static void send(@NotNull Player player, @NotNull FileConfiguration config, @NotNull String path, String... args) {
        String fullPath = path.startsWith("messages.") ? path : "messages." + path;

        if (!config.getBoolean(fullPath + ".enabled", true)) {
            return;
        }

        String types = config.getString(fullPath + ".type", "chat");
        boolean isChat = types.contains("chat");
        boolean isActionbar = types.contains("actionbar");
        boolean isTitle = types.contains("title");
        boolean isList = types.equals("list");

        if (isList) {
            List<String> messages = config.getStringList(fullPath + ".messages");
            messages.stream()
                    .map(message -> ExoText.parse(parsePlaceholders(message, args)))
                    .forEach(player::sendMessage);

            parseOthers(player, config, fullPath, args);
            return;
        }

        if (isChat) {
            String message = config.getString(fullPath + ".message");
            if (message != null && !message.isEmpty()) {
                message = parsePlaceholders(message, args);
                player.sendMessage(ExoText.parse(message));
            }
        }

        if (isActionbar) {
            String actionbar = config.getString(fullPath + ".actionbar.message");
            if (actionbar != null && !actionbar.isEmpty()) {
                actionbar = parsePlaceholders(actionbar, args);
                player.sendActionBar(ExoText.parse(actionbar));
            }
        }

        if (isTitle) {
            String mainTitle = config.getString(fullPath + ".title.main");
            String subTitle = config.getString(fullPath + ".title.sub");
            if (mainTitle != null || subTitle != null) {
                int fadeIn = config.getInt(fullPath + ".title.duration.fade_in", 300) / 50;
                int stay = config.getInt(fullPath + ".title.duration.stay", 2000) / 50;
                int fadeOut = config.getInt(fullPath + ".title.duration.fade_out", 300) / 50;
                player.sendTitle(
                        ExoText.parse(mainTitle != null ? parsePlaceholders(mainTitle, args) : ""),
                        ExoText.parse(subTitle != null ? parsePlaceholders(subTitle, args) : ""),
                        fadeIn, stay, fadeOut
                );
            }
        }

        parseSound(player, config, fullPath);
    }

    public static void parseOthers(@NotNull Player player, @NotNull FileConfiguration config, @NotNull String fullPath, String... args) {
        String actionbar = config.getString(fullPath + ".actionbar.message");
        if (actionbar != null && !actionbar.isEmpty()) {
            actionbar = parsePlaceholders(actionbar, args);
            player.sendActionBar(ExoText.parse(actionbar));
        }

        String mainTitle = config.getString(fullPath + ".title.main");
        String subTitle = config.getString(fullPath + ".title.sub");
        if (mainTitle != null || subTitle != null) {
            int fadeIn = config.getInt(fullPath + ".title.duration.fade_in", 300) / 50;
            int stay = config.getInt(fullPath + ".title.duration.stay", 2000) / 50;
            int fadeOut = config.getInt(fullPath + ".title.duration.fade_out", 300) / 50;
            player.sendTitle(
                    ExoText.parse(mainTitle != null ? parsePlaceholders(mainTitle, args) : ""),
                    ExoText.parse(subTitle != null ? parsePlaceholders(subTitle, args) : ""),
                    fadeIn, stay, fadeOut
            );
        }

        parseSound(player, config, fullPath);
    }

    public static void parseSound(@NotNull Player player, @NotNull FileConfiguration config, @NotNull String fullPath) {
        if (config.getBoolean(fullPath + ".sound.enabled", false)) {
            String soundKey = config.getString(fullPath + ".sound.key");
            if (soundKey != null && !soundKey.isEmpty()) {
                try {
                    float volume = (float) config.getDouble(fullPath + ".sound.volume", 1.0);
                    float pitch = (float) config.getDouble(fullPath + ".sound.pitch", 1.0);
                    player.playSound(player.getLocation(), soundKey, volume, pitch);
                } catch (IllegalArgumentException e) {
                    ExoLogger.Log.error("Invalid sound for path: " + fullPath + " => " + soundKey);
                }
            }
        }
    }

    public static String parsePlaceholders(String input, String... args) {
        if (input == null) return "";
        for (int i = 0; i < args.length; i += 2) {
            input = input.replace(args[i], args[i + 1]);
        }
        return input;
    }
}
