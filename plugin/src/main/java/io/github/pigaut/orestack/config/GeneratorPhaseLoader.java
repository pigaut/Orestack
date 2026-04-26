package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.data.structure.*;
import io.github.pigaut.voxel.util.Server;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorPhaseLoader implements ConfigLoader<GeneratorPhase> {

    private final OrestackPlugin plugin;

    public GeneratorPhaseLoader(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid generator phase";
    }

    @Override
    public @NotNull GeneratorPhase loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        GrowthState state = section.getRequired("type|state", GrowthState.class);

        StructureTemplate structure = section.contains("structure|blocks") ?
                section.getRequired("structure|blocks", StructureTemplate.class) :
                section.getRequired(StructureTemplate.class);

        List<Material> decorativeBlocks = section.getAllRequired("decorative-blocks", Material.class);

        Boolean defaultDrops = section.getBoolean("default-drops|drops").withDefault(null);
        boolean dropItems = defaultDrops != null ? defaultDrops :
                section.getBoolean("drop-items").withDefault(false);
        boolean dropExp = defaultDrops != null ? defaultDrops :
                section.getBoolean("drop-exp|drop-xp").withDefault(false);

        Amount toolDamage = section.get("tool-damage", Amount.class)
                .withDefault(plugin.getSettings().getDefaultToolDamage());

        Double health = section.getDouble("health")
                .require(Requirements.positive())
                .withDefault(null);

        boolean idle = section.getBoolean("idle").withDefault(health != null);

        if (health != null && !idle) {
            throw new InvalidConfigException(section, "idle", "Generator Phase with health set must be idle.");
        }

        Boolean harvestOnly = section.getBoolean("harvest-only").withDefault(null);
        int growthTime = section.get("growth|growth-time", Ticks.class)
                .check(harvestOnly == null || !harvestOnly, "Cannot set growth time while harvest-only is true")
                .map(Ticks::getCount)
                .withDefault(0);

        Double chance = section.getDouble("chance|growth-chance").withDefault(null);

        int clickCooldown = section.getInteger("click-cooldown")
                .require(Requirements.min(1))
                .withDefault(plugin.getSettings().getClickCooldown());

        int hitCooldown = section.getInteger("hit-cooldown")
                .require(Requirements.min(1))
                .withDefault(plugin.getSettings().getHitCooldown());

        int harvestCooldown = section.getInteger("harvest-cooldown")
                .require(Requirements.min(1))
                .withDefault(plugin.getSettings().getClickCooldown());

        HologramTemplate hologramTemplate = section.get("hologram", HologramTemplate.class)
                .withDefault(null);

        Function onBreak = section.get("on-break", Function.class).withDefault(null);
        Function onGrowth = section.get("on-growth", Function.class).withDefault(null);
        Function onClick = section.get("on-click", Function.class).withDefault(null);
        Function onHit = section.get("on-hit|on-left-click", Function.class).withDefault(null);
        Function onHarvest = section.get("on-harvest|on-right-click", Function.class).withDefault(null);
        Function onDestroy = section.get("on-destroy", Function.class).withDefault(null);

        return new GeneratorPhase(state, structure, decorativeBlocks, dropItems, dropExp,
                toolDamage, idle, growthTime, chance, health, clickCooldown, hitCooldown, harvestCooldown,
                hologramTemplate, onBreak, onGrowth, onClick, onHit, onHarvest, onDestroy);
    }

}
