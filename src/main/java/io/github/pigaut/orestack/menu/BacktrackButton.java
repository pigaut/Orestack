package io.github.pigaut.orestack.menu;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

public class BacktrackButton extends Button {

    private static final ItemStack icon;

    static {
        icon = IconBuilder.of(Material.SPRUCE_DOOR)
                .withDisplay("&c&lBack")
                .addLore("&7Left-Click: &fGo back to the previous menu")
                .enchanted(true)
                .buildIcon();
    }

    public BacktrackButton() {
        super(icon);
    }

    @Override
    public void onLeftClick(MenuView view, InventoryClickEvent event) {
        final MenuView previousView = view.getPreviousView();
        if (previousView != null) {
            view.getViewer().setOpenView(previousView);
        }
    }

}
