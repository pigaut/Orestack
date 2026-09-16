package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.block.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.action.server.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class ServerActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.register("BROADCAST", (Line<Action>) line ->
                new ServerBroadcast(plugin, line.getRequiredString(1)));

        actions.register("LIGHTNING", (Line<Action>) line ->
                new StrikeLightning(
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z"),
                        line.getBoolean("doDamage|damage").withDefault(true)
                ));

        actions.register("CONSOLE_COMMAND", (Line<Action>) line ->
                new ExecuteConsoleCommand(line.getRequiredString(1)));

        actions.register("DROP_ITEM", (Line<Action>) line ->
                new DropItem(line.getRequired(ItemDrop.class)));

        actions.register("DROP_EXP", (Line<Action>) line ->
                new DropExp(line.getRequired(ExpDrop.class)));

        actions.register("DROP_ITEM_AT_COORDS", (Line<Action>) line ->
                new DropItemAtCoords(
                        line.getRequired(ItemDrop.class),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        actions.register("DROP_EXP_AT_COORDS", (Line<Action>) line ->
                new DropExpAtCoords(plugin,
                        line.getRequired(1, Amount.class),
                        line.get("orbs|orbCount", Amount.class).withDefault(null),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z"),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        actions.register("SPAWN_PARTICLE_AT_COORDS", (Line<Action>) line ->
                new SpawnParticleAtCoords(
                        line.getRequired(1, ParticleEffect.class),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        actions.register("PLAY_SOUND_AT_COORDS", (Line<Action>) line ->
                new PlaySoundAtCoords(
                        line.getRequired(1, SoundEffect.class),
                        line.get("world", World.class).orElse(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        actions.registerAlias("DROP_ITEM", "DROP");
        actions.registerAlias("DROP_ITEM_AT_COORDS", "DROP_AT_COORDS");
        actions.registerAlias("SPAWN_PARTICLE_AT_COORDS", "SPAWN_AT_COORDS");
        actions.registerAlias("PLAY_SOUND_AT_COORDS", "PLAY_AT_COORDS");

    }

}