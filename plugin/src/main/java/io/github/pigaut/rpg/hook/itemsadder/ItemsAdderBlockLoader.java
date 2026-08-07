package io.github.pigaut.rpg.hook.itemsadder;

import dev.lone.itemsadder.api.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ItemsAdderBlockLoader implements ConfigLoader<CustomBlock> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid ItemsAdder block";
    }

    @Override
    public @NotNull CustomBlock loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String blockName = scalar.toString();
        CustomBlock customBlock = CustomBlock.getInstance(blockName);
        if (customBlock == null) {
            throw new InvalidConfigException(scalar, "Could not find ItemsAdder block with name: '" + blockName + "'");
        }
        return customBlock;
    }

}
