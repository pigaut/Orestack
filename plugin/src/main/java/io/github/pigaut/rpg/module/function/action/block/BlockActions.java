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

        actions.register("STRIKE_BLOCK", (Line<Action>) line ->
                new StrikeBlockWithLightning(line.getBoolean("doDamage|damage").withDefault(true)));

        actions.register("DROP_ITEM_AT_BLOCK", (Line<Action>) line ->
                new DropItemAtBlock(line.getRequired(ItemDrop.class)));

        actions.register("DROP_EXP_AT_BLOCK", (Line<Action>) line ->
                new DropExpAtBlock(line.getRequired(ExpDrop.class)));

        actions.register("SPAWN_PARTICLE_AT_BLOCK", (Line<Action>) line ->
                new SpawnParticleAtBlock(line.getRequired(1, ParticleEffect.class)));

        actions.register("PLAY_SOUND_AT_BLOCK", (Line<Action>) line ->
                new PlaySoundAtBlock(line.getRequired(1, SoundEffect.class)));

        actions.registerAlias("DROP_ITEM_AT_BLOCK", "BLOCK_ITEM_DROP", "DROP_AT_BLOCK");
        actions.registerAlias("DROP_EXP_AT_BLOCK", "BLOCK_EXP_DROP");
        actions.registerAlias("SPAWN_PARTICLE_AT_BLOCK", "BLOCK_PARTICLE");
        actions.registerAlias("PLAY_SOUND_AT_BLOCK", "BLOCK_SOUND");
    }

}