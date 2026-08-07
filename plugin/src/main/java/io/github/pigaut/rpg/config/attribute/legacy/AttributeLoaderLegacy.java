package io.github.pigaut.rpg.config.attribute.legacy;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.attribute.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

//Attribute loader for <1.21.3
public class AttributeLoaderLegacy implements ConfigLoader.Line<CustomAttribute> {

    private final Reflect attributeReflection = Reflect.onClass(AttributeModifier.class);

    public AttributeLoaderLegacy() {
        if (!Reflect.onClass(AttributeModifier.class)
                .matchConstructor(UUID.class, String.class, double.class, AttributeModifier.Operation.class, EquipmentSlot.class)) {
            throw new IllegalStateException("Expected AttributeModifier(UUID, String, double, Operation, EquipmentSlot) constructor was not found");
        }
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid item type modifier";
    }

    @Override
    @SuppressWarnings("")
    public @NotNull CustomAttribute loadFromLine(ConfigLine line) throws InvalidConfigException {
        Attribute attribute = line.getRequired(0, Attribute.class);

        var name = line.getString("name").withDefault("");
        var amount = line.getRequiredDouble(1);
        var operation = line.get("operation", AttributeOperation.class).withDefault(AttributeOperation.ADD_VALUE);
        var slot = line.get("slot", EquipmentSlot.class).withDefault(EquipmentSlot.HAND);
        AttributeModifier modifier = attributeReflection.create(UUID.randomUUID(), name, amount, operation.getOperation(), slot).get();

        return new CustomAttribute(attribute, modifier);
    }

    @Override
    public @NotNull CustomAttribute loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Attribute attribute = section.getRequired("type|type", Attribute.class);

        var name = section.getString("name").withDefault("");
        var amount = section.getRequiredDouble("amount");
        var operation = section.get("operation", AttributeOperation.class).withDefault(AttributeOperation.ADD_VALUE);
        var slot = section.get("slot", EquipmentSlot.class).withDefault(EquipmentSlot.HAND);
        AttributeModifier modifier = attributeReflection.create(UUID.randomUUID(), name, amount, operation.getOperation(), slot).get();

        return new CustomAttribute(attribute, modifier);
    }

}
