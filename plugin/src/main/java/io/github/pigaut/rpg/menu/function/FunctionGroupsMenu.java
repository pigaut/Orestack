package io.github.pigaut.rpg.menu.function;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FunctionGroupsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;

    public FunctionGroupsMenu(EnhancedPlugin plugin) {
        super("Function Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        return plugin.getGlobalFunctions().getAllGroups().stream()
                .map(group -> Button.builder()
                        .type(Material.CHEST)
                        .name("&8&l" + CaseFormatter.toTitleCase(group))
                        .addEmptyLine()
                        .addLine("&eLeft-Click: &fView all functions")
                        .onLeftClick((menuView, player) -> menuView.getViewer().openMenu(new FunctionsMenu(plugin, group)))
                        .buildButton())
                .toList();
    }

}
