package io.github.pigaut.rpg.config.attribute.legacy;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.map.*;
import org.bukkit.attribute.*;
import org.jetbrains.annotations.*;

public class AttributeMapperLegacy implements ConfigMapper.Line<CustomAttribute> {

    public AttributeMapperLegacy() {
        if (!Reflect.onClass(AttributeModifier.class).matchMethod("getSlot")) {
            throw new IllegalStateException("Expected AttributeModifier#getSlot() method was not found");
        }
    }

    @Override
    public void mapToLine(@NotNull ConfigLine line, @NotNull CustomAttribute itemAttribute) {
        final AttributeModifier modifier = itemAttribute.modifier();
        line.set(0, itemAttribute.type());
        line.set(1, modifier.getAmount());
        line.setFlag("operation", AttributeOperation.of(modifier.getOperation()));
        line.setFlag("slot", Reflect.on(modifier).call("getSlot").get());
        line.setFlag("name", modifier.getName());
    }

    @Override
    public void mapToSection(@NotNull ConfigSection section, @NotNull CustomAttribute itemAttribute) {
        final AttributeModifier modifier = itemAttribute.modifier();
        section.set("type|type", itemAttribute.type());
        section.set("amount", modifier.getAmount());
        section.set("slot", Reflect.on(modifier).call("getSlot").get());
        section.set("operation", modifier.getOperation());
        section.set("name", modifier.getName());
    }

}
