package com.exorastudios.library.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class LocationUtil {

    private LocationUtil() {}

    public static Location cloneLocation(Location loc) {
        if (loc == null) return null;
        return new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public static boolean isSameBlock(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) return false;
        if (!loc1.getWorld().equals(loc2.getWorld())) return false;
        return loc1.getBlockX() == loc2.getBlockX()
            && loc1.getBlockY() == loc2.getBlockY()
            && loc1.getBlockZ() == loc2.getBlockZ();
    }

    public static Location getCenterOfBlock(Location loc) {
        if (loc == null) return null;
        return new Location(loc.getWorld(),
                loc.getBlockX() + 0.5,
                loc.getBlockY(),
                loc.getBlockZ() + 0.5);
    }

    public static Location getBelowBlock(Location loc) {
        if (loc == null) return null;
        return loc.clone().add(0, -1, 0);
    }

    public static Location getAboveBlock(Location loc) {
        if (loc == null) return null;
        return loc.clone().add(0, 1, 0);
    }

    public static List<Block> getBlocksInRadius(Location center, int radius) {
        List<Block> blocks = new ArrayList<>();
        if (center == null || radius < 0) return blocks;

        World world = center.getWorld();
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        int radiusSquared = radius * radius;

        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int y = Math.max(0, cy - radius); y <= Math.min(world.getMaxHeight(), cy + radius); y++) {
                for (int z = cz - radius; z <= cz + radius; z++) {
                    int dx = x - cx;
                    int dy = y - cy;
                    int dz = z - cz;
                    if (dx * dx + dy * dy + dz * dz <= radiusSquared) {
                        blocks.add(world.getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }

    public static boolean isSafeLocation(Location loc) {
        if (loc == null) return false;

        Block block = loc.getBlock();
        Block blockAbove = block.getRelative(0, 1, 0);
        Block blockBelow = block.getRelative(0, -1, 0);


        boolean blockIsSafe = block.isPassable() || block.getType() == Material.AIR;
        boolean blockAboveIsSafe = blockAbove.isPassable() || blockAbove.getType() == Material.AIR;
        boolean blockBelowIsSolid = !blockBelow.isPassable();

        return blockIsSafe && blockAboveIsSafe && blockBelowIsSolid;
    }

    public static Location getHighestSafeLocation(Location start) {
        if (start == null) return null;
        World world = start.getWorld();
        int maxY = world.getMaxHeight();

        int x = start.getBlockX();
        int z = start.getBlockZ();

        for (int y = maxY; y > 0; y--) {
            Location loc = new Location(world, x + 0.5, y, z + 0.5);
            if (isSafeLocation(loc)) {
                return loc;
            }
        }
        return null;
    }

    public static double distanceSquaredXZ(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) return -1;
        if (!loc1.getWorld().equals(loc2.getWorld())) return -1;

        double dx = loc1.getX() - loc2.getX();
        double dz = loc1.getZ() - loc2.getZ();
        return dx * dx + dz * dz;
    }
}
