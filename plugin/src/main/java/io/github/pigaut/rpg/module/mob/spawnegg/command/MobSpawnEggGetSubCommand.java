package io.github.pigaut.rpg.module.mob.spawnegg.command;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnegg.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnegg.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MobSpawnEggGetSubCommand extends SubCommand {

    public MobSpawnEggGetSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "get");
        withPermission(plugin.getPermission("mob.spawn-egg.get"));
        withDescription(plugin.getTranslation("mob-spawn-egg-get-command"));
        withParameter(CommandParameters.mobName(plugin));
        withPlayerExecution((player, context, args) -> {
            MobTemplate mobTemplate = plugin.getMobTemplate(args[0]);
            if (mobTemplate == null) {
                plugin.sendMessage(player, context, "mob-not-exists");
                return;
            }

            Tool tool = plugin.getTool("mob_spawn_egg");
            if (!(tool instanceof MobSpawnEggTool mobSpawnEggTool)) {
                plugin.sendMessage(player, context, "tool-not-found");
                return;
            }

            PlayerUtil.giveItemsOrDrop(player, mobSpawnEggTool.createItem(mobTemplate));
            plugin.sendMessage(player, context, "received-mob-spawn-egg");
        });
    }

}
