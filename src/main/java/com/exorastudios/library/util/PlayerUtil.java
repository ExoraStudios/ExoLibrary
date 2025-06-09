package com.exorastudios.library.util;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class PlayerUtil {

    private PlayerUtil() {}

    public static boolean isOnline(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        return player != null && player.isOnline();
    }

    @Nullable
    public static Player getOnlinePlayer(String playerName) {
        return Bukkit.getPlayerExact(playerName);
    }

    @Nullable
    public static OfflinePlayer getOfflinePlayer(String playerName) {
        return Bukkit.getOfflinePlayer(playerName);
    }

    @Nullable
    public static UUID getUUIDByName(String playerName) {
        Player online = Bukkit.getPlayerExact(playerName);
        if (online != null) return online.getUniqueId();
        OfflinePlayer offline = Bukkit.getOfflinePlayer(playerName);
        if (offline != null && offline.hasPlayedBefore()) return offline.getUniqueId();
        return null;
    }

    public static boolean isCreative(Player player) {
        return player != null && player.getGameMode() == GameMode.CREATIVE;
    }

    public static boolean isSurvival(Player player) {
        return player != null && player.getGameMode() == GameMode.SURVIVAL;
    }

    public static boolean isAdventure(Player player) {
        return player != null && player.getGameMode() == GameMode.ADVENTURE;
    }

    public static boolean isSpectator(Player player) {
        return player != null && player.getGameMode() == GameMode.SPECTATOR;
    }

    public static boolean isFlying(Player player) {
        return player != null && player.isFlying();
    }

    public static boolean isOp(Player player) {
        return player != null && player.isOp();
    }

    public static boolean hasPermission(Player player, String permission) {
        return player != null && player.hasPermission(permission);
    }

    public static boolean kickPlayer(Player player, String reason) {
        if (player == null || !player.isOnline()) return false;
        player.kickPlayer(reason);
        return true;
    }

    public static boolean hasPlayedBefore(String playerName) {
        OfflinePlayer player = getOfflinePlayer(playerName);
        return player != null && player.hasPlayedBefore();
    }

    public static int getPing(Player player) {
        if (player == null || !player.isOnline()) {
            return -1;
        }
        return player.getPing();
    }
    public static boolean teleportToPlayer(Player toTeleport, Player target) {
        if (toTeleport == null || target == null) return false;
        if (!toTeleport.isOnline() || !target.isOnline()) return false;
        toTeleport.teleport(target.getLocation());
        return true;
    }

    public static int getDisplayNameLength(Player player) {
        if (player == null) return 0;
        String name = player.getDisplayName();
        return name != null ? name.length() : 0;
    }
}
