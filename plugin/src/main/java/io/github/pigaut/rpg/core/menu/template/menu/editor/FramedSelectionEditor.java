package io.github.pigaut.rpg.core.menu.template.menu.editor;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

public class FramedSelectionEditor extends FramedSelectionMenu {

    private final ConfigRoot config;

    public FramedSelectionEditor(ConfigRoot config, String title, int size) {
        super(title, size);
        this.config = config;
    }

    @Override
    public boolean keepOpen() {
        return true;
    }

    @Override
    public void onClose(MenuView view) {
        config.save();
    }

    @Override
    public Button getToolbarButton5() {
        return new BackSaveButton(config);
    }

}
