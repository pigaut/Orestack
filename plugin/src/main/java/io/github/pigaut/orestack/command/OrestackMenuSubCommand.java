package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.data.menu.command.*;
import org.jetbrains.annotations.*;

public class OrestackMenuSubCommand extends MenuSubCommand {

    public OrestackMenuSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin);
        withPlayerStateExecution((player, args, placeholders) -> {
            player.openMenu(new OrestackMenu(plugin));
        });
    }

}
