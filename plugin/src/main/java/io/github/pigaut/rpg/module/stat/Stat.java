package io.github.pigaut.rpg.module.stat;

import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class Stat {

    private final String name;

    public Stat(@NotNull String name) {
        this.name = CaseFormatter.toSnakeCase(name);
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Stat statType)) return false;
        return Objects.equals(name, statType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
