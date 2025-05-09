package com.exorastudios.library.config.accessor;

import com.exorastudios.library.config.ExoConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ConfigAccessor {

    private final String name;

    public ConfigAccessor(String name) {
        this.name = name;
    }

    public @NotNull ConfigAccessor reload() {
        ExoConfig.getCache().entrySet().removeIf(entry -> entry.getKey().startsWith(name + "."));
        ExoConfig.loadConfig(name);
        ExoConfig.info("Reloaded config: " + name);
        return this;
    }

    public @Nullable Object get(@NotNull String key) {
        return ExoConfig.getCache().get(name + "." + key);
    }

    public @NotNull Object get(@NotNull String key, @NotNull Object def) {
        Object value = get(key);
        return value != null ? value : def;
    }

    public @NotNull String String(@NotNull String key) {
        return getValue(key, String.class, "string");
    }

    public @NotNull String String(@NotNull String key, @NotNull String def) {
        Object value = get(key);
        return value instanceof String ? (String) value : def;
    }

    public @NotNull List<String> StringList(@NotNull String key) {
        Object value = getValue(key, List.class, "string list");
        return ((List<?>) value).stream().map(Object::toString).toList();
    }

    public int Int(@NotNull String key, int def) {
        Object value = get(key);
        return value instanceof Integer ? (Integer) value : def;
    }

    public double Double(@NotNull String key, double def) {
        Object value = get(key);
        return value instanceof Number ? ((Number) value).doubleValue() : def;
    }

    public boolean Boolean(@NotNull String key, boolean def) {
        Object value = get(key);
        return value instanceof Boolean ? (Boolean) value : def;
    }

    public @NotNull ConfigAccessor set(@NotNull String key, @NotNull Object value) {
        ExoConfig.getCache().put(name + "." + key, value);
        Optional.ofNullable(ExoConfig.getConfigs().get(name)).ifPresent(config -> {
            config.set(key, value);
            ExoConfig.saveConfig(name, config);
        });
        return this;
    }

    private <T> T getValue(String key, Class<T> type, String typeName) {
        Object value = get(key);
        if (value == null) {
            throw new IllegalArgumentException("Key '%s' not found in config '%s'".formatted(key, name));
        }
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException("Key '%s' is not a %s in config '%s'".formatted(key, typeName, name));
        }
        return type.cast(value);
    }
}