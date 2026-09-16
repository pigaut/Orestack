package io.github.pigaut.rpg.core.gameplay.settings;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.line.*;
import org.jetbrains.annotations.*;

public class GameplayConfigSettings implements GameplaySettings {

    private final EnhancedPlugin plugin;
    private final Settings settings;

    public GameplayConfigSettings(@NotNull EnhancedPlugin plugin, @NotNull Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    private Boolean showDeathMessages;
    private Boolean showSlainMessages;

    private Boolean hungerDepletion;
    private Boolean hungerHealthRegen;
    private Float exhaustionMultiplier;
    private Integer hungerCap;
    private Integer saturationCap;

    private Boolean customCowMilking;
    private Integer cowMaxMilk;
    private Amount milkProduceAmount;
    private Delay milkProduceDelay;

    private Boolean customEggLaying;
    private Amount eggLayAmount;
    private Delay eggLayDelay;

    private @Nullable Function onPlayerDamageEntity;

    public void loadConfiguration(@NotNull ConfigSection config) {
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


        plugin.loadWhenReady(() -> {
            onPlayerDamageEntity = config.get("on-player-damage-entity", Function.class)
                    .withDefault(null);
        });

    }

    @Override
    public boolean isShowDeathMessages() {
        settings.checkLoaded(showDeathMessages);
        return showDeathMessages;
    }

    @Override
    public boolean isShowSlainMessages() {
        settings.checkLoaded(showSlainMessages);
        return showSlainMessages;
    }

    @Override
    public boolean isCustomCowMilking() {
        settings.checkLoaded(customCowMilking);
        return customCowMilking;
    }

    @Override
    public boolean isHungerDepletion() {
        settings.checkLoaded(hungerDepletion);
        return hungerDepletion;
    }

    @Override
    public boolean isHungerHealthRegen() {
        settings.checkLoaded(hungerHealthRegen);
        return hungerHealthRegen;
    }

    @Override
    public float getExhaustionMultiplier() {
        settings.checkLoaded(exhaustionMultiplier);
        return exhaustionMultiplier;
    }

    @Override
    public int getHungerCap() {
        settings.checkLoaded(hungerCap);
        return hungerCap;
    }

    @Override
    public int getSaturationCap() {
        settings.checkLoaded(saturationCap);
        return saturationCap;
    }

    @Override
    public int getCowMaxMilk() {
        settings.checkLoaded(cowMaxMilk);
        return cowMaxMilk;
    }

    @Override
    public @NotNull Amount getMilkProduceAmount() {
        settings.checkLoaded(milkProduceAmount);
        return milkProduceAmount;
    }

    @Override
    public @NotNull Delay getMilkProduceDelay() {
        settings.checkLoaded(milkProduceDelay);
        return milkProduceDelay;
    }

    @Override
    public boolean isCustomEggLaying() {
        settings.checkLoaded(customEggLaying);
        return customEggLaying;
    }

    @Override
    public @NotNull Amount getEggLayAmount() {
        settings.checkLoaded(eggLayAmount);
        return eggLayAmount;
    }

    @Override
    public @NotNull Delay getEggLayDelay() {
        settings.checkLoaded(eggLayDelay);
        return eggLayDelay;
    }

    @Override
    public @Nullable Function getOnPlayerDamageEntity() {
        return onPlayerDamageEntity;
    }

}
