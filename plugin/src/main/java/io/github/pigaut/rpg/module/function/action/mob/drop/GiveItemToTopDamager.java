package io.github.pigaut.rpg.module.function.action.mob.drop;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GiveItemToTopDamager implements MobAction.Executor {

    private final EnhancedPlugin plugin;
    private final ItemStack item;
    private final Amount amount;

    public GiveItemToTopDamager(EnhancedPlugin plugin, ItemStack item, Amount amount) {
        this.plugin = plugin;
        this.item = item;
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        Player attackerWithMostDamage = mob.getTopDamager();
        if (attackerWithMostDamage == null) {
            return;
        }

        ItemStack drop = item.clone();
        drop.setAmount(amount.intValue());

        PlayerUtil.giveItemsOrDrop(attackerWithMostDamage, drop);
    }

}
