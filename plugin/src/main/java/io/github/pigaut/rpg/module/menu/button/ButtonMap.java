package io.github.pigaut.rpg.module.menu.button;

import org.jetbrains.annotations.*;

import java.util.*;

public class ButtonMap {

    private final Map<String, ButtonTemplate> buttonsByName = new HashMap<>();

    public boolean contains(@NotNull String name) {
        return buttonsByName.containsKey(name);
    }

    public @Nullable ButtonTemplate get(@NotNull String name) {
        return buttonsByName.get(name);
    }

    public void put(@NotNull String name, @Nullable ButtonTemplate button) {
        buttonsByName.put(name, button);
    }

    @Override
    public String toString() {
        return buttonsByName.toString();
    }

}
