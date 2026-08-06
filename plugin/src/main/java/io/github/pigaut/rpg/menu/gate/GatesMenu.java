package io.github.pigaut.rpg.menu.gate;

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

public class GatesMenu extends FramedSelectionMenu {

    private final RpgMakerPlugin plugin;
    private final String group;

    public GatesMenu(RpgMakerPlugin plugin, String group) {
        super(CaseFormatter.toTitleCase(group) + " Gates", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        return plugin.getGateTemplates(group).stream()
                .map(gate -> {
                    String gateName = gate.getName();
                    return Button.builder()
                            .type(gate.getItemType())
                            .name("&6&o" + CaseFormatter.toTitleCase(gateName))
                            .addEmptyLine()
                            .addLine("&eLeft-Click: &fGet Gate")
                            .onLeftClick((menuView, player) ->
                                    player.performCommand("castlegates gate get " + gateName))
                            .buildButton();
                })
                .toList();
    }

    @Override
    public Button getToolbarButton4() {
        return Buttons.MAIN_MENU;
    }

}
