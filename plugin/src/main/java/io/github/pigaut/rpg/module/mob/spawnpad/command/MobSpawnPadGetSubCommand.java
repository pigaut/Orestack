package io.github.pigaut.rpg.module.mob.spawnpad.command;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnpad.tool.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnpad.tool.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MobSpawnPadGetSubCommand extends SubCommand {

    public MobSpawnPadGetSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "get");
        withPermission(plugin.getPermission("mob.spawn-pad.get"));
        withDescription(plugin.getTranslation("mob-spawn-pad-get-command"));
        withParameter(CommandParameters.mobName(plugin));
        withPlayerExecution((player, context, args) -> {
            MobTemplate mobTemplate = plugin.getMobTemplate(args[0]);
            if (mobTemplate == null) {
                plugin.sendMessage(player, context, "mob-not-exists");
                return;
            }

            Tool tool = plugin.getTool("mob_spawn_pad");
            if (!(tool instanceof MobSpawnPadTool mobSpawnPadTool)) {
                plugin.sendMessage(player, context, "tool-not-found");
                return;
            }

            PlayerUtil.giveItemsOrDrop(player, mobSpawnPadTool.createItem(mobTemplate));
            plugin.sendMessage(player, context, "received-mob-spawn-pad");
        });
    }

}
