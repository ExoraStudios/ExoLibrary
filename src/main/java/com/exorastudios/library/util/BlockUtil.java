package com.exorastudios.library.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

public final class BlockUtil {

    private BlockUtil() {}


    public static boolean isSolid(Block block) {
        if (block == null) return false;
        Material mat = block.getType();
        return mat.isSolid() && mat != Material.LAVA && mat != Material.WATER;
    }


    public static boolean isLiquid(Block block) {
        if (block == null) return false;
        Material mat = block.getType();
        return mat == Material.WATER || mat == Material.LAVA;
    }


    public static boolean isSafeBlock(Block block) {
        if (block == null) return false;
        return isSolid(block) && !isLiquid(block);
    }


    public static boolean isContainer(Block block) {
        if (block == null) return false;
        Material mat = block.getType();
        return mat.toString().endsWith("CHEST") 
            || mat.toString().endsWith("BARREL")
            || mat.toString().endsWith("SHULKER_BOX")
            || mat.toString().endsWith("HOPPER")
            || mat.toString().endsWith("DISPENSER")
            || mat.toString().endsWith("DROPPER")
            || mat == Material.FURNACE;
    }
}
