package io.github.pigaut.rpg.module.function.action.block;

import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.registry.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class BlockActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("STRIKE_BLOCK", (Line<Action>) line ->
                new StrikeBlockWithLightning(line.getBoolean("doDamage|damage").withDefault(true)));

        actions.addLoader("DROP_ITEM_AT_BLOCK", (Line<Action>) line ->
                new DropItemAtBlock(line.getRequired(ItemDrop.class)));

        actions.addLoader("DROP_EXP_AT_BLOCK", (Line<Action>) line ->
                new DropExpAtBlock(plugin,
                        line.getRequired(1, Amount.class),
                        line.get("orbs|orbCount", Amount.class).withDefault(null),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        actions.addLoader("SPAWN_PARTICLE_AT_BLOCK", (Line<Action>) line ->
                new SpawnParticleAtBlock(line.getRequired(1, ParticleEffect.class)));

        actions.addLoader("PLAY_SOUND_AT_BLOCK", (Line<Action>) line ->
                new PlaySoundAtBlock(line.getRequired(1, SoundEffect.class)));

        actions.addAliases("DROP_ITEM_AT_BLOCK", "BLOCK_ITEM_DROP", "DROP_AT_BLOCK");
        actions.addAliases("DROP_EXP_AT_BLOCK", "BLOCK_EXP_DROP");
        actions.addAliases("SPAWN_PARTICLE_AT_BLOCK", "BLOCK_PARTICLE");
        actions.addAliases("PLAY_SOUND_AT_BLOCK", "BLOCK_SOUND");
    }

}