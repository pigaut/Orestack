package io.github.pigaut.rpg.plugin.manager;

public class DuplicateElementException extends Exception {

    public DuplicateElementException(String elementName) {
        super("An value with the name '" + elementName + "' already exists.");
    }

}
