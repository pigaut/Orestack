package io.github.pigaut.rpg.module.function.condition.server;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class OnlinePlayersCondition implements ServerCondition.Predicate {

    private final Amount amount;

    public OnlinePlayersCondition(@NotNull Amount amount) {
        this.amount = amount;
    }

    @Override
    public boolean test() {
        return amount.match(Bukkit.getOnlinePlayers().size());
    }

}
