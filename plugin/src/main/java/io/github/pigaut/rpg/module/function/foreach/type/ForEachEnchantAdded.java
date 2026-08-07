package io.github.pigaut.rpg.module.function.foreach.type;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import org.bukkit.event.*;
import org.bukkit.event.enchantment.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ForEachEnchantAdded implements ForEachSource<EnchantLevel> {

    @Override
    public @NotNull List<EnchantLevel> getElements(@NotNull Context context) {
        Event event = context.event();
        if (event instanceof EnchantItemEvent enchantItemEvent) {
            List<EnchantLevel> enchantsAdded = new ArrayList<>();
            enchantItemEvent.getEnchantsToAdd().forEach((enchant, level) -> {
                enchantsAdded.add(new EnchantLevel(enchant, level));
            });
            return enchantsAdded;
        }
        return List.of();
    }

}
