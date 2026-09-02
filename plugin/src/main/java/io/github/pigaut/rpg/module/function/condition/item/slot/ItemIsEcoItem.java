package io.github.pigaut.rpg.module.function.condition.item.slot;

import com.willfp.ecoitems.items.*;
import io.github.pigaut.rpg.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemIsEcoItem implements ItemPredicate {

    private final String name;

    public ItemIsEcoItem(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        EcoItem ecoItem = EcoItems.INSTANCE.getByID(name);
        if (ecoItem == null) {
            return false;
        }

        String ecoItemName = PersistentData.getString(item.getItemMeta(), ecoItem.getId());
        return ecoItemName.equalsIgnoreCase(name);
    }
}
