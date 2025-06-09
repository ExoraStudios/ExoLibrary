package com.exorastudios.library.util;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public final class EconomyUtil {

    private final Economy economy;

    public EconomyUtil() {
        RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);
        this.economy = (provider != null) ? provider.getProvider() : null;
    }

    public boolean isAvailable() {
        return economy != null;
    }

    public boolean withdraw(@NotNull OfflinePlayer player, double amount) {
        if (!isAvailable() || !economy.has(player, amount)) return false;
        EconomyResponse response = economy.withdrawPlayer(player, amount);
        return response.transactionSuccess();
    }

    public boolean deposit(@NotNull OfflinePlayer player, double amount) {
        if (!isAvailable()) return false;
        EconomyResponse response = economy.depositPlayer(player, amount);
        return response.transactionSuccess();
    }

    public double getBalance(@NotNull OfflinePlayer player) {
        if (!isAvailable()) return 0.0;
        return economy.getBalance(player);
    }
}
