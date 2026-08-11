package io.github.pigaut.rpg.config.deserializer;

import com.ticxo.modelengine.api.utils.data.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.bukkit.potion.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.convert.deserialize.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;

public class PotionTypeDeserializer implements Deserializer<PotionType> {

    @Override
    public @NotNull PotionType deserialize(@NotNull String potionName) throws StringParseException {
        PotionType potion = ParseUtil.parseEnumOrNull(PotionType.class, potionName);
        if (potion == null) {
            throw new StringParseException("Could not find potion with name: " + potionName);
        }
        return potion;
    }

    @Override
    public @NotNull PotionType loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String potionName = scalar.toString(CaseStyle.CONSTANT);
        PotionType potion = ParseUtil.parseEnumOrNull(PotionType.class, potionName);
        if (potion != null) {
            return potion;
        }

        LatestPotion latestPotionType = ParseUtil.parseEnumOrNull(LatestPotion.class, potionName);
        if (latestPotionType != null) {
            ConfigRoot root = scalar.getRoot();
            root.collectWarning(new InvalidConfigException(scalar, "Potion not available on this server version"));
            return PotionType.AWKWARD;
        }

        throw new InvalidConfigException(scalar, "Could not find potion with name: " + potionName);
    }

}
