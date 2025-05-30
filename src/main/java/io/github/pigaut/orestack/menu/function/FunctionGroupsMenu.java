package io.github.pigaut.orestack.menu.function;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class FunctionGroupsMenu extends FramedSelectionMenu {

    public FunctionGroupsMenu(OrestackPlugin plugin) {
        super("Function Groups", MenuSize.BIG);

        for (String group : plugin.getFunctions().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&8&l" + StringFormatter.toTitleCase(group))
                    .addLore("")
                    .addLore("&7Left-Click: &fView all")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new FunctionsMenu(plugin, group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
