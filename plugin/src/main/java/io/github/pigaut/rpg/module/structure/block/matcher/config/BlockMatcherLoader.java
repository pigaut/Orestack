package io.github.pigaut.rpg.module.structure.block.matcher.config;

import com.nexomc.nexo.api.*;
import dev.lone.itemsadder.api.*;
import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.rpg.hook.craftengine.*;
import io.github.pigaut.rpg.hook.itemsadder.*;
import io.github.pigaut.rpg.hook.nexo.*;
import io.github.pigaut.rpg.module.structure.block.matcher.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import io.github.pigaut.yaml.node.scalar.*;
import net.momirealms.craftengine.core.block.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.regex.*;

public class BlockMatcherLoader implements ConfigLoader<BlockMatcher> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid block";
    }

    // Format: "stone", "items-adder:block_name"
    @Override
    public @NotNull BlockMatcher loadFromScalar(@NotNull ConfigScalar scalar) throws InvalidConfigException {
        MaterialTag materialTag = scalar.get(MaterialTag.class)
                .orElse(null);

        if (materialTag != null) {
            List<Material> materials = new ArrayList<>(materialTag.getMaterials());
            if (materials.size() == 1) {
                return new MaterialBlockMatcher(materials.get(0));
            } else {
                return new MaterialBlockMatcher.Multi(materials);
            }
        }

        ConfigLine definition = scalar.toLine(LineStyle.COLON);

        String identifier = definition.getRequiredString(0);
        if (StringUtil.isAnyEqualIgnoreCase(identifier, "ItemsAdder", "items-adder", "ia-block", "ia")) {
            if (!Server.isPluginEnabled("ItemsAdder")) {
                scalar.collectWarning(scalar, "ItemsAdder plugin is not installed");
                return BlockMatcher.INVALID;
            }

            dev.lone.itemsadder.api.CustomBlock customBlock = definition.getRequired(1, dev.lone.itemsadder.api.CustomBlock.class);
            return new ItemsAdderBlockTemplate(customBlock);
        }

        if (StringUtil.isAnyEqualIgnoreCase(identifier, "CraftEngine", "craft-engine", "ce-block", "ce")) {
            if (!Server.isPluginEnabled("CraftEngine")) {
                scalar.collectWarning(scalar, "CraftEngine plugin is not installed");
                return BlockMatcher.INVALID;
            }

            BlockDefinition customBlock = definition.getRequired(1, BlockDefinition.class);
            return new CraftEngineBlockTemplate(customBlock);
        }

        if (StringUtil.isAnyEqualIgnoreCase(identifier, "Nexo", "nexo", "nx-block", "nx")) {
            if (!Server.isPluginEnabled("Nexo")) {
                scalar.collectWarning(scalar, "Nexo plugin is not installed");
                return BlockMatcher.INVALID;
            }

            BlockDefinition customBlock = definition.getRequired(1, BlockDefinition.class);
            return new CraftEngineBlockTemplate(customBlock);
        }

        throw new InvalidConfigException(scalar, "Could not determine block type");
    }

}
