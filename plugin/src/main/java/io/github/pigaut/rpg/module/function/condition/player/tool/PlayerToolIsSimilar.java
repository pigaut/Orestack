package io.github.pigaut.rpg.module.function.condition.player.tool;

import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerToolIsSimilar implements ToolCondition {

    private final List<ItemStack> items;

    public PlayerToolIsSimilar(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        for (ItemStack item : items) {
            if (item.isSimilar(tool)) {
                return true;
            }
        }
        return false;
    }

}
