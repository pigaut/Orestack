package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.generator.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.core.item.command.*;
import io.github.pigaut.voxel.core.message.command.*;
import io.github.pigaut.voxel.core.particle.command.*;
import io.github.pigaut.voxel.core.sound.command.*;
import io.github.pigaut.voxel.core.structure.command.*;
import io.github.pigaut.voxel.plugin.command.*;
import org.jetbrains.annotations.*;

public class OrestackCommand extends EnhancedCommand {

    public OrestackCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "orestack");
        this.description = "Orestack plugin commands";
        this.setAliases("ostack");

        final RootCommand command = this.getRootCommand();
        command.withPermission("orestack");
//        command.withPlayerStateExecution((player, args, placeholders) -> {
//           player.openMenu(plugin.getMenu("orestack"));
//        });

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
