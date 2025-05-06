package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.generator.parameter.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorGetGroupSubCommand extends LangSubCommand {

    public GeneratorGetGroupSubCommand(@NotNull OrestackPlugin plugin) {
        super("get-group-generators", plugin);
        addParameter(new GeneratorGroupParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final List<GeneratorTemplate> groupGenerators = plugin.getGeneratorTemplates().getGeneratorTemplates(args[0]);

            if (groupGenerators.isEmpty()) {
                plugin.sendMessage(player, "group-not-found", placeholders);
                return;
            }

            for (GeneratorTemplate generator : plugin.getGeneratorTemplates().getGeneratorTemplates(args[0])) {
                PlayerUtil.giveItemsOrDrop(player, generator.getItem());
            }
            plugin.sendMessage(player, "received-group-generators", placeholders);
        });
    }

}
