package com.exorastudios.library.task;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ExoScheduler {

    private static final Map<String, ThreadPool> poolThreads = new HashMap<>();
    private static BukkitScheduler bukkitScheduler;
    private static Plugin plugin;

    private ExoScheduler() {
        throw new IllegalStateException("Utility class");
    }

    public static @NotNull ThreadPool pool(@NotNull String identifier) {
        ThreadPool pool = poolThreads.get(identifier);
        if (pool == null) {
            throw new IllegalStateException("Thread pool '%s' not initialized. Use Builder.executor() or Builder.scheduler().".formatted(identifier));
        }
        return pool;
    }

    public static @NotNull ScheduledThreadPool scheduled(@NotNull String identifier) {
        ThreadPool pool = poolThreads.get(identifier);
        if (!(pool instanceof ScheduledThreadPool scheduledPool)) {
            throw new IllegalStateException("Scheduled thread pool '%s' not initialized. Use Builder.scheduler().".formatted(identifier));
        }
        return scheduledPool;
    }

    public static @NotNull BukkitScheduler bukkit() {
        if (bukkitScheduler == null) {
            throw new IllegalStateException("Bukkit scheduler not initialized. Call Builder.executor() or Builder.scheduler().");
        }
        return bukkitScheduler;
    }

    public interface ThreadPool {
        void execute(@NotNull Runnable task);
        Future<?> submit(@NotNull Runnable task);
        <T> Future<T> submit(@NotNull Runnable task, @NotNull T result);
        <T> Future<T> submit(@NotNull Callable<T> task);
        void shutdown();
        void shutdownNow();
    }

    public interface ScheduledThreadPool extends ThreadPool {
        ScheduledFuture<?> schedule(@NotNull Runnable task, long delay, @NotNull TimeUnit unit);
        <V> ScheduledFuture<V> schedule(@NotNull Callable<V> task, long delay, @NotNull TimeUnit unit);
        ScheduledFuture<?> scheduleAtFixedRate(@NotNull Runnable task, long initialDelay, long period, @NotNull TimeUnit unit);
        ScheduledFuture<?> scheduleWithFixedDelay(@NotNull Runnable task, long initialDelay, long delay, @NotNull TimeUnit unit);
    }

    public static class Builder {
        private final Plugin plugin;
        private String name;
        private String identifier;
        private int threads = 1;
        private boolean isScheduled;

        private Builder(@NotNull Plugin plugin) {
            this.plugin = plugin;
        }

        public static @NotNull Builder executor(@NotNull Plugin plugin) {
            ExoScheduler.plugin = plugin;
            return new Builder(plugin).executor();
        }

        public static @NotNull Builder scheduler(@NotNull Plugin plugin) {
            ExoScheduler.plugin = plugin;
            return new Builder(plugin).scheduler();
        }

        public @NotNull Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public @NotNull Builder identifier(@NotNull String identifier) {
            this.identifier = identifier;
            return this;
        }

        public @NotNull Builder threads(int threads) {
            if (threads < 1) {
                throw new IllegalArgumentException("Threads must be at least 1");
            }
            if (name == null || identifier == null) {
                throw new IllegalStateException("Name and identifier must be set before threads");
            }
            if (poolThreads.containsKey(identifier)) {
                throw new IllegalArgumentException("Thread pool with identifier '%s' already exists".formatted(identifier));
            }
            ThreadPool pool = isScheduled
                    ? new ScheduledPoolScheduler(plugin, name, threads, identifier)
                    : new PoolScheduler(plugin, name, threads, identifier);
            poolThreads.put(identifier, pool);
            this.name = null;
            this.identifier = null;
            this.threads = 1;
            return this;
        }

        public @NotNull Builder executor() {
            this.isScheduled = false;
            return this;
        }

        public @NotNull Builder scheduler() {
            this.isScheduled = true;
            return this;
        }

        public void build() {
            if (plugin == null) {
                throw new IllegalStateException("Plugin not initialized. Call executor() or scheduler() with a plugin.");
            }
            if (bukkitScheduler == null) {
                bukkitScheduler = new BukkitScheduler(plugin);
            }
        }
    }
}