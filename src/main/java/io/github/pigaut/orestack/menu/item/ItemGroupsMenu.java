package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemGroupsMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin;

    public ItemGroupsMenu(OrestackPlugin plugin) {
        super("Item Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getItems().getAllGroups().stream()
                .map(group -> Button.builder()
                        .withType(Material.CHEST)
                        .withDisplay("&a&l" + StringFormatter.toTitleCase(group))
                        .addLore("")
                        .addLore("&eLeft-Click: &fView all items")
                        .addLore("&6Right-Click: &fGet all items")
                        .onLeftClick((view, player, event) ->
                                player.openMenu(new ItemsMenu(plugin, group)))
                        .onRightClick((view, player, event) ->
                                player.performCommand("orestack item get-group " + group))
                        .buildButton())
                .toList();
    }

}
