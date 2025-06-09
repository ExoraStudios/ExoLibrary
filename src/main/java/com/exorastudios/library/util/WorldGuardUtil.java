package com.exorastudios.library.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class WorldGuardUtil {

    private WorldGuardUtil() {}

    private static RegionQuery getRegionQuery() {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
    }

    public static Set<String> getRegions(Player player) {
        if (player == null) return Collections.emptySet();
        Location loc = BukkitAdapter.adapt(player.getLocation());
        ApplicableRegionSet set = getRegionQuery().getApplicableRegions(loc);
        Set<String> regions = new HashSet<>();
        for (ProtectedRegion region : set) {
            regions.add(region.getId());
        }
        return regions;
    }


    public static boolean isInRegion(Player player, String regionId) {
        if (player == null || regionId == null) return false;
        return getRegions(player).stream()
                .anyMatch(r -> r.equalsIgnoreCase(regionId));
    }
}
