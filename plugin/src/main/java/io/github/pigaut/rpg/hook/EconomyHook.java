package io.github.pigaut.rpg.hook;

import io.github.pigaut.rpg.server.Server;
import net.milkbowl.vault.economy.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

public class EconomyHook extends PluginHook {

    private final Economy economy;

    private EconomyHook(@NotNull Plugin plugin, @NotNull Economy economy) {
        super(plugin);
        this.economy = economy;
    }

    public static @Nullable EconomyHook newInstance() {
        Plugin plugin = Server.getPlugin("Vault");
        if (plugin == null) {
            return null;
        }
        Economy economy = Server.getRegisteredService(Economy.class);
        if (economy == null) {
            return null;
        }
        return new EconomyHook(plugin, economy);
    }

    public double getBalance(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    public EconomyResponse depositMoney(OfflinePlayer player, double amount) {
        return economy.depositPlayer(player, amount);
    }

    public EconomyResponse withdrawMoney(OfflinePlayer player, double amount) {
        return economy.withdrawPlayer(player, amount);
    }

}
