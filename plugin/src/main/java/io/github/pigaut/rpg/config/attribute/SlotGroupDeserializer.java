package io.github.pigaut.rpg.config.attribute;

import io.github.pigaut.yaml.configurator.convert.deserialize.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.inventory.EquipmentSlotGroup;

public class SlotGroupDeserializer implements Deserializer<EquipmentSlotGroup> {

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public EquipmentSlotGroup deserialize(String slotName) throws StringParseException {
        final EquipmentSlotGroup slotGroup = EquipmentSlotGroup.getByName(CaseFormatter.toSnakeCase(slotName));

        if (slotGroup == null) {
            throw new StringParseException("Could not find equipment slot with name: " + slotName);
        }

        return slotGroup;
    }

}
