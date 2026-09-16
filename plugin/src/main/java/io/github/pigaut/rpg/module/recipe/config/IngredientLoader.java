package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class IngredientLoader implements ConfigLoader.Line<Ingredient> {

    private final EnhancedPlugin plugin;

    public IngredientLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid ingredient";
    }

    @Override
    public @NotNull LineStyle getLineStyle() {
        return LineStyle.SPACED;
    }

    @Override
    public @NotNull Ingredient loadFromLine(ConfigLine line) throws InvalidConfigException {
        boolean overrideAmount = line.valueCount() > 1;
        int amount = overrideAmount ? line.getRequiredInteger(0) : 1;

        ItemTemplate itemTemplate = line.get(overrideAmount ? 1 : 0, ItemTemplate.class).orElse(null);
        if (itemTemplate != null) {
            ItemStack item = itemTemplate.createItemStack(null);
            return new CustomIngredient(plugin, item.getType(), itemTemplate.getName(), amount);
        }

        MaterialGroup materialGroup = line.get(overrideAmount ? 1 : 0, MaterialGroup.class).orElse(null);
        if (materialGroup != null) {
            Set<Material> materials = materialGroup.getMaterials();
            for (Material material : materials) {
                if (MaterialUtil.isAir(material)) {
                    throw new InvalidConfigException(line, "Recipe ingredient cannot be air");
                }
            }
            return new VanillaIngredient(materials, amount);
        }

        throw new InvalidConfigException(line, "Ingredient must be either a material or a custom item");
    }

}
