package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.item.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;

public class ItemsMenu extends GenericGroupsMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public ItemsMenu(String group) {
        super(StringFormatter.toTitleCase(group) + " Items");
        for (Item item : plugin.getItems(group)) {
            final String itemName = item.getName();

            final Button button = Button.builder()
                    .withType(item.getItemStack().getType())
                    .withDisplay("&a&o" + StringFormatter.toTitleCase(itemName))
                    .addLore("&7Left-Click: &fGet item")
                    .onLeftClick((menuView, event) -> menuView.getViewer().performCommand("orestack item get " + itemName))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
