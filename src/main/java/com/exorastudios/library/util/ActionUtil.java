package com.exorastudios.library.util;

import com.exorastudios.library.text.ExoText;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ActionUtil {

    private ActionUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void action(@NotNull Plugin plugin, @NotNull Player player, @NotNull String path, @NotNull FileConfiguration config) {
        List<String> actions = config.getStringList(path);
        if (actions == null || actions.isEmpty()) return;

        for (String action : actions) {
            run(plugin, action, player);
        }
    }

    public static void run(@NotNull Plugin plugin, @NotNull String event, @NotNull Player player) {
        int spaceIndex = event.indexOf(" ");
        if (spaceIndex == -1) return;

        String actionType = event.substring(0, spaceIndex).toLowerCase();
        String content = event.substring(spaceIndex + 1);

        switch (actionType) {
            case "[playercommand]" -> Bukkit.getScheduler().runTask(plugin, () -> player.performCommand(content));

            case "[consolecommand]" -> {
                String command = content.replace("%player%", player.getName());
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
            }

            case "[consolecommandchance]" -> {
                String[] parts = content.split(";", 2);
                if (parts.length == 2) {
                    try {
                        double chance = Double.parseDouble(parts[0].trim());
                        String chanceCommand = parts[1].replace("%player%", player.getName());
                        if (Math.random() * 100 < chance) {
                            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), chanceCommand));
                        }
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().warning("[DEBUG] Invalid chance format: " + content);
                    }
                }
            }

            case "[title]" -> {
                String[] titleArgs = content.split(";", 2);
                String title = ExoText.parse(titleArgs[0]);
                String subtitle = titleArgs.length > 1 ? ExoText.parse(titleArgs[1]) : "";
                player.sendTitle(title, subtitle, 10, 40, 10);
            }

            case "[subtitle]" -> player.sendTitle("", ExoText.parse(content), 10, 40, 10);

            case "[actionbar]" -> player.sendActionBar(ExoText.parse(content));

            case "[sound]" -> {
                try {
                    Sound sound = Sound.valueOf(content.toUpperCase());
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§cInvalid sound: §f" + content);
                    Bukkit.getLogger().warning("[DEBUG] Invalid sound: " + content);
                }
            }

            case "[broadcast]" -> Bukkit.getScheduler().runTask(plugin, () ->  Bukkit.broadcastMessage(ExoText.parse(content)));

            default -> Bukkit.getLogger().warning("[DEBUG] Unknown action type: " + actionType);
        }
    }
}
