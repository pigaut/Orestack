package io.github.pigaut.rpg.command.item;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.section.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.io.*;

public class ItemSaveSubCommand extends SubCommand {

    public ItemSaveSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "save");
        withPermission(plugin.getPermission("item.save"));
        withDescription(plugin.getTranslation("item-save-command"));
        withParameter(CommandParameters.filePath(plugin, "items"));
        withParameter(CommandParameters.itemName(plugin));
        withPlayerExecution((player, context, args) -> {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                plugin.sendMessage(player, context, "not-holding-item");
                return;
            }

            File file = plugin.getFile("items", args[0]);
            if (!file.exists()) {
                plugin.sendMessage(player, context, "file-not-found");
                return;
            }

            if (!YamlConfig.isYamlFile(file)) {
                plugin.sendMessage(player, context, "not-yaml-file");
                return;
            }

            RootSection config;
            try {
                config = YamlConfig.loadSection(file, plugin.getConfigurator());
            } catch (ConfigLoadException e) {
                plugin.sendMessage(player, context, "file-load-error");
                return;
            }

            plugin.getScheduler().runTaskAsync(() -> {
                config.set(args[1], item);
                config.save();
                plugin.getItems().reload();
                plugin.sendMessage(player, context, "saved-item");
            });
        });
    }

}
