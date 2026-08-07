package io.github.pigaut.rpg.core.command.completion;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

@FunctionalInterface
public interface PlayerCompletion extends CommandCompletion {

    List<String> tabComplete(Player player, String[] args);

    @Override
    default @NotNull List<String> tabComplete(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            return tabComplete(player, args);
        }
        return List.of();
    }

}
