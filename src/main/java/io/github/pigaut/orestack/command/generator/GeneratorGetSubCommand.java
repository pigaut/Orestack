package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GeneratorGetSubCommand extends LangSubCommand {

    public GeneratorGetSubCommand(@NotNull OrestackPlugin plugin) {
        super("get-generator", plugin);
        addParameter(new GeneratorNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "generator-not-found", placeholders);
                return;
            }
            PlayerUtil.giveItemsOrDrop(player, generator.getItem());
            plugin.sendMessage(player, "received-generator", placeholders, generator);
        });
    }

}
