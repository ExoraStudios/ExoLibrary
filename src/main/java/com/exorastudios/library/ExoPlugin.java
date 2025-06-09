package com.exorastudios.library;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class ExoPlugin extends JavaPlugin {

    private PaperCommandManager acfCommandManager;

    @Override
    public final void onLoad() {
        load();
    }
    @Override
    public final void onEnable() {
        acfCommandManager = new PaperCommandManager(this);
        start();
    }

    @Override
    public final void onDisable() {
        stop();
    }

    protected void load() {}

    protected abstract void start();

    protected abstract void stop();

    @NotNull
    public final Server server() {
        return getServer();
    }

    @NotNull
    public final PluginManager plugin() {
        return getServer().getPluginManager();
    }

    public final void shutdown() {
        plugin().disablePlugin(this);
    }

    public final void listeners(@NotNull Listener... listeners) {
        for (Listener l : listeners) {
            plugin().registerEvents(l, this);
        }
    }

    public final void commands(@NotNull register... regster) {
        for (register b : regster) {
            if (b.executor() instanceof co.aikar.commands.BaseCommand) {

                acfCommandManager.registerCommand((co.aikar.commands.BaseCommand) b.executor());
            } else {
                PluginCommand cmd = getCommand(b.name());
                if (cmd != null) {
                    cmd.setExecutor(b.executor());
                } else {
                    getLogger().warning("Command '/" + b.name() + "' not found in plugin.yml.");
                }
            }
        }
    }

    public record register(String name, CommandExecutor executor) {}
}
