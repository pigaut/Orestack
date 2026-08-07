package io.github.pigaut.rpg.module.mob.spawnpad.command;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MobSpawnPadMakeVisibleSubCommand extends SubCommand {

    public MobSpawnPadMakeVisibleSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "make-visible");
        withPermission(plugin.getPermission("mob.spawn-pad.make-visible"));
        withDescription(plugin.getTranslation("mob-spawn-pad-make-visible-command"));
        withPlayerExecution((player, context, args) -> {
            PlayerState playerState = plugin.getPlayerState(player);

            if (!playerState.isMobSpawnPadsVisible()) {
                playerState.setMobSpawnPadsVisible(true);
                plugin.sendMessage(player, context, "made-visible-mob-spawn-pads");
            } else {
                playerState.setMobSpawnPadsVisible(false);
                plugin.sendMessage(player, context, "made-invisible-mob-spawn-pads");
            }
        });
    }

}
