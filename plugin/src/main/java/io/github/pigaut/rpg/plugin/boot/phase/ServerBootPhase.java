package io.github.pigaut.rpg.plugin.boot.phase;

import org.jetbrains.annotations.*;

import java.util.*;

public class ServerBootPhase implements BootPhase {

    private static final String NAMESPACE = "server";
    private final String key;

    public ServerBootPhase(@NotNull String key) {
        this.key = key;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ServerBootPhase other)) return false;
        return Objects.equals(key, other.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }
}
