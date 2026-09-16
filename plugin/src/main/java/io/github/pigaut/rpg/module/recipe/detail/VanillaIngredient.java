package io.github.pigaut.rpg.module.recipe.detail;

import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class VanillaIngredient implements Ingredient {

    private final EnumSet<Material> materials;
    private final int amount;

    public VanillaIngredient(@NotNull Collection<Material> materials, int amount) {
        Preconditions.checkArgument(!materials.isEmpty(), "Materials cannot be empty");
        this.materials = EnumSet.copyOf(materials);
        this.amount = amount;
    }

    public VanillaIngredient(@NotNull Material material, int amount) {
        this(EnumSet.of(material), amount);
    }

    @Override
    public int match(@NotNull ItemStack item) {
        if (!materials.contains(item.getType())) {
            return -1;
        }

        if (item.getAmount() < amount) {
            return -1;
        }

        return amount;
    }

    @Override
    public List<Material> getMaterialChoices() {
        return new ArrayList<>(materials);
    }

}