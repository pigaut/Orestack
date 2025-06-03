package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.voxel.core.item.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;

public class ItemsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;

    public ItemsMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Items", MenuSize.BIG);
        this.plugin = plugin;
        for (Item item : plugin.getItems(group)) {
            final String itemName = item.getName();

            final Button button = Button.builder()
                    .withType(item.getItemStack().getType())
                    .withDisplay("&a&o" + StringFormatter.toTitleCase(itemName))
                    .addLore("")
                    .addLore("&eLeft-Click: &fGet item")
                    .onLeftClick((menuView, event) -> menuView.getViewer().performCommand("orestack item get " + itemName))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
