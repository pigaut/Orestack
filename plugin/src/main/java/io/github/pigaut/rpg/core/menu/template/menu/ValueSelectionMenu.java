package io.github.pigaut.rpg.core.menu.template.menu;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ValueSelectionMenu extends FramedSelectionMenu {

    private final List<ValueInputButton> values;

    public ValueSelectionMenu(String title, int size, List<ValueInputButton> values) {
        super(title, size);
        this.values = values;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        return new ArrayList<>(values);
    }

}
