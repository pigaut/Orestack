package io.github.pigaut.rpg.module.function.condition.player.tool;

import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerToolHasCustomModel implements ToolCondition {

    private final List<Integer> validModels;

    public PlayerToolHasCustomModel(List<Integer> validModels) {
        this.validModels = validModels;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        if (!tool.hasItemMeta()) {
            return false;
        }
        int toolModel = tool.getItemMeta().getCustomModelData();
        for (Integer validModel : validModels) {
            if (toolModel == validModel) {
                return true;
            }
        }
        return false;
    }

}

