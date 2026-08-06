package io.github.pigaut.rpg.menu.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorsMenu extends FramedSelectionMenu {

    private final RpgMakerPlugin plugin;
    private final String group;

    public GeneratorsMenu(RpgMakerPlugin plugin, String group) {
        super(CaseFormatter.toTitleCase(group) + " Generators", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
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
