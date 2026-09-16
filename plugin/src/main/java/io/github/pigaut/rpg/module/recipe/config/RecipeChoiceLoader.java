package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RecipeChoiceLoader implements ConfigLoader.Line<RecipeChoice> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid recipe choice";
    }

    @Override
    public @NotNull LineStyle getLineStyle() {
        return LineStyle.COMMA;
    }

    @Override
    public @NotNull RecipeChoice loadFromLine(ConfigLine line) throws InvalidConfigException {
        if (line.valueCount() > 1) {
            List<MaterialGroup> materialGroups = line.getAll(MaterialGroup.class).orElse(null);
            if (materialGroups != null) {
                List<Material> materials = MaterialGroup.toMaterialsList(materialGroups);
                return new RecipeChoice.MaterialChoice(materials);
            }

            List<ItemStack> items = line.getAllRequired(ItemStack.class);
            return new RecipeChoice.ExactChoice(items);
        }

        MaterialGroup materialGroup = line.get(0, MaterialGroup.class)
                .orElse(null);

        if (materialGroup != null) {
            List<Material> materials = new ArrayList<>(materialGroup.getMaterials());
            if (materials.stream().anyMatch(MaterialUtil::isAir)) {
                throw new InvalidConfigException(line, "Air cannot be used in recipes");
            }
            return new RecipeChoice.MaterialChoice(materials);
        } else {
            ItemStack item = line.getRequired(0, ItemStack.class);
            return new RecipeChoice.ExactChoice(item);
        }
    }

    @Override
    public @NotNull RecipeChoice loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        List<Material> materials = sequence.getAll(Material.class)
                .orElse(null);

        if (materials != null) {
            return new RecipeChoice.MaterialChoice(materials);
        } else {
            List<ItemStack> items = sequence.getAllRequired(ItemStack.class);
            return new RecipeChoice.ExactChoice(items);
        }
    }

}
