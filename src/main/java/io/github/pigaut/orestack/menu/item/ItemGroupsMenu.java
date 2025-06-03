package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ItemGroupsMenu extends FramedSelectionMenu {

    public ItemGroupsMenu(OrestackPlugin plugin) {
        super("Item Groups", MenuSize.BIG);

        for (String group : plugin.getItems().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&a&l" + StringFormatter.toTitleCase(group))
                    .addLore("")
                    .addLore("&eLeft-Click: &fView all items")
                    .addLore("&6Right-Click: &fGet all items")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new ItemsMenu(plugin, group)))
                    .onRightClick((menuView, event) -> menuView.getViewer().performCommand("orestack item get-group " + group))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
