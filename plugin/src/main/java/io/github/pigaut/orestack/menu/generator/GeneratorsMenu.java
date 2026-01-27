package io.github.pigaut.orestack.menu.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.convert.format.*;

import java.util.*;

public class GeneratorsMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin;
    private final String group;

    public GeneratorsMenu(OrestackPlugin plugin, String group) {
        super(CaseFormatter.toTitleCase(group) + " Generators", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getGeneratorTemplates(group).stream()
                .map(generator -> {
                    String generatorName = generator.getName();
                    return Button.builder()
                            .type(generator.getItemType())
                            .name("&6&o" + CaseFormatter.toTitleCase(generatorName))
                            .addEmptyLine()
                            .addLine("&eLeft-Click: &fGet Generator")
                            .onLeftClick((menuView, player) ->
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
