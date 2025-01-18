package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GeneratorGetAllSubCommand extends LangSubCommand {

    public GeneratorGetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super("get-all-generators", plugin);
        withPlayerExecution((player, args, placeholders) -> {
            for (Generator generator : plugin.getGenerators().getAllGenerators()) {
                PlayerUtil.giveItemsOrDrop(player, generator.getItem());
            }
            plugin.sendMessage(player, "received-all-generators", placeholders);
        });
    }
}
