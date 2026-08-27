package io.github.pigaut.rpg.module.stat;

import io.github.pigaut.rpg.module.stat.modifier.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerStat {

    private final double base;

    private StatModifier hand = StatModifier.ZERO, handBuff = StatModifier.ZERO;
    private StatModifier offHand = StatModifier.ZERO, offHandBuff = StatModifier.ZERO;
    private StatModifier head = StatModifier.ZERO, headBuff = StatModifier.ZERO;
    private StatModifier chest = StatModifier.ZERO, chestBuff = StatModifier.ZERO;
    private StatModifier legs = StatModifier.ZERO, legsBuff = StatModifier.ZERO;
    private StatModifier feet = StatModifier.ZERO, feetBuff = StatModifier.ZERO;

    private final Map<String, StatModifier> buffs = new HashMap<>();
    private final Map<String, StatModifier> skills = new HashMap<>();
    private final Map<String, StatModifier> events = new HashMap<>();

    public PlayerStat() {
        this(0);
    }

    public PlayerStat(double base) {
        this.base = base;
    }

    public double getTotal() {
        List<StatModifier> modifiers = new ArrayList<>();
        modifiers.add(hand);
        modifiers.add(handBuff);
        modifiers.add(offHand);
        modifiers.add(offHandBuff);
        modifiers.add(head);
        modifiers.add(headBuff);
        modifiers.add(chest);
        modifiers.add(chestBuff);
        modifiers.add(legs);
        modifiers.add(legsBuff);
        modifiers.add(feet);
        modifiers.add(feetBuff);
        modifiers.addAll(buffs.values());
        modifiers.addAll(skills.values());
        modifiers.addAll(events.values());
        return StatUtil.calculateStatTotal(base, modifiers);
    }

    public double getDisplayTotal() {
        List<StatModifier> modifiers = new ArrayList<>();
        modifiers.add(hand);
        modifiers.add(offHand);
        modifiers.add(head);
        modifiers.add(chest);
        modifiers.add(legs);
        modifiers.add(feet);
        modifiers.addAll(skills.values());
        modifiers.addAll(events.values());
        return StatUtil.calculateStatTotal(base, modifiers);
    }

    public void setEquipment(@NotNull EquipmentSlot slot, double value) {
        setEquipment(slot, value, StatOperation.ADD);
    }

    public void setEquipment(@NotNull EquipmentSlot slot, double value, @NotNull StatOperation type) {
        StatModifier modifier = new StatModifier(value, type);
        switch (slot) {
            case HAND -> hand = modifier;
            case OFF_HAND -> offHand = modifier;
            case HEAD -> head = modifier;
            case CHEST -> chest = modifier;
            case LEGS -> legs = modifier;
            case FEET -> feet = modifier;
            default -> throw new IllegalArgumentException("Unsupported equipment slot: " + slot);
        }
    }

    public void setEquipmentBuff(@NotNull EquipmentSlot slot, double value) {
        setEquipmentBuff(slot, value, StatOperation.ADD);
    }

    public void setEquipmentBuff(@NotNull EquipmentSlot slot, double value, @NotNull StatOperation type) {
        StatModifier modifier = new StatModifier(value, type);
        switch (slot) {
            case HAND -> handBuff = modifier;
            case OFF_HAND -> offHandBuff = modifier;
            case HEAD -> headBuff = modifier;
            case CHEST -> chestBuff = modifier;
            case LEGS -> legsBuff = modifier;
            case FEET -> feetBuff = modifier;
            default -> throw new IllegalArgumentException("Unsupported equipment slot: " + slot);
        }
    }

    public void clear() {
        hand = StatModifier.ZERO;
        handBuff = StatModifier.ZERO;
        offHand = StatModifier.ZERO;
        offHandBuff = StatModifier.ZERO;
        head = StatModifier.ZERO;
        headBuff = StatModifier.ZERO;
        chest = StatModifier.ZERO;
        chestBuff = StatModifier.ZERO;
        legs = StatModifier.ZERO;
        legsBuff = StatModifier.ZERO;
        feet = StatModifier.ZERO;
        feetBuff = StatModifier.ZERO;
        skills.clear();
        buffs.clear();
        events.clear();
    }

    public double getBase() {
        return base;
    }

    public double getHand() {
        return hand.getAmount();
    }

    public StatOperation getHandType() {
        return hand.getOperation();
    }

    public void setHand(double hand) {
        this.hand = StatModifier.add(hand);
    }

    public void setHand(double hand, @NotNull StatOperation type) {
        this.hand = new StatModifier(hand, type);
    }

    public double getHandBuff() {
        return handBuff.getAmount();
    }

    public StatOperation getHandBuffType() {
        return handBuff.getOperation();
    }

    public void setHandBuff(double handBuff) {
        this.handBuff = StatModifier.add(handBuff);
    }

    public void setHandBuff(double handBuff, @NotNull StatOperation type) {
        this.handBuff = new StatModifier(handBuff, type);
    }

    public double getOffHand() {
        return offHand.getAmount();
    }

    public StatOperation getOffHandType() {
        return offHand.getOperation();
    }

    public void setOffHand(double offHand) {
        this.offHand = StatModifier.add(offHand);
    }

    public void setOffHand(double offHand, @NotNull StatOperation type) {
        this.offHand = new StatModifier(offHand, type);
    }

    public double getOffHandBuff() {
        return offHandBuff.getAmount();
    }

    public StatOperation getOffHandBuffType() {
        return offHandBuff.getOperation();
    }

    public void setOffHandBuff(double offHandBuff) {
        this.offHandBuff = StatModifier.add(offHandBuff);
    }

    public void setOffHandBuff(double offHandBuff, @NotNull StatOperation type) {
        this.offHandBuff = new StatModifier(offHandBuff, type);
    }

    public double getHead() {
        return head.getAmount();
    }

    public StatOperation getHeadType() {
        return head.getOperation();
    }

    public void setHead(double head) {
        this.head = StatModifier.add(head);
    }

    public void setHead(double head, @NotNull StatOperation type) {
        this.head = new StatModifier(head, type);
    }

    public double getHeadBuff() {
        return headBuff.getAmount();
    }

    public StatOperation getHeadBuffType() {
        return headBuff.getOperation();
    }

    public void setHeadBuff(double headBuff) {
        this.headBuff = StatModifier.add(headBuff);
    }

    public void setHeadBuff(double headBuff, @NotNull StatOperation type) {
        this.headBuff = new StatModifier(headBuff, type);
    }

    public double getChest() {
        return chest.getAmount();
    }

    public StatOperation getChestType() {
        return chest.getOperation();
    }

    public void setChest(double chest) {
        this.chest = StatModifier.add(chest);
    }

    public void setChest(double chest, @NotNull StatOperation type) {
        this.chest = new StatModifier(chest, type);
    }

    public double getChestBuff() {
        return chestBuff.getAmount();
    }

    public StatOperation getChestBuffType() {
        return chestBuff.getOperation();
    }

    public void setChestBuff(double chestBuff) {
        this.chestBuff = StatModifier.add(chestBuff);
    }

    public void setChestBuff(double chestBuff, @NotNull StatOperation type) {
        this.chestBuff = new StatModifier(chestBuff, type);
    }

    public double getLegs() {
        return legs.getAmount();
    }

    public StatOperation getLegsType() {
        return legs.getOperation();
    }

    public void setLegs(double legs) {
        this.legs = StatModifier.add(legs);
    }

    public void setLegs(double legs, @NotNull StatOperation type) {
        this.legs = new StatModifier(legs, type);
    }

    public double getLegsBuff() {
        return legsBuff.getAmount();
    }

    public StatOperation getLegsBuffType() {
        return legsBuff.getOperation();
    }

    public void setLegsBuff(double legsBuff) {
        this.legsBuff = StatModifier.add(legsBuff);
    }

    public void setLegsBuff(double legsBuff, @NotNull StatOperation type) {
        this.legsBuff = new StatModifier(legsBuff, type);
    }

    public double getFeet() {
        return feet.getAmount();
    }

    public StatOperation getFeetType() {
        return feet.getOperation();
    }

    public void setFeet(double feet) {
        this.feet = StatModifier.add(feet);
    }

    public void setFeet(double feet, @NotNull StatOperation type) {
        this.feet = new StatModifier(feet, type);
    }

    public double getFeetBuff() {
        return feetBuff.getAmount();
    }

    public StatOperation getFeetBuffType() {
        return feetBuff.getOperation();
    }

    public void setFeetBuff(double feetBuff) {
        this.feetBuff = StatModifier.add(feetBuff);
    }

    public void setFeetBuff(double feetBuff, @NotNull StatOperation type) {
        this.feetBuff = new StatModifier(feetBuff, type);
    }

    public double getSkillBonus(@NotNull String source) {
        return skills.getOrDefault(source, StatModifier.ZERO).getAmount();
    }

    public void setSkillBonus(@NotNull String source, double value) {
        skills.put(source, StatModifier.add(value));
    }

    public void setSkillBonus(@NotNull String source, @NotNull StatModifier statModifier) {
        skills.put(source, statModifier);
    }

    public void setSkillBonus(@NotNull String source, double value, @NotNull StatOperation type) {
        skills.put(source, new StatModifier(value, type));
    }

    public void removeSkillBonus(@NotNull String source) {
        skills.remove(source);
    }

    public double getTotalSkillBonus() {
        return sumRaw(skills);
    }

    public double getTotalBoost() {
        return sumRaw(buffs);
    }

    public boolean hasBoost(@NotNull String name) {
        return buffs.containsKey(name);
    }

    public double getBoost(@NotNull String name) {
        return buffs.getOrDefault(name, StatModifier.ZERO).getAmount();
    }

    public void setBoost(@NotNull String name, @NotNull StatModifier modifier) {
        buffs.put(name, modifier);
    }

    public void setBoost(@NotNull String name, double value) {
        buffs.put(name, StatModifier.add(value));
    }

    public void setBoost(@NotNull String name, double value, @NotNull StatOperation type) {
        buffs.put(name, new StatModifier(value, type));
    }

    public void removeBoost(@NotNull String name) {
        buffs.remove(name);
    }

    public void clearBoosts() {
        buffs.clear();
    }

    public double getEvent(@NotNull String name) {
        return events.getOrDefault(name, StatModifier.ZERO).getAmount();
    }

    public void setEvent(@NotNull String source, double value) {
        events.put(source, StatModifier.add(value));
    }

    public void setEvent(@NotNull String source, @NotNull StatModifier modifier) {
        events.put(source, modifier);
    }

    public void setEvent(@NotNull String source, double value, @NotNull StatOperation type) {
        events.put(source, new StatModifier(value, type));
    }

    public void removeEvent(@NotNull String source) {
        events.remove(source);
    }

    public double getTotalEvent() {
        return sumRaw(events);
    }

    private double sumRaw(@NotNull Map<String, StatModifier> map) {
        double total = 0;
        for (StatModifier modifier : map.values()) {
            total += modifier.getAmount();
        }
        return total;
    }

}