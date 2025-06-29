package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.voxel.core.item.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;

import java.util.*;

public class ItemsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;

    public ItemsMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Items", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getItems(group).stream()
                .map(item -> {
                    final String itemName = item.getName();
                    return Button.builder()
                            .withType(item.getItemStack().getType())
                            .withDisplay("&a&o" + StringFormatter.toTitleCase(itemName))
                            .addLore("")
                            .addLore("&eLeft-Click: &fGet item")
                            .onLeftClick((menuView, player, event) ->
                                    menuView.getViewer().performCommand("orestack item get " + itemName))
                            .buildButton();
                })
                .toList();
    }

}
