package io.github.pigaut.rpg.core.drop;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemDropLoader implements ConfigLoader.Line<ItemDrop> {

    private final EnhancedPlugin plugin;

    public ItemDropLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid item drop";
    }

    @Override
    public @NotNull ItemDrop loadFromLine(ConfigLine line) throws InvalidConfigException {
        ItemStack item;
        if (line.size() <= 1) {
            item = line.getRequired(0, ItemStack.class);
        } else {
            item = line.getRequired(1, ItemStack.class);
        }

        Amount amount = line.get("amount", Amount.class)
                .withDefault(Amount.ONE);

        Double chance = line.getDouble("chance")
                .require(Requirements.between(0, 1))
                .withDefault(null);

        String action = line.getRequiredString(0, CaseStyle.CONSTANT);

        boolean towardsPlayer = line.getBoolean("towardPlayer|towardsPlayer")
                .withDefault(action.equalsIgnoreCase("DROP_ITEM_AT_BLOCK")
                        || action.equalsIgnoreCase("DROP_ITEM_AT_COORDS"));

        boolean fortune = line.getBoolean("fortune|doFortune|applyFortune")
                .withDefault(plugin.getSettings().isFortuneDrop(item.getType()));

        boolean looting = line.getBoolean("looting|doLooting|applyLooting")
                .withDefault(plugin.getSettings().isLootingDrop(item.getType()));

        boolean telepathy = line.getBoolean("telepathy|telekinesis")
                .withDefault(plugin.getSettings().isTelepathy());

        boolean miningFortune = line.getBoolean("miningFortune|applyMiningFortune")
                .withDefault(plugin.getSettings().isMiningFortuneDrop(item.getType()));

        Material originalDrop = item.getType();

        Material silkDrop = line.get("silk|silkDrop", Material.class)
                .withDefault(plugin.getSettings().getSilkDrop(originalDrop));

        Material smeltedDrop = line.get("smelted|smeltedDrop", Material.class)
                .withDefault(plugin.getSettings().getSmeltedDrop(originalDrop));

        return new ItemDrop(plugin, item, amount,
                chance, towardsPlayer, fortune, looting, telepathy,
                miningFortune,
                silkDrop, smeltedDrop);
    }

}
