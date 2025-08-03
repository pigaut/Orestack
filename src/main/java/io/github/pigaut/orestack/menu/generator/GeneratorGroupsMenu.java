package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

import java.util.*;

public class GeneratorGroupsMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin;

    public GeneratorGroupsMenu(OrestackPlugin plugin) {
        super("Generator Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getGeneratorTemplates().getAllGroups().stream()
                .map(group -> Button.builder()
                        .withType(Material.CHEST)
                        .withDisplay("&6&l" + StringFormatter.toTitleCase(group))
                        .addLore("")
                        .addLore("&eLeft-Click: &fView all generators")
                        .addLore("&6Right-Click: &fGet all generators")
                        .onLeftClick((menuView, player, event) ->
                                player.openMenu(new GeneratorsMenu(plugin, group)))
                        .onRightClick((menuView, player, event) ->
                                player.performCommand("orestack generator get-group " + group))
                        .buildButton())
                .toList();
    }

}
