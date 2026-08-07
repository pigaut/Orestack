package io.github.pigaut.rpg.config.attribute;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

// Attribute Loader for 1.21.3+
public class AttributeLoader implements ConfigLoader.Line<CustomAttribute> {

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid attribute modifier";
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull CustomAttribute loadFromLine(ConfigLine line) throws InvalidConfigException {
        Attribute attribute = line.getRequired(0, Attribute.class);

        var namespacedKey = NamespacedKey.fromString(line.getString("name").withDefault(UUID.randomUUID().toString()));
        var amount = line.getRequiredDouble(1);
        var operation = line.get("operation", AttributeOperation.class).withDefault(AttributeOperation.ADD_VALUE);
        var slot = line.get("slot", EquipmentSlotGroup.class).withDefault(EquipmentSlotGroup.HAND);
        AttributeModifier modifier = new AttributeModifier(namespacedKey, amount, operation.getOperation(), slot);

        return new CustomAttribute(attribute, modifier);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull CustomAttribute loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Attribute attribute = section.getRequired("type|type", Attribute.class);

        var namespacedKey = NamespacedKey.fromString(section.getString("name").withDefault(UUID.randomUUID().toString()));
        var amount = section.getRequiredDouble("amount");
        var operation = section.get("operation", AttributeOperation.class).withDefault(AttributeOperation.ADD_VALUE);
        var slot = section.get("slot", EquipmentSlotGroup.class).withDefault(EquipmentSlotGroup.HAND);
        AttributeModifier modifier = new AttributeModifier(namespacedKey, amount, operation.getOperation(), slot);

        return new CustomAttribute(attribute, modifier);
    }

}
