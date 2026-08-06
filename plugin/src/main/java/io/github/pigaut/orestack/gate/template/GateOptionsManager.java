package io.github.pigaut.orestack.gate.template;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.plugin.manager.config.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.section.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateOptionsManager extends Manager implements ConfigBacked {

    private final OrestackPlugin plugin;

    private List<GateTemplate> playerConstructions;

    public GateOptionsManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public @NotNull ErrorCollector loadConfigurationData() {
        RootSection config = plugin.getConfiguration();

        playerConstructions = config.getList("player-constructions", GateTemplate.class)
                .withDefault(List.of());

        return config;
    }

    public boolean isPlayerConstruction(@NotNull GateTemplate construction) {
        return playerConstructions.contains(construction);
    }

}
