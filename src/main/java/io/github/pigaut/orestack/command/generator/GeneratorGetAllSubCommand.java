package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GeneratorGetAllSubCommand extends SubCommand {

    public GeneratorGetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("GET_ALL_GENERATORS_COMMAND", "get-all"), plugin);
        withDescription(plugin.getLang("GET_ALL_GENERATORS_DESCRIPTION"));
        withPermission("orestack.generator.get-all");
        withPlayerExecution((player, args) -> {
            for (Generator generator : plugin.getGenerators().getAllGenerators()) {
                PlayerUtil.giveItemsOrDrop(player, generator.getItem());
            }
            plugin.sendMessage(player, "RECEIVED_ALL_GENERATORS", this);
        });
    }
}
