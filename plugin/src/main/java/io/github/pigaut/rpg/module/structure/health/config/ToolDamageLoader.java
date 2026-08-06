package io.github.pigaut.rpg.module.structure.health.config;

import io.github.pigaut.rpg.module.structure.health.*;
import io.github.pigaut.rpg.module.structure.health.*;
import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ToolDamageLoader implements ConfigLoader<ToolDamage> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid tool damage";
    }

    @Override
    public @NotNull ToolDamage loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Set<Material> blocks = new HashSet<>();
        for (MaterialTag tag : section.getAllRequired("blocks", MaterialTag.class)) {
            for (Material material : tag.getMaterials()) {
                if (!material.isBlock()) {
                    throw new InvalidConfigException(section, "blocks", "Expected a block but found: " + material);
                }
                blocks.add(material);
            }
        }

        Map<Material, Amount> damageByTool = new HashMap<>();
        for (KeyedField field : section.getSectionOrCreate("tools").getNestedFields()) {
            Material tool = field.getKey(Material.class);
            Amount amount = field.getRequired(Amount.class);
            damageByTool.put(tool, amount);
        }

        return new ToolDamage(blocks, damageByTool);
    }

}
