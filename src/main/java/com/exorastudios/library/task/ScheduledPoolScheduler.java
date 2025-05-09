package com.exorastudios.library.task;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public class ScheduledPoolScheduler implements ExoScheduler.ScheduledThreadPool {

    private final ScheduledExecutorService scheduler;
    private final Plugin plugin;
    private final String name;
    private final String identifier;

    public ScheduledPoolScheduler(@NotNull Plugin plugin, @NotNull String name, int threads, @NotNull String identifier) {
        this.plugin = plugin;
        this.name = name;
        this.identifier = identifier;
        this.scheduler = Executors.newScheduledThreadPool(threads, r -> new Thread(r, name));
    }

    @Override
    public void execute(@NotNull Runnable task) {
        scheduler.execute(wrap(task));
    }

    @Override
    public Future<?> submit(@NotNull Runnable task) {
        return scheduler.submit(wrap(task));
    }

    @Override
    public <T> Future<T> submit(@NotNull Runnable task, @NotNull T result) {
        return scheduler.submit(wrap(task), result);
    }

    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        return scheduler.submit(wrap(task));
    }

    @Override
    public ScheduledFuture<?> schedule(@NotNull Runnable task, long delay, @NotNull TimeUnit unit) {
        return scheduler.schedule(wrap(task), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(@NotNull Callable<V> task, long delay, @NotNull TimeUnit unit) {
        return scheduler.schedule(wrap(task), delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NotNull Runnable task, long initialDelay, long period, @NotNull TimeUnit unit) {
        return scheduler.scheduleAtFixedRate(wrap(task), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NotNull Runnable task, long initialDelay, long delay, @NotNull TimeUnit unit) {
        return scheduler.scheduleWithFixedDelay(wrap(task), initialDelay, delay, unit);
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
    }

    @Override
    public void shutdownNow() {
        scheduler.shutdownNow();
    }

    public @NotNull String identifier() {
        return identifier;
    }

    private @NotNull Runnable wrap(@NotNull Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                plugin.getLogger().warning("Task in scheduled pool '%s' failed: %s".formatted(name, e.getMessage()));
            }
        };
    }

    private <T> @NotNull Callable<T> wrap(@NotNull Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception e) {
                plugin.getLogger().warning("Task in scheduled pool '%s' failed: %s".formatted(name, e.getMessage()));
                throw e;
            }
        };
    }
}