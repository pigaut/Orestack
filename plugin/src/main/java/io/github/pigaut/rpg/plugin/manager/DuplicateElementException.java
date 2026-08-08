package io.github.pigaut.rpg.plugin.manager;

import org.jetbrains.annotations.*;

public class DuplicateElementException extends Exception {

    public DuplicateElementException(@NotNull String elementName) {
        super("Found duplicate element with name: " + elementName);
    }

}
