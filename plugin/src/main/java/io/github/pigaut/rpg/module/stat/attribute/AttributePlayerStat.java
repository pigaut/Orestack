package io.github.pigaut.rpg.module.stat.attribute;

import io.github.pigaut.rpg.module.stat.*;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AttributePlayerStat extends PlayerStat {

    private final UUID playerId;
    private final Attribute attribute;
    private final NamespacedKey key;
    private final double multiplier;

    public AttributePlayerStat(@NotNull UUID playerId, @NotNull Attribute attribute, @NotNull NamespacedKey key) {
        this(playerId, attribute, key, 1.0);
    }

    public AttributePlayerStat(@NotNull UUID playerId, @NotNull Attribute attribute, @NotNull NamespacedKey key, double multiplier) {
        this.playerId = playerId;
        this.attribute = attribute;
        this.key = key;
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    private void sync() {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            return;
        }

        AttributeInstance attribute = player.getAttribute(this.attribute);
        if (attribute == null) {
            return;
        }

        attribute.removeModifier(key);

        double total = super.getTotal() / multiplier; // convert nice value back to Minecraft's raw scale
        if (total != 0) {
            attribute.addModifier(new AttributeModifier(key, total, AttributeModifier.Operation.ADD_NUMBER));
        }
    }

    @Override
    public void setEquipment(@NotNull EquipmentSlot slot, double value) {
        super.setEquipment(slot, value);
        sync();
    }

    @Override
    public void setEquipment(@NotNull EquipmentSlot slot, double value, @NotNull StatOperation type) {
        super.setEquipment(slot, value, type);
        sync();
    }

    @Override
    public void setEquipmentBuff(@NotNull EquipmentSlot slot, double value) {
        super.setEquipmentBuff(slot, value);
        sync();
    }

    @Override
    public void setEquipmentBuff(@NotNull EquipmentSlot slot, double value, @NotNull StatOperation type) {
        super.setEquipmentBuff(slot, value, type);
        sync();
    }

    @Override
    public void setHandBuff(double handBuff) {
        super.setHandBuff(handBuff);
        sync();
    }

    @Override
    public void setHandBuff(double handBuff, @NotNull StatOperation type) {
        super.setHandBuff(handBuff, type);
        sync();
    }

    @Override
    public void setOffHandBuff(double offHandBuff) {
        super.setOffHandBuff(offHandBuff);
        sync();
    }

    @Override
    public void setOffHandBuff(double offHandBuff, @NotNull StatOperation type) {
        super.setOffHandBuff(offHandBuff, type);
        sync();
    }

    @Override
    public void setHeadBuff(double headBuff) {
        super.setHeadBuff(headBuff);
        sync();
    }

    @Override
    public void setHeadBuff(double headBuff, @NotNull StatOperation type) {
        super.setHeadBuff(headBuff, type);
        sync();
    }

    @Override
    public void setChestBuff(double chestBuff) {
        super.setChestBuff(chestBuff);
        sync();
    }

    @Override
    public void setChestBuff(double chestBuff, @NotNull StatOperation type) {
        super.setChestBuff(chestBuff, type);
        sync();
    }

    @Override
    public void setLegsBuff(double legsBuff) {
        super.setLegsBuff(legsBuff);
        sync();
    }

    @Override
    public void setLegsBuff(double legsBuff, @NotNull StatOperation type) {
        super.setLegsBuff(legsBuff, type);
        sync();
    }

    @Override
    public void setFeetBuff(double feetBuff) {
        super.setFeetBuff(feetBuff);
        sync();
    }

    @Override
    public void setFeetBuff(double feetBuff, @NotNull StatOperation type) {
        super.setFeetBuff(feetBuff, type);
        sync();
    }

    @Override
    public void clear() {
        super.clear();
        sync();
    }

    @Override
    public void setHand(double hand) {
        super.setHand(hand);
        sync();
    }

    @Override
    public void setHand(double hand, @NotNull StatOperation type) {
        super.setHand(hand, type);
        sync();
    }

    @Override
    public void setOffHand(double offHand) {
        super.setOffHand(offHand);
        sync();
    }

    @Override
    public void setOffHand(double offHand, @NotNull StatOperation type) {
        super.setOffHand(offHand, type);
        sync();
    }

    @Override
    public void setHead(double head) {
        super.setHead(head);
        sync();
    }

    @Override
    public void setHead(double head, @NotNull StatOperation type) {
        super.setHead(head, type);
        sync();
    }

    @Override
    public void setChest(double chest) {
        super.setChest(chest);
        sync();
    }

    @Override
    public void setChest(double chest, @NotNull StatOperation type) {
        super.setChest(chest, type);
        sync();
    }

    @Override
    public void setLegs(double legs) {
        super.setLegs(legs);
        sync();
    }

    @Override
    public void setLegs(double legs, @NotNull StatOperation type) {
        super.setLegs(legs, type);
        sync();
    }

    @Override
    public void setFeet(double feet) {
        super.setFeet(feet);
        sync();
    }

    @Override
    public void setFeet(double feet, @NotNull StatOperation type) {
        super.setFeet(feet, type);
        sync();
    }

    @Override
    public void setSkillBonus(@NotNull String source, double value) {
        super.setSkillBonus(source, value);
        sync();
    }

    @Override
    public void setSkillBonus(@NotNull String source, double value, @NotNull StatOperation type) {
        super.setSkillBonus(source, value, type);
        sync();
    }

    @Override
    public void removeSkillBonus(@NotNull String source) {
        super.removeSkillBonus(source);
        sync();
    }

    @Override
    public void setBoost(@NotNull String name, double value) {
        super.setBoost(name, value);
        sync();
    }

    @Override
    public void setBoost(@NotNull String name, double value, @NotNull StatOperation type) {
        super.setBoost(name, value, type);
        sync();
    }

    @Override
    public void removeBoost(@NotNull String name) {
        super.removeBoost(name);
        sync();
    }

    @Override
    public void setEvent(@NotNull String source, double value) {
        super.setEvent(source, value);
        sync();
    }

    @Override
    public void setEvent(@NotNull String source, double value, @NotNull StatOperation type) {
        super.setEvent(source, value, type);
        sync();
    }

    @Override
    public void removeEvent(@NotNull String source) {
        super.removeEvent(source);
        sync();
    }

}
