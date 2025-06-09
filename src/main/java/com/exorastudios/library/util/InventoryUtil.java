package com.exorastudios.library.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class InventoryUtil {

    private InventoryUtil() {}


    public static boolean hasItem(Player player, ItemStack item) {
        if (player == null || item == null) return false;
        return player.getInventory().containsAtLeast(item, item.getAmount());
    }


    public static boolean removeItem(Player player, ItemStack item) {
        if (player == null || item == null) return false;

        Inventory inv = player.getInventory();
        int toRemove = item.getAmount();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack != null && stack.isSimilar(item)) {
                int stackAmount = stack.getAmount();
                if (stackAmount <= toRemove) {
                    inv.clear(i);
                    toRemove -= stackAmount;
                } else {
                    stack.setAmount(stackAmount - toRemove);
                    inv.setItem(i, stack);
                    toRemove = 0;
                }

                if (toRemove <= 0) return true;
            }
        }

        return toRemove <= 0;
    }


    public static boolean addItem(Player player, ItemStack item) {
        if (player == null || item == null) return false;
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        return leftovers.isEmpty();
    }


    public static int countItem(Player player, ItemStack item) {
        if (player == null || item == null) return 0;
        int count = 0;
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack != null && stack.isSimilar(item)) {
                count += stack.getAmount();
            }
        }
        return count;
    }


    public static boolean isFull(Player player) {
        if (player == null) return false;
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack == null || stack.getType().isAir()) {
                return false;
            }
        }
        return true;
    }


    public static int firstSlotOfItem(Player player, ItemStack item) {
        if (player == null || item == null) return -1;
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack != null && stack.isSimilar(item)) return i;
        }
        return -1;
    }


    public static boolean containsSimilarItem(Player player, ItemStack item) {
        if (player == null || item == null) return false;
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack != null && stack.isSimilar(item)) {
                return true;
            }
        }
        return false;
    }


    public static int countFreeSlots(Player player) {
        if (player == null) return 0;
        int free = 0;
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack == null || stack.getType().isAir()) free++;
        }
        return free;
    }


    public static void clearItem(Player player, ItemStack item) {
        if (player == null || item == null) return;
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack != null && stack.isSimilar(item)) {
                inv.clear(i);
            }
        }
    }


    public static boolean transferItem(Player from, Player to, ItemStack item) {
        if (from == null || to == null || item == null) return false;

        int available = countItem(from, item);
        if (available < item.getAmount()) return false;

        if (isFull(to)) return false;

        boolean removed = removeItem(from, item);
        if (!removed) return false;

        boolean added = addItem(to, item);
        if (!added) {

            addItem(from, item);
            return false;
        }
        return true;
    }

    public static ItemStack cloneItem(ItemStack item) {
        if (item == null) return null;
        return item.clone();
    }
}
