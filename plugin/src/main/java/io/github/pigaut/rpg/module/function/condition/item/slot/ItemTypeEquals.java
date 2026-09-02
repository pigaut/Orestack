package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemTypeEquals implements ItemPredicate {

    private final Material[] materials;

    public ItemTypeEquals(@NotNull Collection<Material> materials) {
        this.materials = materials.toArray(new Material[0]);
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        Material toolType = item.getType();
        for (Material material : materials) {
            if (material == toolType) {
                return true;
            }
        }
        return false;
    }

}
