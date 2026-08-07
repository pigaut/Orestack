package io.github.pigaut.rpg.module.function.condition.server;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;

public class OnlinePlayersCondition implements ServerCondition {

    private final Amount amount;

    public OnlinePlayersCondition(Amount amount) {
        this.amount = amount;
    }

    @Override
    public Boolean evaluate() {
        return amount.match(Bukkit.getOnlinePlayers().size());
    }

}
