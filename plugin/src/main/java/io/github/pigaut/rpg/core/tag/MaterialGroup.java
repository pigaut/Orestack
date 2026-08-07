package io.github.pigaut.rpg.core.tag;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.bukkit.material.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public enum MaterialGroup implements MaterialTag {

    OAK(Set.of(
            "OAK_LOG", "OAK_WOOD",
            "STRIPPED_OAK_LOG", "STRIPPED_OAK_WOOD"
    )),
    SPRUCE(Set.of(
            "SPRUCE_LOG", "SPRUCE_WOOD",
            "STRIPPED_SPRUCE_LOG", "STRIPPED_SPRUCE_WOOD"
    )),
    BIRCH(Set.of(
            "BIRCH_LOG", "BIRCH_WOOD",
            "STRIPPED_BIRCH_LOG", "STRIPPED_BIRCH_WOOD"
    )),
    JUNGLE(Set.of(
            "JUNGLE_LOG", "JUNGLE_WOOD",
            "STRIPPED_JUNGLE_LOG", "STRIPPED_JUNGLE_WOOD"
    )),
    ACACIA(Set.of(
            "ACACIA_LOG", "ACACIA_WOOD",
            "STRIPPED_ACACIA_LOG", "STRIPPED_ACACIA_WOOD"
    )),
    DARK_OAK(Set.of(
            "DARK_OAK_LOG", "DARK_OAK_WOOD",
            "STRIPPED_DARK_OAK_LOG", "STRIPPED_DARK_OAK_WOOD"
    )),
    MANGROVE(Set.of(
            "MANGROVE_LOG", "MANGROVE_WOOD",
            "STRIPPED_MANGROVE_LOG", "STRIPPED_MANGROVE_WOOD"
    )),
    CHERRY(Set.of(
            "CHERRY_LOG", "CHERRY_WOOD",
            "STRIPPED_CHERRY_LOG", "STRIPPED_CHERRY_WOOD"
    )),
    PALE_OAK(Set.of(
            "PALE_OAK_LOG", "PALE_OAK_WOOD",
            "STRIPPED_PALE_OAK_LOG", "STRIPPED_PALE_OAK_WOOD"
    )),
    CRIMSON(Set.of(
            "CRIMSON_STEM", "CRIMSON_HYPHAE",
            "STRIPPED_CRIMSON_STEM", "STRIPPED_CRIMSON_HYPHAE"
    )),
    WARPED(Set.of(
            "WARPED_STEM", "WARPED_HYPHAE",
            "STRIPPED_WARPED_STEM", "STRIPPED_WARPED_HYPHAE"
    )),
    BAMBOO(Set.of(
            "BAMBOO_BLOCK", "STRIPPED_BAMBOO_BLOCK"
    ));

    private final Set<Material> materials = new HashSet<>();

    MaterialGroup(Set<String> materialNames) {
        for (String materialName : materialNames) {
            Material material = MaterialUtil.getMaterial(materialName);
            if (material != null) {
                materials.add(material);
            }
        }
    }

    public @NotNull Set<Material> getMaterials() {
        return new HashSet<>(materials);
    }

    public boolean contains(@NotNull Material material) {
        return materials.contains(material);
    }

}
