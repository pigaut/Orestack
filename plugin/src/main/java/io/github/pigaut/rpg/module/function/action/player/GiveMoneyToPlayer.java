package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class GiveMoneyToPlayer implements PlayerAction {

    private final EconomyHook economy;
    private final Amount amount;

    public GiveMoneyToPlayer(@NotNull EconomyHook economy, Amount amount) {
        this.economy = economy;
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player) {
        economy.depositMoney(player, amount.doubleValue());
    }

}
