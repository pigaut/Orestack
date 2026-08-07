package io.github.pigaut.rpg.config.itemstack;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.item.options.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.item.options.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemStackLoader implements ConfigLoader.Section<ItemStack> {

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid item";
    }

    @Override
    public @NotNull ItemStack loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Material type = section.get("type|material", Material.class)
                .requireOrThrow(MaterialUtil::isNotAir, "item type must not be air");

        int amount = section.getInteger("amount").withDefault(1);

        ItemStack item = new ItemStack(type, amount);

        ItemOptions options = section.getRequired(ItemOptions.class);
        options.applyTo(item);

        return item;
    }

}
