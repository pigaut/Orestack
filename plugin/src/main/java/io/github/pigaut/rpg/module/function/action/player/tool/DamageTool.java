package io.github.pigaut.rpg.module.function.action.player.tool;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DamageTool implements Action {

    private final Amount amount;

    public DamageTool(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        ItemStack tool = context.tool();
        if (player != null && tool != null) {
            ItemUtil.damageItem(tool, player, amount.intValue());
        }
    }

}
