package io.github.pigaut.rpg.module.function.action.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DamageItemDurability implements ItemAction {

    private final Amount amount;

    public DamageItemDurability(@NotNull Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull ItemStack item) {
        ItemUtil.damageItem(item, player, amount.intValue());
    }

}
