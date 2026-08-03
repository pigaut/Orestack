package io.github.pigaut.orestack.skill.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.orestack.skill.Skill;
import io.github.pigaut.voxel.core.command.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillIncrementSubCommand extends SubCommand {

    public SkillIncrementSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "increment");
        withPermission(plugin.getPermission("skill.increment"));
        withDescription(plugin.getTranslation("skill-increment-command"));
        withParameter(OrestackParameters.SKILL_NAME);
        withParameter(CommandParameter.create("amount", "1", (sender, args) -> List.of("1", "10", "100", "1000", "10000")));
        withPlayerExecution((player, context, args) -> {
            RpgPlayerData playerData = plugin.getPlayerData(player);
            Skill skill = playerData.getSkill(args[0]);
            if (skill == null) {
                plugin.sendMessage(player, context, "skill-not-found");
                return;
            }

            Integer amount = ParseUtil.parseIntegerOrNull(args[1]);
            if (amount == null) {
                plugin.sendMessage(player, context, "expected-amount");
                return;
            }

            if (amount < 1) {
                plugin.sendMessage(player, context, "amount-must-be-positive");
                return;
            }

            skill.increaseExp(context, amount);
            plugin.sendMessage(player, context, "incremented-skill");
        });
    }

}