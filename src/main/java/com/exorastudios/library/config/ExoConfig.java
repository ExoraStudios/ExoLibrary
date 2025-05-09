package com.exorastudios.library.config;

import com.exorastudios.library.config.accessor.ConfigAccessor;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public final class ExoConfig {

    @Getter private static final Map<String, Object> cache = new HashMap<>();
    @Getter private static final Map<String, FileConfiguration> configs = new HashMap<>();
    @Getter private static final Map<String, ConfigAccessor> accessors = new HashMap<>();

    private static Plugin plugin;

    private ExoConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static void of(@NotNull Plugin plugin) {
        plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");
    }

    public static void cache(@NotNull String configName, @NotNull String accessorName) {
        isLoaded();
        loadConfig(configName);
        accessors.put(accessorName.toUpperCase(Locale.ROOT), new ConfigAccessor(configName));
    }

    public static void reloadAll() {
        cache.clear();
        configs.keySet().forEach(ExoConfig::loadConfig);
        info("Reloaded all configs");
    }

    public static @NotNull ConfigAccessor get(@NotNull String name) {
        ConfigAccessor accessor = accessors.get(name.toUpperCase(Locale.ROOT));
        if (accessor == null) {
            throw new IllegalStateException("Accessor '%s' not initialized. Use Cache(\"<file>.yml\", \"%s\")".formatted(name, name));
        }
        return accessor;
    }

    public static void loadConfig(String configName) {
        try {
            File file = new File(plugin.getDataFolder(), configName);
            if (!file.exists()) {
                plugin.saveResource(configName, false);
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            flattenConfig(config, configName);
            configs.put(configName, config);
            info("Loaded config: " + configName);
        } catch (Exception e) {
            severe("Failed to load config: " + configName, e);
        }
    }

    public static void saveConfig(String configName, FileConfiguration config) {
        try {
            config.save(new File(plugin.getDataFolder(), configName));
            info("Saved config: " + configName);
        } catch (IOException e) {
            severe("Failed to save config: " + configName, e);
        }
    }

    private static void flattenConfig(FileConfiguration config, String configName) {
        for (String key : config.getKeys(true)) {
            if (!config.isConfigurationSection(key)) {
                cache.put(configName + "." + key, config.get(key));
            }
        }
    }

    private static void isLoaded() {
        if (plugin == null) {
            throw new IllegalStateException("ExoraConfig not initialized. Call initialize() first.");
        }
    }

    public static void info(String message) {
        plugin.getLogger().info(message);
    }

    private static void severe(String message, Throwable e) {
        plugin.getLogger().log(Level.SEVERE, message, e);
    }

    public static Builder builder(@NotNull Plugin plugin) {
        return new Builder(plugin);
    }

    public static class Builder {
        private final Plugin plugin;
        private final List<Pair> pairs = new ArrayList<>();

        private static class Pair {
            String config;
            String accessor;

            Pair(String config, String accessor) {
                this.config = config;
                this.accessor = accessor;
            }
        }

        public Builder(@NotNull Plugin plugin) {
            this.plugin = plugin;
        }

        public Builder config(@NotNull String configName) {
            pairs.add(new Pair(configName, configName));
            return this;
        }

        public Builder accessor(@NotNull String accessorName) {
            if (pairs.isEmpty()) {
                throw new IllegalStateException("Call config() before accessor()");
            }
            pairs.get(pairs.size() - 1).accessor = accessorName;
            return this;
        }

        public void build() {
            of(plugin);
            for (Pair pair : pairs) {
                cache(pair.config, pair.accessor);
            }
        }
    }
}
