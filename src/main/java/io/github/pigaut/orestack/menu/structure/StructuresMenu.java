package io.github.pigaut.orestack.menu.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;

public class StructuresMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public StructuresMenu(String group) {
        super(StringFormatter.toTitleCase(group) + " Structures", MenuSize.BIG);
        for (BlockStructure structure : plugin.getStructures().getAll(group)) {
            final Button button = Button.builder()
                    .withType(structure.getIcon().getType())
                    .withDisplay("&e&o" + StringFormatter.toTitleCase(structure.getName()))
                    .buildButton();
            this.addEntry(button);
        }
    }

}
