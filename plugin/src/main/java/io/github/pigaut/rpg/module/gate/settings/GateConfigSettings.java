package io.github.pigaut.rpg.module.gate.settings;

import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateConfigSettings implements GateSettings {

    private final EnhancedPlugin plugin;

    private Delay gateClickCooldown;
    private List<GateTemplate> playerConstructions;

    public GateConfigSettings(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfiguration(@NotNull ConfigSection config) {
        gateClickCooldown = config.get("gate-click-cooldown", Delay.class)
                .withDefault(Delay.fromTicks(4));

        playerConstructions = config.getList("player-constructions", GateTemplate.class)
                .withDefault(List.of());
    }

    @Override
    public @NotNull Delay getGateClickCooldown() {
        return gateClickCooldown;
    }

    @Override
    public boolean isPlayerConstruction(@NotNull GateTemplate construction) {
        return playerConstructions.contains(construction);
    }

}

