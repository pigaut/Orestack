package io.github.pigaut.rpg.module.mob.spawnegg.command;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnegg.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnegg.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MobSpawnEggGetAllSubCommand extends SubCommand {

    public MobSpawnEggGetAllSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "get-all");
        withPermission(plugin.getPermission("mob.spawn-egg.get-all"));
        withDescription(plugin.getTranslation("mob-spawn-egg-get-all-command"));
        withPlayerExecution((player, context, args) -> {
            Tool tool = plugin.getTool("mob_spawn_egg");
            if (!(tool instanceof MobSpawnEggTool mobSpawnEggTool)) {
                plugin.sendMessage(player, context, "tool-not-found");
                return;
            }

            for (MobTemplate mobTemplate : plugin.getMobTemplates().getAll()) {
                PlayerUtil.giveItemsOrDrop(player, mobSpawnEggTool.createItem(mobTemplate));
            }

            plugin.sendMessage(player, context, "received-all-mob-spawn-eggs");
        });
    }

}
