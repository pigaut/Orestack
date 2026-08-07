package io.github.pigaut.rpg.command.mob;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.mob.spawnegg.command.*;
import io.github.pigaut.rpg.module.mob.spawnpad.command.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MobSubCommand extends SubCommand {

    public MobSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "mob");
        this.withPermission(plugin.getPermission("mob"));
        this.withDescription(plugin.getTranslation("mob-command"));
        this.addSubCommand(new MobSpawnSubCommand(plugin));
        this.addSubCommand(new MobSpawnPadSubCommand(plugin));
        this.addSubCommand(new MobSpawnEggSubCommand(plugin));
    }

}
