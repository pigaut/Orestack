package io.github.pigaut.orestack.menu.function;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

import java.util.*;

public class FunctionGroupsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;

    public FunctionGroupsMenu(EnhancedPlugin plugin) {
        super("Function Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getFunctions().getAllGroups().stream()
                .map(group -> Button.builder()
                        .withType(Material.CHEST)
                        .withDisplay("&8&l" + StringFormatter.toTitleCase(group))
                        .addLore("")
                        .addLore("&eLeft-Click: &fView all functions")
                        .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new FunctionsMenu(plugin, group)))
                        .buildButton())
                .toList();
    }

}
