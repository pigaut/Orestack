package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class GeneratorGroupsMenu extends GenericGroupsMenu {

    public GeneratorGroupsMenu(OrestackPlugin plugin) {
        super("Generator Groups");

        for (String group : plugin.getGeneratorTemplates().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&6&l" + StringFormatter.toTitleCase(group))
                    .addLore("&7Left-Click: &fView all")
                    .addLore("&7Right-Click: &fGet all")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new GeneratorsMenu(group)))
                    .onRightClick((menuView, event) -> menuView.getViewer().performCommand("orestack generator get-group " + group))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
