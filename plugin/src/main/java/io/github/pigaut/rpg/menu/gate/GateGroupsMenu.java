package io.github.pigaut.rpg.menu.gate;

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

public class GateGroupsMenu extends FramedSelectionMenu {

    private final RpgMakerPlugin plugin;

    public GateGroupsMenu(RpgMakerPlugin plugin) {
        super("Gate Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        return plugin.getGateTemplates().getAllGroups().stream()
                .map(group -> Button.builder()
                        .type(Material.CHEST)
                        .name("&6&l" + CaseFormatter.toTitleCase(group))
                        .addEmptyLine()
                        .addLine("&eLeft-Click: &fView all gates")
                        .addLine("&6Right-Click: &fGet all gates")
                        .onLeftClick((menuView, player) ->
                                player.openMenu(new GatesMenu(plugin, group)))
                        .onRightClick((menuView, player) ->
                                player.performCommand("castlegates gate get-group " + group))
                        .buildButton())
                .toList();
    }

}
