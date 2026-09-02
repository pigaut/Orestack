package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class RemoveItemFromPlayer implements PlayerAction {

    private final ItemStack item;
    private final Amount amount;

    public RemoveItemFromPlayer(@NotNull ItemStack item, @NotNull Amount amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Player player) {
        if (!PlayerUtil.removeItem(player, item, amount.intValue())) {
            return new FunctionError("Could not remove all items from player inventory");
        }
        return FunctionResponse.NONE;
    }

}
