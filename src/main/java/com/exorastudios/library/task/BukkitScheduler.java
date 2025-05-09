package com.exorastudios.library.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BukkitScheduler {

    private final Plugin plugin;

    public BukkitScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    public void sync(@NotNull Runnable task) {
        Bukkit.getScheduler().runTask(plugin, wrap(task));
    }

    public void async(@NotNull Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, wrap(task));
    }

    public void syncLater(@NotNull Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, wrap(task), delay);
    }

    public void asyncLater(@NotNull Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, wrap(task), delay);
    }

    public void syncTimer(@NotNull Runnable task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(plugin, wrap(task), delay, period);
    }

    public void asyncTimer(@NotNull Runnable task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, wrap(task), delay, period);
    }

    @Contract(pure = true)
    private @NotNull Runnable wrap(@NotNull Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                plugin.getLogger().warning("Bukkit task failed: " + e.getMessage());
            }
        };
    }
}