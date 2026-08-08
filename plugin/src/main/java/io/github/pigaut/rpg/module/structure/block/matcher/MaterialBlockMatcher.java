package io.github.pigaut.rpg.module.structure.block.matcher;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MaterialBlockMatcher implements BlockMatcher {

    private final Material material;

    public MaterialBlockMatcher(@NotNull Material material) {
        this.material = material;
    }

    @Override
    public boolean matchBlock(@NotNull Block block) {
        return block.getType() == material;
    }

    public static class Multi implements BlockMatcher {
        private final Material[] materials;

        public Multi(@NotNull List<Material> materials) {
            this.materials = materials.toArray(new Material[0]);
        }

        @Override
        public boolean matchBlock(@NotNull Block block) {
            Material other = block.getType();
            for (Material material : materials) {
                if (material == other) {
                    return true;
                }
            }
            return false;
        }
    }

}
