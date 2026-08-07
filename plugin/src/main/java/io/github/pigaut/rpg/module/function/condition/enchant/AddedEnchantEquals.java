package io.github.pigaut.rpg.module.function.condition.enchant;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.enchantments.*;
import org.jetbrains.annotations.*;

public class AddedEnchantEquals implements Condition {

    private final Enchantment enchant;
    private final Amount level;

    public AddedEnchantEquals(@NotNull Enchantment enchant, @NotNull Amount level) {
        this.enchant = enchant;
        this.level = level;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        EnchantLevel enchantAdded = context.enchantAdded();
        if (enchantAdded == null) {
            return null;
        }

        if (enchantAdded.enchant() != enchant) {
            return false;
        }

        return level.match(enchantAdded.level());
    }

}
