package io.github.pigaut.rpg.core.tag.config;

import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MaterialGroupLoader implements ConfigLoader<MaterialGroup> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid material(s)";
    }

    @Override
    public @NotNull MaterialGroup loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String name = scalar.toString(CaseStyle.CONSTANT);
        MaterialTag group = ParseUtil.parseEnumOrNull(MaterialTag.class, name);
        if (group != null) {
            return group;
        }

        Material material = ParseUtil.parseEnumOrNull(Material.class, name);
        if (material != null) {
            return new MaterialGroup() {
                private final Set<Material> materials = Set.of(material);

                @Override
                public boolean contains(@NotNull Material material) {
                    return materials.contains(material);
                }

                @Override
                public @NotNull Set<Material> getMaterials() {
                    return new HashSet<>(materials);
                }
            };
        }

        throw new InvalidConfigException(scalar, "Could not find material/group with name: " + name);
    }

}
