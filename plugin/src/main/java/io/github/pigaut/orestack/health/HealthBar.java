package io.github.pigaut.orestack.health;

import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class HealthBar {

    private final String id;
    private final Map<Amount, String> barsByHealth;

    public HealthBar(@NotNull String id, @NotNull Map<Amount, String> barsByHealth) {
        this.id = id;
        this.barsByHealth = barsByHealth;
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull String getHealthBar(int health) {
        return barsByHealth.getOrDefault(Amount.fixed(health), "");
    }

}
