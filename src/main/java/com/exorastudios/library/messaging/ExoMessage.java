package com.exorastudios.library.messaging;

import com.exorastudios.library.text.ExoText;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class ExoMessage {

    private ExoMessage() {
        throw new IllegalStateException("Utility class");
    }

    private static String parsePlaceholders(String message, String... placeholders) {
        if (message == null) return "";
        if (placeholders != null && placeholders.length > 1) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < placeholders.length - 1; i += 2) {
                map.put(placeholders[i], placeholders[i + 1]);
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
        }
        return ExoText.parse(message);
    }

    public static void send(Player player, String message, String... placeholders) {
        if (player == null || message == null) return;
        player.sendMessage(parsePlaceholders(message, placeholders));
    }

    public static void sendActionbar(Player player, String message, String... placeholders) {
        if (player == null || message == null) return;
        player.sendActionBar(parsePlaceholders(message, placeholders));
    }

    public static void sendTitle(Player player, String title, String subtitle, String... placeholders) {
        sendTitle(player, title, subtitle, 10, 70, 20, placeholders);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut, String... placeholders) {
        if (player == null) return;
        player.sendTitle(
                parsePlaceholders(title, placeholders),
                parsePlaceholders(subtitle, placeholders),
                fadeIn, stay, fadeOut
        );
    }
}
