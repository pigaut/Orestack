package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.generator.*;
import io.github.pigaut.orestack.command.structure.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.item.command.*;
import io.github.pigaut.voxel.message.command.*;
import io.github.pigaut.voxel.particle.command.*;
import io.github.pigaut.voxel.plugin.command.*;
import io.github.pigaut.voxel.sound.command.*;
import org.jetbrains.annotations.*;

public class OrestackCommand extends EnhancedCommand {

    public OrestackCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "orestack");
        this.description = "Orestack plugin commands";
        this.setAliases("ostack");

        addSubCommand(new HelpSubCommand(plugin));
        addSubCommand(new ReloadSubCommand(plugin));
        addSubCommand(new ItemSubCommand(plugin));
        addSubCommand(new ParticleSubCommand(plugin));
        addSubCommand(new MessageSubCommand(plugin));
        addSubCommand(new SoundSubCommand(plugin));
        addSubCommand(new GeneratorSubCommand(plugin));
        addSubCommand(new StructureSubCommand(plugin));
        addSubCommand(new GetWandSubCommand(plugin));
    }

}
