package com.exorastudios.library.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;

public final class WorldUtil {

    private WorldUtil() {}

    public static boolean isNether(World world) {
        if (world == null) return false;
        return world.getEnvironment() == Environment.NETHER;
    }

    public static boolean isEnd(World world) {
        if (world == null) return false;
        return world.getEnvironment() == Environment.THE_END;
    }

    public static boolean isOverworld(World world) {
        if (world == null) return false;
        return world.getEnvironment() == Environment.NORMAL;
    }

    public static boolean isLocationInWorld(Location loc, World world) {
        return loc != null && loc.getWorld() != null && loc.getWorld().equals(world);
    }

    public static Location getSpawnLocation(World world) {
        if (world == null) return null;
        return world.getSpawnLocation();
    }

    public static boolean isSameWorld(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) return false;
        if (loc1.getWorld() == null || loc2.getWorld() == null) return false;
        return loc1.getWorld().equals(loc2.getWorld());
    }

    public static boolean isWorldEnvironment(World world, Environment env) {
        if (world == null || env == null) return false;
        return world.getEnvironment() == env;
    }
}
