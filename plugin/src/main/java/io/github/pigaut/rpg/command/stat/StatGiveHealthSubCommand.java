package io.github.pigaut.rpg.command.stat;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StatGiveHealthSubCommand extends SubCommand {

    public StatGiveHealthSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "give-health");
        withPermission(plugin.getPermission("stat.give-health"));
        withDescription(plugin.getTranslation("stat-give-health-command"));
        withParameter(CommandParameter.create("amount", (sender, args) ->
                List.of("10", "25", "50", "100")));
        withParameter(CommandParameters.ONLINE_PLAYER_OPTIONAL);
        withCommandExecution((sender, context, args) -> {
            Integer amount = ParseUtil.parseIntegerOrNull(args[0]);
            if (amount == null || amount <= 0) {
                plugin.sendMessage(sender, context, "expected-positive-amount");
                return;
            }

            String targetName = args[1];
            if (targetName == null && !(sender instanceof Player)) {
                plugin.sendMessage(sender, context, "console-requires-target");
                return;
            }

            Player target = (targetName != null) ? Bukkit.getPlayer(targetName) : (Player) sender;
            if (target == null) {
                plugin.sendMessage(sender, context, "player-not-online");
                return;
            }

            PlayerState playerState = plugin.getPlayerState(target);
            playerState.setHealth(playerState.getHealth() + amount);

            context.addPlaceholder("player", target.getName());
            plugin.sendMessage(sender, context, "gave-health");
        });
    }

}
