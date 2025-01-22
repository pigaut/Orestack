package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GeneratorGetWandSubCommand extends LangSubCommand {

    public GeneratorGetWandSubCommand(@NotNull OrestackPlugin plugin) {
        super("get-wand", plugin);
        withPlayerExecution((player, args, placeholders) -> {
            PlayerUtil.giveItemsOrDrop(player, SelectionUtil.getSelectionWand());
            plugin.sendMessage(player, "received-wand", placeholders);
        });
    }

}
