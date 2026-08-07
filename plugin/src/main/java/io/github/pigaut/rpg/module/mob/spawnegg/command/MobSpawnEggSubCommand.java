package io.github.pigaut.rpg.module.mob.spawnegg.command;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MobSpawnEggSubCommand extends SubCommand {

    public MobSpawnEggSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "spawn-egg");
        this.withPermission(plugin.getPermission("mob.spawn-egg"));
        this.withDescription(plugin.getTranslation("mob-spawn-egg-command"));
        this.addSubCommand(new MobSpawnEggGetSubCommand(plugin));
        this.addSubCommand(new MobSpawnEggGetAllSubCommand(plugin));
    }

}
