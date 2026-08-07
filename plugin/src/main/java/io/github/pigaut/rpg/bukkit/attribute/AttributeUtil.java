package io.github.pigaut.rpg.bukkit.attribute;

import org.bukkit.*;
import org.bukkit.attribute.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AttributeUtil {

    private static final Map<String, Attribute> ATTRIBUTES = new HashMap<>();

    static {
        registerAttribute("max_health", "generic.max_health");
        registerAttribute("follow_range", "generic.follow_range");
        registerAttribute("knockback_resistance", "generic.knockback_resistance");
        registerAttribute("movement_speed", "generic.movement_speed");
        registerAttribute("flying_speed", "generic.flying_speed");
        registerAttribute("attack_damage", "generic.attack_damage");
        registerAttribute("attack_knockback", "generic.attack_knockback");
        registerAttribute("attack_speed", "generic.attack_speed");
        registerAttribute("armor", "generic_armor", "generic.armor");
        registerAttribute("armor_toughness", "generic_armor_toughness", "generic.armor_toughness");
        registerAttribute("fall_damage_multiplier", "generic.fall_damage_multiplier");
        registerAttribute("luck", "generic.luck");
        registerAttribute("max_absorption", "generic.max_absorption");
        registerAttribute("safe_fall_distance", "generic.safe_fall_distance");
        registerAttribute("scale", "generic.scale");
        registerAttribute("step_height", "generic.step_height");
        registerAttribute("gravity", "generic.gravity");
        registerAttribute("jump_strength", "generic.jump_strength");
        registerAttribute("jump_strength", "horse.jump_strength");
        registerAttribute("burning_time", "generic.burning_time");
        registerAttribute("explosion_knockback_resistance", "generic.explosion_knockback_resistance");
        registerAttribute("movement_efficiency", "generic.movement_efficiency");
        registerAttribute("oxygen_bonus", "generic.oxygen_bonus");
        registerAttribute("water_movement_efficiency", "generic.water_movement_efficiency");
        registerAttribute("tempt_range", "generic.tempt_range");
        registerAttribute("block_interaction_range", "player.block_interaction_range");
        registerAttribute("entity_interaction_range", "player.entity_interaction_range");
        registerAttribute("block_break_speed", "player.block_break_speed");
        registerAttribute("mining_efficiency", "player.mining_efficiency");
        registerAttribute("sneaking_speed", "player.sneaking_speed");
        registerAttribute("submerged_mining_speed", "player.submerged_mining_speed");
        registerAttribute("sweeping_damage_ratio", "player.sweeping_damage_ratio");
        registerAttribute("spawn_reinforcements", "zombie.spawn_reinforcements");
        registerAttribute("camera_distance");
        registerAttribute("waypoint_transmit_range");
        registerAttribute("waypoint_receive_range");
    }

    public static Attribute getAttribute(@NotNull String name) {
        return ATTRIBUTES.get(name.toLowerCase(Locale.ENGLISH));
    }

    public static Attribute getAttributeOrThrow(@NotNull String name) {
        Attribute attribute = getAttribute(name);
        if (attribute == null) {
            throw new IllegalStateException("Could not find attribute with name: " + name);
        }
        return attribute;
    }

    private static void registerAttribute(String... names) {
        for (String name : names) {
            Attribute foundAttribute = Registry.ATTRIBUTE.get(NamespacedKey.minecraft(name));
            if (foundAttribute != null) {
                for (String id : names) {
                    ATTRIBUTES.put(id, foundAttribute);
                }
                return;
            }
        }
    }

}
