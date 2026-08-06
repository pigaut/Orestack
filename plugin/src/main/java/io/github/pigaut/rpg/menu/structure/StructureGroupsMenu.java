package io.github.pigaut.rpg.menu.structure;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StructureGroupsMenu extends FramedSelectionMenu {

    private final RpgMakerPlugin plugin;

    public StructureGroupsMenu(RpgMakerPlugin plugin) {
        super("Structure Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
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
