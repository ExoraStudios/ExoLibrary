package com.exorastudios.library.task;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PoolScheduler implements ExoScheduler.ThreadPool {

    private final ExecutorService executor;
    private final Plugin plugin;
    private final String name;
    private final String identifier;

    public PoolScheduler(@NotNull Plugin plugin, @NotNull String name, int threads, @NotNull String identifier) {
        this.plugin = plugin;
        this.name = name;
        this.identifier = identifier;
        this.executor = Executors.newFixedThreadPool(threads, r -> new Thread(r, name));
    }

    @Override
    public void execute(@NotNull Runnable task) {
        executor.execute(wrap(task));
    }

    @Override
    public Future<?> submit(@NotNull Runnable task) {
        return executor.submit(wrap(task));
    }

    @Override
    public <T> Future<T> submit(@NotNull Runnable task, @NotNull T result) {
        return executor.submit(wrap(task), result);
    }

    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        return executor.submit(wrap(task));
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public void shutdownNow() {
        executor.shutdownNow();
    }

    public @NotNull String identifier() {
        return identifier;
    }

    private @NotNull Runnable wrap(@NotNull Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                plugin.getLogger().warning("Task in pool '%s' failed: %s".formatted(name, e.getMessage()));
                e.printStackTrace();
            }
        };
    }

    private <T> @NotNull Callable<T> wrap(@NotNull Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception e) {
                plugin.getLogger().warning("Task in pool '%s' failed: %s".formatted(name, e.getMessage()));
                e.printStackTrace();
                throw e;
            }
        };
    }
}