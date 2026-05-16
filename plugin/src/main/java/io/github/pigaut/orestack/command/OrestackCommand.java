package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.generator.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.buildstation.command.*;
import io.github.pigaut.voxel.core.command.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.data.item.command.*;
import io.github.pigaut.voxel.data.menu.command.*;
import io.github.pigaut.voxel.data.message.command.*;
import io.github.pigaut.voxel.data.particle.command.*;
import io.github.pigaut.voxel.data.recipe.command.*;
import io.github.pigaut.voxel.data.sound.command.*;
import io.github.pigaut.voxel.data.structure.command.*;
import io.github.pigaut.voxel.plugin.command.*;
import org.jetbrains.annotations.*;

public class OrestackCommand extends EnhancedCommand {

    public OrestackCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "orestack");
        this.description = "Orestack plugin commands";
        this.setAliases("ostack");

        RootCommand command = this.getRootCommand();
        command.withPermission("orestack");
        command.withPlayerStateExecution((player, args, placeholders) -> {
           player.openMenu(new OrestackMenu(plugin));
        });

        addSubCommand(new HelpSubCommand(plugin));
        addSubCommand(new WikiSubCommand(plugin));
        addSubCommand(new SupportSubCommand(plugin));
        addSubCommand(new OrestackMenuSubCommand(plugin));
        addSubCommand(new ReloadSubCommand(plugin));
        addSubCommand(new ItemSubCommand(plugin));
        addSubCommand(new ParticleSubCommand(plugin));
        addSubCommand(new MessageSubCommand(plugin));
        addSubCommand(new SoundSubCommand(plugin));
        addSubCommand(new GeneratorSubCommand(plugin));
        addSubCommand(new StructureSubCommand(plugin));
        addSubCommand(new RecipeSubCommand(plugin));
        addSubCommand(new BuildStationSubCommand(plugin));
        addSubCommand(new GetWandSubCommand(plugin));

    }

}
