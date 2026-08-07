package io.github.pigaut.rpg.plugin;

public class PluginReloadInProgressException extends Exception {

    public PluginReloadInProgressException() {
        super("The plugin is already in the process of reloading. Please wait until the current reload is complete before starting another.");
    }

}
