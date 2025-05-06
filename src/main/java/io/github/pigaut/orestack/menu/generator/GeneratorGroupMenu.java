package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GeneratorGroupMenu extends GenericPagedMenu {

    public GeneratorGroupMenu(OrestackPlugin plugin) {
        super("generator_group_menu", "Generator Groups");

        for (String group : plugin.getGeneratorTemplates().getGeneratorGroups()) {
            final ItemStack icon = IconBuilder.of(Material.CHEST)
                    .withDisplay(StringFormatter.toTitleCase(group))
                    .addLore("")
                    .buildIcon();

            this.addEntry(new GeneratorGroupButton(icon, group));
        }
    }

    private class GeneratorGroupButton extends GroupButton {

        public GeneratorGroupButton(@NotNull ItemStack icon, String group) {
            super(icon, group);
        }

        @Override
        public void onLeftClick(MenuView view, InventoryClickEvent event) {
            view.getViewer().openMenu(new GeneratorMenu(group));
        }

    }

}
