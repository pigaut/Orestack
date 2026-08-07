package io.github.pigaut.rpg.config.deserializer;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.convert.deserialize.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class MaterialDeserializer implements Deserializer<Material> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid material";
    }

    @Override
    public @NotNull Material deserialize(@NotNull String materialName) throws StringParseException {
        Material material = MaterialUtil.getMaterial(materialName);
        if (material == null) {
            throw new StringParseException("Could not find material with name: " + materialName);
        }
        return material;
    }

    @Override
    public @NotNull Material loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String materialName = scalar.toString(CaseStyle.CONSTANT);
        Material material = MaterialUtil.getMaterial(materialName);
        if (material != null) {
            return material;
        }

        LatestMaterial latestMaterial = ParseUtil.parseEnumOrNull(LatestMaterial.class, materialName);
        if (latestMaterial != null) {
            ConfigRoot root = scalar.getRoot();
            root.collectWarning(new InvalidConfigException(scalar, "Material not available on this server version"));
            return Material.STONE;
        }

        throw new InvalidConfigException(scalar, "Could not find material with name: " + materialName);
    }

}
