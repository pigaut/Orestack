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
                    .withDisplay(StringFormatter.toTitleCase(group))
                    .addLore("[left-click] to view all generators")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new GeneratorsMenu(group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
