package io.github.pigaut.rpg.module.gate.settings;

import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateConfigSettings implements GateSettings {

    private final EnhancedPlugin plugin;

    private int gateClickCooldown;
    private List<GateTemplate> playerConstructions;

    public GateConfigSettings(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfiguration(@NotNull ConfigSection config) {
        gateClickCooldown = config.getInteger("gate-click-cooldown")
                .require(Requirements.positive())
                .withDefault(4);

        playerConstructions = config.getList("player-constructions", GateTemplate.class)
                .withDefault(List.of());
    }

    @Override
    public int getGateClickCooldown() {
        return gateClickCooldown;
    }

    @Override
    public boolean isPlayerConstruction(@NotNull GateTemplate construction) {
        return playerConstructions.contains(construction);
    }

}

