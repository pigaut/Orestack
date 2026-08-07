package io.github.pigaut.rpg.module.function.condition.player.tool;

import com.ssomar.score.api.executableitems.*;
import com.ssomar.score.api.executableitems.config.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerToolIsExecutableItem implements ToolCondition {

    private final String id;

    public PlayerToolIsExecutableItem(String id) {
        this.id = id;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        ExecutableItemInterface executableItem = ExecutableItemsAPI.getExecutableItemsManager()
                .getExecutableItem(tool).orElse(null);
        if (executableItem == null) {
            return false;
        }

        return id.equalsIgnoreCase(executableItem.getId());
    }

}
