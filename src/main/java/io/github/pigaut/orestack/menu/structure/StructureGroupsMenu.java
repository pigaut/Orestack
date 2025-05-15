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
                    .withDisplay(StringFormatter.toTitleCase(group))
                    .addLore("[left-click] to view all structures")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new StructuresMenu(group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
