package io.github.pigaut.orestack.menu.function;

import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;

import java.util.*;

public class FunctionsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;

    public FunctionsMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Functions", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getFunctions().getAll(group).stream()
                .map(function -> Button.builder()
                        .withType(function.getIcon().getType())
                        .withDisplay("&8&o" + StringFormatter.toTitleCase(function.getName()))
                        .addLore("")
                        .addLore("&eLeft-Click: &fRun function")
                        .onLeftClick((menuView, player, event) -> function.run(player))
                        .buildButton())
                .toList();
    }

}
