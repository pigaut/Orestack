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
                    .withDisplay("&a&l" + StringFormatter.toTitleCase(group))
                    .addLore("&7Left-Click: &fView all")
                    .addLore("&7Right-Click: &fGet all")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new ItemsMenu(group)))
                    .onRightClick((menuView, event) -> menuView.getViewer().performCommand("orestack item get-group " + group))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
