package com.exorastudios.library.file;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ExoFile {

    private ExoFile() {}

    public static File get(JavaPlugin plugin, String path) {
        File file = new File(plugin.getDataFolder(), path);

        if (!file.exists()) {
            if (file.getName().contains(".")) {
                file.getParentFile().mkdirs();
            } else {
                file.mkdirs();
            }
        }

        return file;
    }
}
