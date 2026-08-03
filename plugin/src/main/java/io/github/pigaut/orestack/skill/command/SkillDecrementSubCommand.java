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

public class SkillDecrementSubCommand extends SubCommand {

    public SkillDecrementSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "decrement");
        withPermission(plugin.getPermission("skill.decrement"));
        withDescription(plugin.getTranslation("skill-decrement-command"));
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

            skill.decreaseExp(context, amount);
            plugin.sendMessage(player, context, "decremented-skill");
        });
    }

}