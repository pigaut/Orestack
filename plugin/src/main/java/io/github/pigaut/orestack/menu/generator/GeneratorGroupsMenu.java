package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.convert.format.*;
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
                        .type(Material.CHEST)
                        .name("&6&l" + CaseFormatter.toTitleCase(group))
                        .addEmptyLine()
                        .addLine("&eLeft-Click: &fView all generators")
                        .addLine("&6Right-Click: &fGet all generators")
                        .onLeftClick((menuView, player) ->
                                player.openMenu(new GeneratorsMenu(plugin, group)))
                        .onRightClick((menuView, player) ->
                                player.performCommand("orestack generator get-group " + group))
                        .buildButton())
                .toList();
    }

}
