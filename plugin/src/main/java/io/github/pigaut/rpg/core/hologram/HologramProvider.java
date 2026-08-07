package io.github.pigaut.rpg.core.hologram;

import io.github.pigaut.rpg.server.Server;

public enum HologramProvider {

    FANCY_HOLOGRAMS("FancyHolograms"),
    DECENT_HOLOGRAMS("DecentHolograms");

    private final String pluginName;

    HologramProvider(String pluginName) {
        this.pluginName = pluginName;
    }

    public boolean isAvailable() {
        return Server.isPluginEnabled(pluginName);
    }

}
