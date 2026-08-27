package io.github.pigaut.rpg.module.function.action.protagonist;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ProtagonistActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("DAMAGE_ENEMY", (ConfigLoader.Line<Action>) line ->
                new DamageEnemy(plugin, line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("SPAWN_PARTICLE_AT_PROTAGONIST", (ConfigLoader.Line<Action>) line ->
                new SpawnParticleAtProtagonist(line.getRequired(1, ParticleEffect.class)));

        actions.addLoader("PLAY_SOUND_AT_PROTAGONIST", (ConfigLoader.Line<Action>) line ->
                new PlaySoundAtProtagonist(line.getRequired(1, SoundEffect.class)));

    }

}
