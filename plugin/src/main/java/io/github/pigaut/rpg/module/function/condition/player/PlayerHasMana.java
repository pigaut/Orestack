package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class PlayerHasMana implements PlayerStateCondition.Predicate {

    private final Amount amount;

    public PlayerHasMana(Amount amount) {
        this.amount = amount;
    }

    @Override
    public boolean test(@NotNull PlayerState playerState) {
        return amount.match(playerState.getMana());
    }

}
