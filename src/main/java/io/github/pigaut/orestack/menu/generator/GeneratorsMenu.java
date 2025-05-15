package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;

public class GeneratorsMenu extends GenericGroupsMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public GeneratorsMenu(String group) {
        super(StringFormatter.toTitleCase(group) + " Generators");

        for (GeneratorTemplate generator : plugin.getGeneratorTemplates(group)) {
            final Button button = Button.builder()
                    .withType(generator.getItem().getType())
                    .withDisplay(generator.getName(), StringStyle.TITLE)
                    .buildButton();

            this.addEntry(button);
        }
    }

}
