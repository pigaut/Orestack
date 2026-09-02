package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class TakeMoneyFromPlayer implements PlayerAction.Executor {

    private final EconomyHook economy;
    private final Amount amount;

    public TakeMoneyFromPlayer(@NotNull EconomyHook economy, Amount amount) {
        this.economy = economy;
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player) {
        economy.withdrawMoney(player, amount.doubleValue());
    }

}
