package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GiveItemToAttackers implements MobAction {

    private final EnhancedPlugin plugin;
    private final ItemStack item;
    private final Amount amount;

    public GiveItemToAttackers(EnhancedPlugin plugin, ItemStack item, Amount amount) {
        this.plugin = plugin;
        this.item = item;
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        for (Player player : mob.getAttackers()) {
            ItemStack drop = item.clone();
            drop.setAmount(amount.intValue());

            PlayerUtil.giveItemsOrDrop(player, drop);
        }
    }

}
