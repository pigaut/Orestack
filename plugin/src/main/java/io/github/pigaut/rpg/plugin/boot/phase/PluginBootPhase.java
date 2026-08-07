package io.github.pigaut.rpg.plugin.boot.phase;

import org.jetbrains.annotations.*;

import java.util.*;

public class PluginBootPhase implements BootPhase {

    private final String pluginName;
    private final String pluginPhase;

    public PluginBootPhase(@NotNull String pluginName, @Nullable String pluginPhase) {
        this.pluginName = pluginName;
        this.pluginPhase = pluginPhase;
    }

    @Override
    public String getNamespace() {
        return pluginName;
    }

    @Override
    public String getKey() {
        return pluginPhase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluginBootPhase other)) return false;
        if (!Objects.equals(this.pluginName, other.pluginName)) return false;
        if (this.pluginPhase == null || other.pluginPhase == null) return true;
        return Objects.equals(this.pluginPhase, other.pluginPhase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pluginName);
    }

}
