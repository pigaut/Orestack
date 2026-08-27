package io.github.pigaut.rpg.module.function.action.server;

import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.block.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class ServerActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("BROADCAST", (Line<Action>) line ->
                new ServerBroadcast(plugin, line.getRequiredString(1)));

        actions.addLoader("LIGHTNING", (Line<Action>) line ->
                new StrikeLightning(
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z"),
                        line.getBoolean("doDamage|damage").withDefault(true)
                ));

        actions.addLoader("CONSOLE_COMMAND", (Line<Action>) line ->
                new ExecuteConsoleCommand(line.getRequiredString(1)));

        actions.addLoader("DROP_ITEM", (Line<Action>) line -> {
            ItemDrop itemDrop = line.getRequired(ItemDrop.class);

            ItemDropTarget dropTarget = line.get("target", ItemDropTarget.class)
                    .withDefault(plugin.getSettings().getDefaultItemDropTarget());

            return switch (dropTarget) {
                case BLOCK -> new DropItemAtBlock(itemDrop);
                case PLAYER -> new DropItemAtPlayer(itemDrop);
                case COORDS -> new DropItemAtCoords(itemDrop,
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                );
            };
        });

        actions.addLoader("DROP_ITEM_AT_COORDS", (Line<Action>) line ->
                new DropItemAtCoords(
                        line.getRequired(ItemDrop.class),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        actions.addLoader("DROP_EXP", (Line<Action>) line ->
                new DropExp(plugin,
                        line.getRequired(1, Amount.class),
                        line.get("orbs|orbCount", Amount.class).withDefault(null),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z"),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        actions.addLoader("SPAWN_PARTICLE", (Line<Action>) line ->
                new SpawnParticle(
                        line.getRequired(1, ParticleEffect.class),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        actions.addLoader("PARTICLE", (Line<Action>) line ->
                new SpawnParticle(
                        line.getRequired(1, ParticleEffect.class),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        actions.addLoader("PLAY_SOUND", (Line<Action>) line ->
                new PlaySound(
                        line.getRequired(1, SoundEffect.class),
                        line.get("world", World.class).orElse(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        actions.addLoader("SOUND", (Line<Action>) line ->
                new PlaySound(
                        line.getRequired(1, SoundEffect.class),
                        line.get("world", World.class).orElse(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        actions.addAliases("DROP_ITEM", "DROP");
        actions.addAliases("DROP_ITEM_AT_COORDS", "DROP_AT_COORDS");
    }

}