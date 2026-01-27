package io.github.pigaut.orestack.menu.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.convert.format.*;
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
                        .type(Material.CHEST)
                        .name("&e&l" + CaseFormatter.toTitleCase(group))
                        .addEmptyLine()
                        .addLine("&eLeft-Click: &fView all")
                        .onLeftClick((menuView, player) -> menuView.getViewer().openMenu(new StructuresMenu(group)))
                        .buildButton())
                .toList();
    }

}
