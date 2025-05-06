package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.jetbrains.annotations.*;

public class GeneratorMenu extends GenericPagedMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public GeneratorMenu(String group) {
        super("generator_menu", StringFormatter.toTitleCase(group) + " Generators");

        for (GeneratorTemplate generator : plugin.getGeneratorTemplates(group)) {
            this.addEntry(new GeneratorButton(generator));
        }
    }

    private class GeneratorButton extends Button {

        private final GeneratorTemplate generator;

        public GeneratorButton(@NotNull GeneratorTemplate generator) {
            super(generator.getItem());
            this.generator = generator;
        }

        public GeneratorTemplate getGenerator() {
            return generator;
        }

    }

}
