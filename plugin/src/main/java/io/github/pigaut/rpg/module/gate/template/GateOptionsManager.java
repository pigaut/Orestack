package io.github.pigaut.rpg.module.gate.template;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.section.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateOptionsManager extends Manager implements ConfigBacked {

    private final RpgMakerPlugin plugin;

    private List<GateTemplate> playerConstructions;

    public GateOptionsManager(RpgMakerPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public @NotNull ErrorCollector loadConfiguration() {
        ConfigSection config = plugin.getConfiguration();

        playerConstructions = config.getList("player-constructions", GateTemplate.class)
                .withDefault(List.of());

        // Errors are collected to config.
        return ErrorCollector.EMPTY;
    }

    public boolean isPlayerConstruction(@NotNull GateTemplate construction) {
        return playerConstructions.contains(construction);
    }

}
