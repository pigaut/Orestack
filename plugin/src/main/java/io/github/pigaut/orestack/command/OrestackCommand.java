package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.gate.*;
import io.github.pigaut.orestack.command.generator.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.buildstation.command.*;
import io.github.pigaut.voxel.core.command.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.data.collection.command.*;
import io.github.pigaut.voxel.data.item.command.*;
import io.github.pigaut.voxel.data.menu.command.*;
import io.github.pigaut.voxel.data.message.command.*;
import io.github.pigaut.voxel.data.particle.command.*;
import io.github.pigaut.voxel.data.recipe.command.*;
import io.github.pigaut.voxel.data.sound.command.*;
import io.github.pigaut.voxel.data.structure.command.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.command.*;
import io.github.pigaut.voxel.plugin.manager.module.Module;
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
        addSubCommand(new ReloadSubCommand(plugin));
        addSubCommand(new OrestackMenuSubCommand(plugin));
        addSubCommand(new GetWandSubCommand(plugin));

        Settings settings = plugin.getSettings();
        if (settings.isModuleEnabled(Module.ITEMS)) {
            addSubCommand(new ItemSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.PARTICLES)) {
            addSubCommand(new ParticleSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.SOUNDS)) {
            addSubCommand(new SoundSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.MESSAGES)) {
            addSubCommand(new MessageSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.GENERATORS)) {
            addSubCommand(new GeneratorSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.GATES)) {
            addSubCommand(new GateSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.STRUCTURES)) {
            addSubCommand(new StructureSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.RECIPES)) {
            addSubCommand(new RecipeSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.COLLECTIONS)) {
            addSubCommand(new CollectionSubCommand(plugin));
        }
    }

}
