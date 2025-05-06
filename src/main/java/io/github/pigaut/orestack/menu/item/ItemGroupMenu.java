package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemGroupMenu extends GenericPagedMenu {

    public ItemGroupMenu(OrestackPlugin plugin) {
        super("item_group_menu", "Item Groups");

        for (String group : plugin.getItems().getItemGroups()) {
            final ItemStack icon = IconBuilder.of(Material.CHEST)
                    .withDisplay(StringFormatter.toTitleCase(group))
                    .addLore("")
                    .buildIcon();

            this.addEntry(new ItemGroupButton(icon, group));
        }
    }

    private class ItemGroupButton extends GroupButton {

        public ItemGroupButton(@NotNull ItemStack icon, String group) {
            super(icon, group);
        }

        @Override
        public void onLeftClick(MenuView view, InventoryClickEvent event) {
            view.getViewer().openMenu(new ItemMenu(group));
        }

    }

}
