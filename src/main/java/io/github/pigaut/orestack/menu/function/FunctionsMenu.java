package io.github.pigaut.orestack.menu.function;

import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;

public class FunctionsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;

    public FunctionsMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Functions", MenuSize.BIG);
        this.plugin = plugin;
        for (Function function : plugin.getFunctions().getAll(group)) {

            final Button button = Button.builder()
                    .withType(function.getIcon().getType())
                    .withDisplay("&8&o" + StringFormatter.toTitleCase(function.getName()))
                    .addLore("")
                    .addLore("&eLeft-Click: &fRun function")
                    .onLeftClick((menuView, event) -> function.run(menuView.getViewer()))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
