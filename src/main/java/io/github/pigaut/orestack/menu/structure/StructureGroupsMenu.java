package io.github.pigaut.orestack.menu.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

import java.util.*;

public class StructureGroupsMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin;

    public StructureGroupsMenu(OrestackPlugin plugin) {
        super("Structure Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getStructures().getAllGroups().stream()
                .map(group -> Button.builder()
                        .withType(Material.CHEST)
                        .withDisplay("&e&l" + StringFormatter.toTitleCase(group))
                        .addLore("")
                        .addLore("&eLeft-Click: &fView all")
                        .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new StructuresMenu(group)))
                        .buildButton())
                .toList();
    }
}
