package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlayerHasMoney implements PlayerCondition.Predicate {

    private final EconomyHook economy;
    private final Amount amount;

    public PlayerHasMoney(@NotNull EconomyHook economy, Amount amount) {
        this.economy = economy;
        this.amount = amount;
    }

    @Override
    public boolean test(@NotNull Player player) {
        return amount.match(economy.getBalance(player));
    }

}
