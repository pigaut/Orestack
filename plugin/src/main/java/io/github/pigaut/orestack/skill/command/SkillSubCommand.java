package io.github.pigaut.orestack.skill.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.plugin.*;
import org.jetbrains.annotations.*;

public class SkillSubCommand extends SubCommand {

    public SkillSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "skill");
        this.withPermission(plugin.getPermission("skill"));
        this.withDescription(plugin.getTranslation("skill-command"));
        this.addSubCommand(new SkillIncrementSubCommand(plugin));
        this.addSubCommand(new SkillDecrementSubCommand(plugin));
    }

}