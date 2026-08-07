package io.github.pigaut.rpg.module.mob.spawnpad.command;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnpad.tool.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnpad.tool.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MobSpawnPadGetAllSubCommand extends SubCommand {

    public MobSpawnPadGetAllSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "get-all");
        withPermission(plugin.getPermission("mob.spawn-pad.get-all"));
        withDescription(plugin.getTranslation("mob-spawn-pad-get-all-command"));
        withPlayerExecution((player, context, args) -> {
            Tool tool = plugin.getTool("mob_spawn_pad");
            if (!(tool instanceof MobSpawnPadTool mobSpawnPadTool)) {
                plugin.sendMessage(player, context, "tool-not-found");
                return;
            }

            for (MobTemplate mobTemplate : plugin.getMobTemplates().getAll()) {
                PlayerUtil.giveItemsOrDrop(player, mobSpawnPadTool.createItem(mobTemplate));
            }

            plugin.sendMessage(player, context, "received-all-mob-spawn-pads");
        });
    }

}
