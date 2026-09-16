package io.github.pigaut.rpg.module.recipe.detail;

import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CustomIngredient implements Ingredient {

    private final EnhancedPlugin plugin;

    private final Material material;
    private final String name;
    private final int amount;

    public CustomIngredient(@NotNull EnhancedPlugin plugin, @NotNull Material material, @NotNull String name, int amount) {
        this.plugin = plugin;
        this.material = material;
        this.name = name;
        this.amount = amount;
    }

    @Override
    public int match(@NotNull ItemStack item) {
        if (item.getType() != material) {
            return -1;
        }

        String itemName = plugin.getItemTemplates().getName(item);
        if (itemName == null) {
            return -1;
        }

        if (!itemName.equals(name)) {
            return -1;
        }

        if (item.getAmount() < amount) {
            return -1;
        }

        return amount;
    }

    @Override
    public List<Material> getMaterialChoices() {
        return List.of(material);
    }

}