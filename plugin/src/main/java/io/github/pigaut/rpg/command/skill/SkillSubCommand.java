package io.github.pigaut.rpg.command.skill;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.node.*;
import org.jetbrains.annotations.*;

public class SkillSubCommand extends SubCommand {

    public SkillSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "skill");
        this.withPermission(plugin.getPermission("skill"));
        this.withDescription(plugin.getTranslation("skill-command"));
        this.addSubCommand(new SkillIncrementSubCommand(plugin));
        this.addSubCommand(new SkillDecrementSubCommand(plugin));
    }

}