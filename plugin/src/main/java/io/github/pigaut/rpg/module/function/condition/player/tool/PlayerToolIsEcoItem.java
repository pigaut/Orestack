package io.github.pigaut.rpg.module.function.condition.player.tool;

import com.willfp.ecoitems.items.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerToolIsEcoItem implements ToolCondition {

    private final String name;

    public PlayerToolIsEcoItem(String name) {
        this.name = name;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        if (!tool.hasItemMeta()) {
            return false;
        }

        EcoItem item = EcoItems.INSTANCE.getByID(name);
        if (item == null) {
            return false;
        }

        String ecoItemName = PersistentData.getString(tool.getItemMeta(), item.getId());
        return ecoItemName.equalsIgnoreCase(name);
    }
}
