package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
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
    public @NotNull FunctionResponse dispatch(@NotNull Player player) {
        if (!PlayerUtil.removeItem(player, item, amount.intValue())) {
            player.sendMessage(ChatColor.RED + "Could not remove all required items from player inventory.");
            return FunctionResponse.STOP;
        }
        return FunctionResponse.NONE;
    }

}
