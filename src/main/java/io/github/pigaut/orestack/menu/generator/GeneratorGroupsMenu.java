package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class GeneratorGroupsMenu extends FramedSelectionMenu {

    public GeneratorGroupsMenu(OrestackPlugin plugin) {
        super("Generator Groups", MenuSize.BIG);

        for (String group : plugin.getGeneratorTemplates().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&6&l" + StringFormatter.toTitleCase(group))
                    .addLore("")
                    .addLore("&eLeft-Click: &fView all generators")
                    .addLore("&6Right-Click: &fGet all generators")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new GeneratorsMenu(plugin, group)))
                    .onRightClick((menuView, event) -> menuView.getViewer().performCommand("orestack generator get-group " + group))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
