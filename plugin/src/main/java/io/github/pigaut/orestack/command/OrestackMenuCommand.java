package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.command.node.*;
import org.jetbrains.annotations.*;

public class OrestackMenuCommand extends SubCommand {

    public OrestackMenuCommand(@NotNull OrestackPlugin plugin) {
        super("menu", plugin);
        withPermission(plugin.getPermission("menu"));
        withDescription(plugin.getLang("orestack-menu-command"));
        withPlayerStateExecution((player, args, placeholders) -> {
            player.openMenu(new OrestackMenu(plugin));
        });
    }

}
