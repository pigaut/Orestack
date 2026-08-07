package io.github.pigaut.rpg.core.tag;

import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface MaterialTag {

    boolean contains(@NotNull Material material);

    @NotNull Set<Material> getMaterials();

}
