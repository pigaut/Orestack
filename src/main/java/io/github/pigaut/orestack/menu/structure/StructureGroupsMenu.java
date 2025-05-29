package io.github.pigaut.orestack.menu.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class StructureGroupsMenu extends GenericGroupsMenu {

    public StructureGroupsMenu(OrestackPlugin plugin) {
        super("Structure Groups");

        for (String group : plugin.getStructures().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&e&l" + StringFormatter.toTitleCase(group))
                    .addLore("&7Left-Click: &fView all")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new StructuresMenu(group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
