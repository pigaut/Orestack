package io.github.pigaut.rpg.core.drop;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockItemDrop extends ItemDrop {

    private final Block block;

    BlockItemDrop(EnhancedPlugin plugin, Block block, ItemStack item, boolean fortune, boolean telepathy, boolean miningFortune, Material silkDrop, Material smeltedDrop) {
        super(plugin, item, Amount.fixed(item.getAmount()), null, true,
                fortune, false, telepathy, miningFortune, silkDrop, smeltedDrop);
        this.block = block;
    }

    public void spawn(@NotNull Context context) {
        spawn(context, ItemSpawnReason.UNKNOWN);
    }

    public void spawn(@NotNull Context context, @NotNull ItemSpawnReason spawnReason) {
        spawn(context.player(), context.tool(), spawnReason);
    }

    public void spawn(@Nullable Player player, @Nullable ItemStack tool) {
        spawn(player, tool, ItemSpawnReason.UNKNOWN);
    }

    public void spawn(@Nullable Player player, @Nullable ItemStack tool, @NotNull ItemSpawnReason spawnReason) {
        spawn(LocationUtil.centered(block.getLocation()), player, tool, spawnReason);
    }

}
