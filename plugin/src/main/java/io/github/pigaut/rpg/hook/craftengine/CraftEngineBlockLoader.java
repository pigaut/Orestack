package io.github.pigaut.rpg.hook.craftengine;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import net.momirealms.craftengine.bukkit.api.*;
import net.momirealms.craftengine.core.block.*;
import net.momirealms.craftengine.core.util.*;
import org.jetbrains.annotations.*;

public class CraftEngineBlockLoader implements ConfigLoader<BlockDefinition> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid CraftEngine block";
    }

    @Override
    public @NotNull BlockDefinition loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String rawKey = scalar.toString();
        String[] splitKey = rawKey.split(":");

        if (splitKey.length > 2) {
            throw new InvalidConfigException(scalar, "Expected a key but found: '" + rawKey + "'");
        }

        Key key = splitKey.length == 1 ? Key.withDefaultNamespace(splitKey[0], Key.CRAFTENGINE_NAMESPACE) : Key.of(splitKey);
        BlockDefinition blockDefinition = CraftEngineBlocks.byId(key);
        if (blockDefinition == null) {
            throw new InvalidConfigException(scalar, "Could not find CraftEngine block with name: '" + rawKey + "'");
        }
        return blockDefinition;
    }

}
