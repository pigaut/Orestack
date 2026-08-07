package io.github.pigaut.rpg.bukkit.attribute;

import org.bukkit.attribute.*;

public class Attributes {

    public static final Attribute MAX_HEALTH = AttributeUtil.getAttributeOrThrow("MAX_HEALTH");
    public static final Attribute MOVEMENT_SPEED = AttributeUtil.getAttributeOrThrow("MOVEMENT_SPEED");
    public static final Attribute KNOCKBACK_RESISTANCE = AttributeUtil.getAttributeOrThrow("KNOCKBACK_RESISTANCE");
    public static final Attribute ATTACK_DAMAGE = AttributeUtil.getAttributeOrThrow("ATTACK_DAMAGE");
    public static final Attribute ATTACK_SPEED = AttributeUtil.getAttributeOrThrow("ATTACK_SPEED");
    public static final Attribute ARMOR = AttributeUtil.getAttributeOrThrow("ARMOR");
    public static final Attribute ARMOR_TOUGHNESS = AttributeUtil.getAttributeOrThrow("ARMOR_TOUGHNESS");

    // 1.21+
    public static final Attribute MINING_EFFICIENCY = AttributeUtil.getAttribute("MINING_EFFICIENCY");
    public static final Attribute MINING_SPEED = AttributeUtil.getAttribute("BLOCK_BREAK_SPEED");
    public static final Attribute SUBMERGED_MINING_SPEED = AttributeUtil.getAttribute("SUBMERGED_MINING_SPEED");

}
