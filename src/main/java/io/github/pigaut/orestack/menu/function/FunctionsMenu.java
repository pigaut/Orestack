package io.github.pigaut.orestack.menu.function;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.message.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;

public class FunctionsMenu extends GenericGroupsMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public FunctionsMenu(String group) {
        super(StringFormatter.toTitleCase(group) + " Functions");
        for (Function function : plugin.getFunctions().getAll(group)) {

            final Button button = Button.builder()
                    .withType(function.getIcon().getType())
                    .withDisplay("&8&o" + StringFormatter.toTitleCase(function.getName()))
                    .addLore("&7Left-Click: &fRun function")
                    .onLeftClick((menuView, event) -> function.run(menuView.getViewer()))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
