package io.github.pigaut.rpg.plugin;

import io.github.pigaut.rpg.server.*;

public class UnsupportedVersionException extends RuntimeException {

    public UnsupportedVersionException(EnhancedJavaPlugin plugin) {
        super(plugin.getName() + " v"  + plugin.getVersion() + " is not compatible with server version: " + Server.getVersion());
    }

}
