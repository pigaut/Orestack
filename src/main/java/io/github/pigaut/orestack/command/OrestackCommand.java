package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.generator.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.item.command.*;
import io.github.pigaut.voxel.particle.command.*;
import io.github.pigaut.voxel.plugin.command.*;
import org.jetbrains.annotations.*;

public class OrestackCommand extends EnhancedCommand {

    public OrestackCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "orestack");
        this.description = "Orestack admin commands";
        this.setAliases("ostack");
        this.setPermission("orestack.admin");

        final SubCommand helpCommand = new HelpSubCommand(plugin);
        helpCommand.withPermission("orestack.help");

        final SubCommand reloadCommand = new ReloadSubCommand(plugin);
        reloadCommand.withPermission("orestack.reload");

        final SubCommand itemAddCommand = new ItemAddSubCommand(plugin);
        itemAddCommand.withPermission("orestack.item.add");

        final SubCommand itemGetCommand = new ItemGetSubCommand(plugin);
        itemGetCommand.withPermission("orestack.item.get");

        final SubCommand itemCommand = new ItemSubCommand(plugin);
        itemCommand.addSubCommand(itemAddCommand);
        itemCommand.addSubCommand(itemGetCommand);

        final SubCommand particleSpawnCommand = new SpawnParticleSubCommand(plugin);
        particleSpawnCommand.withPermission("orestack.particle.spawn");

        final SubCommand particleCommand = new ParticleSubCommand(plugin);
        particleCommand.addSubCommand(particleSpawnCommand);

        addSubCommand(helpCommand);
        addSubCommand(reloadCommand);
        addSubCommand(itemCommand);
        addSubCommand(particleCommand);
        addSubCommand(new GeneratorSubCommand(plugin));
    }

}
