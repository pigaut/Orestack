package io.github.pigaut.rpg.core.tag;

import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface MaterialGroup {

    boolean contains(@NotNull Material material);

    @NotNull Set<Material> getMaterials();

    static Set<Material> toMaterials(@NotNull Collection<MaterialGroup> materialGroups) {
        Set<Material> materials = new HashSet<>();
        for (MaterialGroup group : materialGroups) {
            materials.addAll(group.getMaterials());
        }
        return materials;
    }

    static List<Material> toMaterialsList(@NotNull Collection<MaterialGroup> materialGroups) {
        List<Material> materials = new ArrayList<>();
        for (MaterialGroup group : materialGroups) {
            materials.addAll(group.getMaterials());
        }
        return materials;
    }

}
