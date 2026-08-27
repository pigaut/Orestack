package io.github.pigaut.rpg.menu.function;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FunctionsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;

    public FunctionsMenu(EnhancedPlugin plugin, String group) {
        super(CaseFormatter.toTitleCase(group) + " Functions", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        return plugin.getGlobalFunctions().getAll(group).stream()
                .map(function -> Button.builder()
                        .type(function.getIcon().getType())
                        .name("&8&o" + CaseFormatter.toTitleCase(function.getName()))
                        .addEmptyLine()
                        .addLine("&eLeft-Click: &fRun function")
                        .onLeftClick((menuView, playerState) -> {
                            function.run(context);
                        })
                        .buildButton())
                .toList();
    }

    @Override
    public Button getToolbarButton4() {
        return Buttons.MAIN_MENU;
    }

}
