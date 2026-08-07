package io.github.pigaut.rpg.core.gameplay.settings;

import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

public interface GameplaySettings {

    boolean isShowDeathMessages();

    boolean isShowSlainMessages();

    boolean isCustomCowMilking();

    boolean isHungerDepletion();

    boolean isHungerHealthRegen();

    float getExhaustionMultiplier();

    int getHungerCap();

    int getSaturationCap();

    int getCowMaxMilk();

    @NotNull
    Amount getMilkProduceAmount();

    @NotNull
    Delay getMilkProduceDelay();

    boolean isCustomEggLaying();

    @NotNull
    Amount getEggLayAmount();

    @NotNull
    Delay getEggLayDelay();

}
