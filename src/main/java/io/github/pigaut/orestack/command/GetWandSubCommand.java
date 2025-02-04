package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GetWandSubCommand extends LangSubCommand {

    public GetWandSubCommand(@NotNull OrestackPlugin plugin) {
        super("wand", plugin);
        withPlayerExecution((player, args, placeholders) -> {
            PlayerUtil.giveItemsOrDrop(player, GeneratorTools.getWandTool());
            plugin.sendMessage(player, "received-wand", placeholders);
        });
    }

}
