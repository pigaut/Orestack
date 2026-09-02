package io.github.pigaut.rpg.module.function.condition.item.slot;

import com.ssomar.score.api.executableitems.*;
import com.ssomar.score.api.executableitems.config.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemIsExecutableItem implements ItemPredicate {

    private final String id;

    public ItemIsExecutableItem(@NotNull String id) {
        this.id = id;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        ExecutableItemInterface executableItem = ExecutableItemsAPI.getExecutableItemsManager()
                .getExecutableItem(item).orElse(null);
        if (executableItem == null) {
            return false;
        }

        return id.equalsIgnoreCase(executableItem.getId());
    }

}
