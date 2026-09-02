package io.github.pigaut.rpg.command.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GeneratorRemoveAllSubCommand extends SubCommand {

    public GeneratorRemoveAllSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "remove-all");
        withPermission(plugin.getPermission("generator.remove-all"));
        withDescription(plugin.getTranslation("generator-remove-all-command"));
        withParameter(OrestackParameters.GENERATOR_NAME);
        withPlayerExecution((player, context, args) -> {
            PlayerState playerState = plugin.getPlayerState(player);
            GeneratorTemplate generatorTemplate = plugin.getGeneratorTemplate(args[0]);
            if (generatorTemplate == null) {
                plugin.sendMessage(player, context, "generator-not-found");
                return;
            }

            Location firstSelection = playerState.getFirstSelection();
            Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, context, "incomplete-region");
                return;
            }

            for (Location point : CuboidRegion.getAllLocations(player.getWorld(), firstSelection, secondSelection)) {
                Generator foundGenerator = plugin.getGenerator(player, point);
                if (foundGenerator == null) {
                    continue;
                }
                if (foundGenerator.getTemplate() == generatorTemplate) {
                    foundGenerator.remove();
                }
            }

            plugin.sendMessage(player, context, "removed-all-generators");
        });
    }

}
