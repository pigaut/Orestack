package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.function.action.mob.flag.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class MobActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("ADD_MOB_FLAG", (Line<Action>) line ->
                new AddMobFlag(line.getRequiredString(1)));

        actions.addLoader("ADD_TEMPORARY_MOB_FLAG", (Line<Action>) line ->
                new AddTemporaryMobFlag(
                        line.getRequiredString(1),
                        line.get("duration", Delay.class).mapIfValid(Delay::toTicks).orThrow()
                ));

        actions.addLoader("REMOVE_MOB_FLAG", (Line<Action>) line ->
                new RemoveMobFlag(line.getRequiredString(1)));

        actions.addLoader("HEAL_MOB", (Line<Action>) line ->
                new HealMob(line.get(1, Amount.class).withDefault(null)));

        actions.addLoader("DAMAGE_MOB_TARGET", (Line<Action>) line ->
                new MobDamageEnemy(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("DAMAGE_ATTACKERS", (Line<Action>) line ->
                new DamageMobAttackers(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("DROP_ITEM_AT_MOB", (Line<Action>) line ->
                new DropItemAtMob(line.getRequired(ItemDrop.class)));

        actions.addLoader("DROP_ITEM_AT_LAST_DAMAGER", (Line<Action>) line ->
                new DropItemAtLastDamager(line.getRequired(ItemDrop.class)));

        actions.addLoader("DROP_ITEM_AT_TOP_DAMAGER", (Line<Action>) line ->
                new DropItemAtTopDamager(line.getRequired(ItemDrop.class)));

        actions.addLoader("DROP_ITEM_AT_ATTACKERS", (Line<Action>) line ->
                new DropItemAtDamagers(line.getRequired(ItemDrop.class)));

        actions.addLoader("SPAWN_PARTICLE_AT_MOB", (Line<Action>) line ->
                new SpawnParticleAtMob(line.getRequired(1, ParticleEffect.class)));

        actions.addLoader("PLAY_SOUND_AT_MOB", (Line<Action>) line ->
                new PlaySoundAtMob(line.getRequired(1, SoundEffect.class)));

        actions.addAliases("DAMAGE_MOB_TARGET", "DAMAGE_MOB_ENEMY", "DAMAGE_MOB_VICTIM");
        actions.addAliases("DROP_ITEM_AT_MOB", "DROP_AT_MOB");
        actions.addAliases("DROP_ITEM_AT_LAST_DAMAGER", "DROP_AT_LAST_DAMAGER");
        actions.addAliases("DROP_ITEM_AT_TOP_DAMAGER", "DROP_AT_TOP_DAMAGER");
        actions.addAliases("DROP_ITEM_AT_ATTACKERS", "DROP_AT_DAMAGERS");
    }

}