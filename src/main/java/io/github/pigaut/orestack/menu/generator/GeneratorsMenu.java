package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;

public class GeneratorsMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin;

    public GeneratorsMenu(OrestackPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Generators", MenuSize.BIG);
        this.plugin = plugin;

        for (GeneratorTemplate generator : plugin.getGeneratorTemplates(group)) {
            final String generatorName = generator.getName();

            final Button button = Button.builder()
                    .withType(generator.getItem().getType())
                    .withDisplay("&6&o" + StringFormatter.toTitleCase(generatorName))
                    .addLore("")
                    .addLore("&7Left-Click: &fGet Generator")
                    .onLeftClick((menuView, event) -> menuView.getViewer().performCommand("orestack generator get " + generatorName))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
