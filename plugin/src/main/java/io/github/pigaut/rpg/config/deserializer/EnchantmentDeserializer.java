package io.github.pigaut.rpg.config.deserializer;

import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.yaml.configurator.convert.deserialize.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.enchantments.*;
import org.jetbrains.annotations.*;

public class EnchantmentDeserializer implements Deserializer<Enchantment> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid enchantment";
    }

    @Override
    public @NotNull Enchantment deserialize(@NotNull String enchantName) throws StringParseException {
        Enchantment enchantment = EnchantUtil.getEnchantment(enchantName);
        if (enchantment == null) {
            throw new StringParseException("Could not find enchant with name: " + enchantName);
        }
        return enchantment;
    }

}
