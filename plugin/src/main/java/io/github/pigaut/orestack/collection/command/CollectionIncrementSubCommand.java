package io.github.pigaut.orestack.collection.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.collection.Collection;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.voxel.core.command.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionIncrementSubCommand extends SubCommand {

    public CollectionIncrementSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "increment");
        withPermission(plugin.getPermission("collection.increment"));
        withDescription(plugin.getTranslation("collection-increment-command"));
        withParameter(OrestackParameters.COLLECTION_NAME);
        withParameter(CommandParameter.create("amount", "1", (sender, args) -> List.of("1", "10", "100", "1000", "10000")));
        withPlayerExecution((player, context, args) -> {
            RpgPlayerData playerData = plugin.getPlayerData(player);
            Collection collection = playerData.getItemCollection(args[0]);
            if (collection == null) {
                plugin.sendMessage(player, context, "collection-not-found");
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

            collection.increaseAmount(context, amount);
            plugin.sendMessage(player, context, "incremented-collection");
        });
    }

}
