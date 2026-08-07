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

public class FramedEditor extends FramedMenu {

    private final ConfigRoot config;
    protected final ConfigSection section;

    public FramedEditor(ConfigSection section, String title, int size) {
        super(title, size);
        this.config = section.getRoot();
        this.section = section;
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
