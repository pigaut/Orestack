package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.protagonist.*;
import io.github.pigaut.rpg.module.function.action.protagonist.cooldown.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

public class ProtagonistActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("DAMAGE_ENEMY", (ConfigLoader.Line<Action>) line ->
                new ProtagonistAttackEnemy(plugin, line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("SPAWN_PARTICLE_AT_PROTAGONIST", (ConfigLoader.Line<Action>) line ->
                new SpawnParticleAtProtagonist(line.getRequired(1, ParticleEffect.class)));

        actions.addLoader("PLAY_SOUND_AT_PROTAGONIST", (ConfigLoader.Line<Action>) line ->
                new PlaySoundAtProtagonist(line.getRequired(1, SoundEffect.class)));

        actions.addLoader("ADD_PROTAGONIST_COOLDOWN", (ConfigLoader.Line<Action>) line ->
                new AddProtagonistCooldown(
                        line.getRequiredString(1),
                        line.getRequired("duration", Delay.class)
                ));

        actions.addLoader("REMOVE_PROTAGONIST_COOLDOWN", (ConfigLoader.Line<Action>) line ->
                new RemoveProtagonistCooldown(line.getRequiredString(1)));

        actions.addAliases("DAMAGE_ENEMY", "DAMAGE_ENTITY", "DAMAGE_TARGET", "DAMAGE_VICTIM");

        actions.addAliases("SPAWN_PARTICLE_AT_PROTAGONIST", "SPAWN_PARTICLE");
        actions.addAliases("PLAY_SOUND_AT_PROTAGONIST", "PLAY_SOUND");

        actions.addAliases("ADD_PROTAGONIST_COOLDOWN", "ADD_COOLDOWN", "START_PROTAGONIST_COOLDOWN", "START_COOLDOWN");
        actions.addAliases("REMOVE_PROTAGONIST_COOLDOWN", "REMOVE_COOLDOWN", "STOP_PROTAGONIST_COOLDOWN", "STOP_COOLDOWN");


    }

}
