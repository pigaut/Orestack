package io.github.pigaut.rpg.module.mob.spawnpad.command;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MobSpawnPadSubCommand extends SubCommand {

    public MobSpawnPadSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "spawn-pad");
        this.withPermission(plugin.getPermission("mob.spawn-pad"));
        this.withDescription(plugin.getTranslation("mob-spawn-pad-command"));
        this.addSubCommand(new MobSpawnPadGetSubCommand(plugin));
        this.addSubCommand(new MobSpawnPadGetAllSubCommand(plugin));
        this.addSubCommand(new MobSpawnPadMakeVisibleSubCommand(plugin));
    }
}
