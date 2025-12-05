package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;

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
                        .withDisplay("&a&l" + CaseFormatter.toTitleCase(group))
                        .addLore("")
                        .addLore("&eLeft-Click: &fView all items")
                        .addLore("&6Right-Click: &fGet all items")
                        .onLeftClick((view, player) ->
                                player.openMenu(new ItemsMenu(plugin, group)))
                        .onRightClick((view, player) ->
                                player.performCommand("orestack item get-group " + group))
                        .buildButton())
                .toList();
    }

}
