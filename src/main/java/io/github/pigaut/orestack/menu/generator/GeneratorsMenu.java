package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;

import java.util.*;

public class GeneratorsMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin;
    private final String group;

    public GeneratorsMenu(OrestackPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Generators", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getGeneratorTemplates(group).stream()
                .map(generator -> {
                    final String generatorName = generator.getName();
                    return Button.builder()
                            .withType(generator.getItem().getType())
                            .withDisplay("&6&o" + StringFormatter.toTitleCase(generatorName))
                            .addLore("")
                            .addLore("&eLeft-Click: &fGet Generator")
                            .onLeftClick((menuView, player, event) ->
                                    player.performCommand("orestack generator get " + generatorName))
                            .buildButton();
                })
                .toList();
    }

    @Override
    public Button getToolbarButton4() {
        return Buttons.MAIN_MENU;
    }

}
