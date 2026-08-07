package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class TakeItemFromPlayer implements PlayerAction {

    private final ItemStack item;
    private final Amount amount;

    public TakeItemFromPlayer(ItemStack item, Amount amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player) {
        PlayerUtil.takeItems(player, item, amount.intValue());
    }

}
