package io.github.pigaut.rpg.command.mob;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class MobSpawnSubCommand extends SubCommand {

    public MobSpawnSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "spawn");
        withPermission(plugin.getPermission("mob.spawn"));
        withDescription(plugin.getTranslation("mob-spawn-command"));
        withParameter(CommandParameters.mobName(plugin));
        withPlayerExecution((player, context, args) -> {
            MobTemplate mobTemplate = plugin.getMobTemplate(args[0]);
            if (mobTemplate == null) {
                plugin.sendMessage(player, context, "mob-not-found");
                return;
            }

            Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, context, "too-far-away");
                return;
            }

            Location location = LocationUtil.centered(targetBlock.getLocation());
            mobTemplate.spawn(location);
            plugin.sendMessage(player, context, "spawned-mob");

        });
    }
}
