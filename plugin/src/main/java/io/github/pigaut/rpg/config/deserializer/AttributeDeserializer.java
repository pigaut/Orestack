package io.github.pigaut.rpg.config.deserializer;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.yaml.configurator.convert.deserialize.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.attribute.*;
import org.jetbrains.annotations.*;

public class AttributeDeserializer implements Deserializer<Attribute> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid type";
    }

    @Override
    public @NotNull Attribute deserialize(@NotNull String attributeName) throws StringParseException {
        Attribute attribute = AttributeUtil.getAttribute(attributeName);
        if (attribute == null) {
            throw new StringParseException("Could not find attribute with name: " + attributeName);
        }
        return attribute;
    }

}
