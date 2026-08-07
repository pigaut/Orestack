package io.github.pigaut.rpg.config.attribute;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.map.*;
import org.bukkit.attribute.*;
import org.jetbrains.annotations.*;

// Attribute Mapper for 1.21.3+
public class AttributeMapper implements ConfigMapper.Line<CustomAttribute> {

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void mapToLine(@NotNull ConfigLine line, @NotNull CustomAttribute itemAttribute) {
        final AttributeModifier modifier = itemAttribute.modifier();
        line.set(0, itemAttribute.type());
        line.set(1, modifier.getAmount());
        line.setFlag("operation", AttributeOperation.of(modifier.getOperation()));
        line.setFlag("slot", modifier.getSlotGroup());
        line.setFlag("name", modifier.getName());
    }

    @Override
    public void mapToSection(@NotNull ConfigSection section, @NotNull CustomAttribute itemAttribute) {
        final AttributeModifier modifier = itemAttribute.modifier();
        section.set("type|type", itemAttribute.type());
        section.set("amount", modifier.getAmount());
        section.set("slot", modifier.getSlotGroup());
        section.set("operation", modifier.getOperation());
        section.set("name", modifier.getName());
    }

}
