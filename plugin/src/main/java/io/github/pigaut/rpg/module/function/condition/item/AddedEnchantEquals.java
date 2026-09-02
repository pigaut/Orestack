package io.github.pigaut.rpg.module.function.condition.item;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
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
    public @NotNull FunctionResponse evaluate(@NotNull Context context) {
        EnchantLevel enchantAdded = context.enchantAdded();
        if (enchantAdded == null) {
            return new FunctionError("Event that triggered function is not an ItemEnchantEvent");
        }

        if (enchantAdded.enchant() != enchant) {
            return FunctionResponse.UNMET;
        }

        return FunctionResponse.met(level.match(enchantAdded.level()));
    }

}
