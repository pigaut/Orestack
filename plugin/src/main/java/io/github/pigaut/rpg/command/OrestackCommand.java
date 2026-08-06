package io.github.pigaut.rpg.command;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.collection.*;
import io.github.pigaut.rpg.command.gate.*;
import io.github.pigaut.rpg.command.generator.*;
import io.github.pigaut.rpg.command.item.*;
import io.github.pigaut.rpg.command.message.*;
import io.github.pigaut.rpg.command.mob.*;
import io.github.pigaut.rpg.command.particle.*;
import io.github.pigaut.rpg.command.recipe.*;
import io.github.pigaut.rpg.command.skill.*;
import io.github.pigaut.rpg.command.sound.*;
import io.github.pigaut.rpg.command.stat.*;
import io.github.pigaut.rpg.command.structure.*;
import io.github.pigaut.rpg.menu.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.command.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class OrestackCommand extends EnhancedCommand {

    public OrestackCommand(@NotNull RpgMakerPlugin plugin) {
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

        if (settings.isModuleEnabled(Module.MOBS)) {
            addSubCommand(new MobSubCommand(plugin));
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

        if (settings.isModuleEnabled(Module.STATS)) {
            addSubCommand(new StatSubCommand(plugin));
        }

        if (settings.isModuleEnabled(Module.SKILLS)) {
            addSubCommand(new SkillSubCommand(plugin));
        }

    }

}
