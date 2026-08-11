package io.github.pigaut.rpg.core.gameplay.settings;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.boot.phase.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.line.*;
import io.github.pigaut.yaml.node.section.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimpleGameplaySettings implements GameplaySettings {

    private final EnhancedPlugin plugin;

    private boolean showDeathMessages;
    private boolean showSlainMessages;

    private boolean hungerDepletion;
    private boolean hungerHealthRegen;
    private float exhaustionMultiplier;
    private int hungerCap;
    private int saturationCap;

    private boolean customCowMilking;
    private int cowMaxMilk;
    private Amount milkProduceAmount;
    private Delay milkProduceDelay;

    private boolean customEggLaying;
    private Amount eggLayAmount;
    private Delay eggLayDelay;

    private Function onPlayerDamageEntity = null;

    public SimpleGameplaySettings(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfigurationData(@NotNull ConfigSection config) {
        showDeathMessages = config.getBoolean("show-death-messages")
                .withDefault(true);

        showSlainMessages = config.getBoolean("show-slain-messages")
                .withDefault(true);

        hungerDepletion = config.getBoolean("hunger-depletion")
                .withDefault(false);

        hungerHealthRegen = config.getBoolean("hunger-health-regen")
                .withDefault(false);

        exhaustionMultiplier = config.getFloat("exhaustion-multiplier")
                .require(Requirements.positive())
                .withDefault(0.7f);

        hungerCap = config.getInteger("hunger-cap")
                .require(Requirements.between(0, 20))
                .withDefault(20);

        saturationCap = config.getInteger("saturation-cap")
                .require(Requirements.between(0, 20))
                .withDefault(20);

        customCowMilking = config.getBoolean("custom-cow-milking")
                .withDefault(true);

        cowMaxMilk = config.getInteger("cow-max-milk")
                .withDefault(3);

        config.getLine("milk-production", LineStyle.SPACED, "<amount> milk every <delay>")
                .ifValidOrElse(milkLine -> {
                    milkProduceAmount = milkLine.get(0, Amount.class)
                            .withDefault(Amount.ONE);
                    milkProduceDelay = milkLine.get(3, Delay.class)
                            .withDefault(Delay.fromSeconds(20));
                }, () -> {
                    milkProduceAmount = Amount.ONE;
                    milkProduceDelay = Delay.fromSeconds(20);
                });

        customEggLaying = config.getBoolean("custom-egg-laying")
                .withDefault(true);

        config.getLine("egg-laying", LineStyle.SPACED, "<amount> egg|eggs every <delay>")
                .ifValidOrElse(milkLine -> {
                    eggLayAmount = milkLine.get(0, Amount.class)
                            .withDefault(Amount.ONE);
                    eggLayDelay = milkLine.get(3, Delay.class)
                            .withDefault(Delay.fromSeconds(20));
                }, () -> {
                    eggLayAmount = Amount.ONE;
                    eggLayDelay = Delay.fromSeconds(20);
                });


        plugin.runOnStartup(() -> {
            onPlayerDamageEntity = config.get("on-player-damage-entity", Function.class)
                    .withDefault(null);
        });

    }

    @Override
    public boolean isShowDeathMessages() {
        return showDeathMessages;
    }

    @Override
    public boolean isShowSlainMessages() {
        return showSlainMessages;
    }

    @Override
    public boolean isCustomCowMilking() {
        return customCowMilking;
    }

    @Override
    public boolean isHungerDepletion() {
        return hungerDepletion;
    }

    @Override
    public boolean isHungerHealthRegen() {
        return hungerHealthRegen;
    }

    @Override
    public float getExhaustionMultiplier() {
        return exhaustionMultiplier;
    }

    @Override
    public int getHungerCap() {
        return hungerCap;
    }

    @Override
    public int getSaturationCap() {
        return saturationCap;
    }

    @Override
    public int getCowMaxMilk() {
        return cowMaxMilk;
    }

    @Override
    public @NotNull Amount getMilkProduceAmount() {
        return milkProduceAmount;
    }

    @Override
    public @NotNull Delay getMilkProduceDelay() {
        return milkProduceDelay;
    }

    @Override
    public boolean isCustomEggLaying() {
        return customEggLaying;
    }

    @Override
    public @NotNull Amount getEggLayAmount() {
        return eggLayAmount;
    }

    @Override
    public @NotNull Delay getEggLayDelay() {
        return eggLayDelay;
    }

    @Override
    public @Nullable Function getOnPlayerDamageEntity() {
        return onPlayerDamageEntity;
    }
}
