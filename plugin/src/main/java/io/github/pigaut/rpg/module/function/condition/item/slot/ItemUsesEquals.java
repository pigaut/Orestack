package io.github.pigaut.rpg.module.function.condition.item.slot;

import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemUsesEquals implements ItemPredicate {

    private final EnhancedPlugin plugin;
    private final Amount uses;

    public ItemUsesEquals(@NotNull EnhancedPlugin plugin, @NotNull Amount uses) {
        this.plugin = plugin;
        this.uses = uses;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        Integer usesLeft = plugin.getItems().getUsesLeft(item);
        if (usesLeft == null) {
            return false;
        }

        return uses.match(usesLeft);
    }

}
