package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class ItemGroupsMenu extends GenericGroupsMenu {

    public ItemGroupsMenu(OrestackPlugin plugin) {
        super("Item Groups");

        for (String group : plugin.getItems().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay(StringFormatter.toTitleCase(group))
                    .addLore("[left-click] to view all items")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new ItemsMenu(group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
