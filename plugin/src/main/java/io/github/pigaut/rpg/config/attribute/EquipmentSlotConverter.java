package io.github.pigaut.rpg.config.attribute;

import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.configurator.convert.*;
import org.bukkit.inventory.*;

public class EquipmentSlotConverter extends EnumConverter<EquipmentSlot> {

    public EquipmentSlotConverter() {
        super(EquipmentSlot.class);
        addAlias(EquipmentSlot.HAND, "mainhand", "any");
        addReplacement(EquipmentSlot.HAND, "mainhand");

        addAlias(EquipmentSlot.OFF_HAND, "offhand");
        addReplacement(EquipmentSlot.OFF_HAND, "offhand");

        if (Server.getVersion() >= Version.V1_20_5) {
            addAlias(EquipmentSlot.BODY, "armor");
            addReplacement(EquipmentSlot.BODY, "armor");
        }
    }

}
