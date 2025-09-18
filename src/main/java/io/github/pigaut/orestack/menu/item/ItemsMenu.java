package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.convert.format.*;

import java.util.*;

public class ItemsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;

    public ItemsMenu(EnhancedPlugin plugin, String group) {
        super(CaseFormatter.toTitleCase(group) + " Items", MenuSize.BIG);
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
                            .withDisplay("&a&o" + CaseFormatter.toTitleCase(itemName))
                            .addLore("")
                            .addLore("&eLeft-Click: &fGet item")
                            .onLeftClick((menuView, player, event) ->
                                    menuView.getViewer().performCommand("orestack item get " + itemName))
                            .buildButton();
                })
                .toList();
    }

    @Override
    public Button getToolbarButton4() {
        return Buttons.MAIN_MENU;
    }

}
