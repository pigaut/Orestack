package io.github.pigaut.orestack.menu.item;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.item.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.jetbrains.annotations.*;

public class ItemMenu extends GenericPagedMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public ItemMenu(String group) {
        super("item_menu",  StringFormatter.toTitleCase(group) + " Items");
        for (Item item : plugin.getItems(group)) {
            this.addEntry(new ItemButton(item));
        }
    }

    private class ItemButton extends Button {

        public ItemButton(@NotNull Item item) {
            super(item.getItemStack());
        }

    }

}
