package io.github.pigaut.rpg.config.itemstack;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PluginItemStackLoader extends ItemStackLoader {

    private final ItemStackConverter deserializer;

    public PluginItemStackLoader(EnhancedPlugin plugin) {
        this.deserializer = new ItemStackConverter(plugin);
    }

    @Override
    public @NotNull ItemStack loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        try {
            return deserializer.deserialize(scalar.toString());
        } catch (StringParseException e) {
            throw new InvalidConfigException(scalar, e.getMessage());
        }
    }

}
