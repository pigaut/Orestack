package io.github.pigaut.orestack.menu.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;

import java.util.*;

public class StructuresMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();
    private final String group;

    public StructuresMenu(String group) {
        super(StringFormatter.toTitleCase(group) + " Structures", MenuSize.BIG);
        this.group = group;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getStructures().getAll(group).stream()
                .map(structure -> Button.builder()
                        .withType(structure.getIcon().getType())
                        .withDisplay("&e&o" + StringFormatter.toTitleCase(structure.getName()))
                        .buildButton())
                .toList();
    }

    @Override
    public Button getToolbarButton4() {
        return Buttons.MAIN_MENU;
    }

}
