package com.exorastudios.library.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CooldownUtil {

    private static final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    private CooldownUtil() {}

    public static boolean isOnCooldown(String key, Player player) {
        return isOnCooldown(key, player.getUniqueId());
    }

    public static boolean isOnCooldown(String key, UUID uuid) {
        Map<UUID, Long> map = cooldowns.get(key);
        if (map == null) return false;
        Long time = map.get(uuid);
        if (time == null) return false;
        return time > System.currentTimeMillis();
    }

    public static long getRemaining(String key, Player player) {
        return getRemaining(key, player.getUniqueId());
    }

    public static long getRemaining(String key, UUID uuid) {
        Map<UUID, Long> map = cooldowns.get(key);
        if (map == null) return 0;
        Long time = map.get(uuid);
        if (time == null) return 0;
        long remaining = time - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    public static void setCooldown(String key, Player player, long milliseconds) {
        setCooldown(key, player.getUniqueId(), milliseconds);
    }

    public static void setCooldown(String key, UUID uuid, long milliseconds) {
        cooldowns.computeIfAbsent(key, k -> new HashMap<>())
                .put(uuid, System.currentTimeMillis() + milliseconds);
    }

    public static void clear() {
        cooldowns.clear();
    }

    public static void clearCooldown(String key, Player player) {
        clearCooldown(key, player.getUniqueId());
    }

    public static void clearCooldown(String key, UUID uuid) {
        Map<UUID, Long> map = cooldowns.get(key);
        if (map != null) {
            map.remove(uuid);
        }
    }
}
