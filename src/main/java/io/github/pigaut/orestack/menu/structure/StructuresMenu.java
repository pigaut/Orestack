package io.github.pigaut.orestack.menu.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;

public class StructuresMenu extends GenericGroupsMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public StructuresMenu(String group) {
        super(StringFormatter.toTitleCase(group) + " Structures");
        for (BlockStructure structure : plugin.getStructures().getAll(group)) {
            this.addEntry(new Button(structure.getIcon()));
        }
    }

}
