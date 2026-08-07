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
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemDrop {

    private final EnhancedPlugin plugin;

    private final ItemStack drop;
    private final Amount amount;
    private final @Nullable Double chance;
    private boolean throwTowardsPlayer;
    private final boolean fortune;
    private final boolean looting;
    private final boolean telepathy;
    private final boolean miningFortune;
    private final Material silkDrop;
    private final Material smeltedDrop;

    public ItemDrop(EnhancedPlugin plugin, ItemStack item, Amount amount, boolean throwTowardsPlayer,
                    boolean fortune, boolean looting, boolean telepathy,
                    boolean miningFortune,
                    Material silkDrop, Material smeltedDrop) {
        this(plugin, item, amount, null, throwTowardsPlayer, fortune, looting, telepathy, miningFortune, silkDrop, smeltedDrop);
    }

    public ItemDrop(EnhancedPlugin plugin, ItemStack item, Amount amount,
                    @Nullable Double chance, boolean throwTowardsPlayer,
                    boolean fortune, boolean looting, boolean telepathy, boolean miningFortune,
                    Material silkDrop, Material smeltedDrop) {
        this.plugin = plugin;
        this.drop = item;
        this.amount = amount;
        this.chance = chance;
        this.throwTowardsPlayer = throwTowardsPlayer;
        this.fortune = fortune;
        this.looting = looting;
        this.telepathy = telepathy;
        this.miningFortune = miningFortune;
        this.silkDrop = silkDrop;
        this.smeltedDrop = smeltedDrop;
    }

    public static @NotNull BlockItemDrop fromBlock(@NotNull EnhancedPlugin plugin, @NotNull Block block, @NotNull ItemStack item) {
        Settings settings = plugin.getSettings();
        Material itemType = item.getType();
        boolean fortune = settings.isFortuneDrop(itemType);
        boolean telepathy = settings.isTelepathy();
        boolean miningFortune = settings.isMiningFortuneDrop(itemType);
        Material silkDrop = settings.getSilkDrop(itemType);
        Material smeltedDrop = settings.getSmeltedDrop(itemType);
        return new BlockItemDrop(plugin, block, item, fortune, telepathy, miningFortune, silkDrop, smeltedDrop);
    }

    public static @NotNull List<BlockItemDrop> fromBlock(@NotNull EnhancedPlugin plugin, @NotNull Block block) {
        List<BlockItemDrop> drops = new ArrayList<>();
        for (ItemStack item : block.getDrops()) {
            drops.add(fromBlock(plugin, block, item));
        }
        return drops;
    }

    public boolean isThrowTowardsPlayer() {
        return throwTowardsPlayer;
    }

    public void setThrowTowardsPlayer(boolean throwTowardsPlayer) {
        this.throwTowardsPlayer = throwTowardsPlayer;
    }

    public void spawn(@NotNull Location location, @NotNull Context context) {
        spawn(location, context, ItemSpawnReason.UNKNOWN);
    }

    public void spawn(@NotNull Location location, @NotNull Context context, @NotNull ItemSpawnReason spawnReason) {
        spawn(location, context.player(), context.tool(), spawnReason);
    }

    public void spawn(@NotNull Location location, @Nullable Player player, @Nullable ItemStack tool) {
        spawn(location, player, tool, ItemSpawnReason.UNKNOWN);
    }

    public void spawn(@NotNull Location location, @Nullable Player player, @Nullable ItemStack tool, @NotNull ItemSpawnReason spawnReason) {
        if (chance != null && !Probability.test(chance)) {
            return;
        }

        if (throwTowardsPlayer && player != null) {
            Vector playerDirection = player.getLocation().toVector()
                    .subtract(location.toVector())
                    .normalize()
                    .multiply(0.3);
            location = location.clone().add(playerDirection);
        }

        DropSpawnEvent itemSpawnEvent = new DropSpawnEvent(plugin, drop, amount.intValue(), spawnReason,
                player, tool, fortune, looting, telepathy, miningFortune, silkDrop, smeltedDrop);

        Server.callEvent(itemSpawnEvent);
        ItemStack[] drops = itemSpawnEvent.getDrops();

        if (itemSpawnEvent.isAllowTelepathy() && player != null && tool != null) {
            double telepathyChance = plugin.getSettings().getTelepathyChance(tool);
            if (Probability.test(telepathyChance)) {
                PlayerUtil.giveItemsOrDrop(player, drops);
                return;
            }
        }

        ItemUtil.dropItems(location, drops);
    }

    public void give(@NotNull Player player) {
        give(player, null, ItemSpawnReason.UNKNOWN);
    }

    public void give(@NotNull Player player, @NotNull ItemSpawnReason spawnReason) {
        give(player, null, spawnReason);
    }

    public void give(@NotNull Player player, @Nullable ItemStack tool) {
        give(player, tool, ItemSpawnReason.UNKNOWN);
    }

    public void give(@NotNull Player player, @Nullable ItemStack tool, @NotNull ItemSpawnReason spawnReason) {
        DropSpawnEvent itemSpawnEvent = new DropSpawnEvent(plugin, drop, amount.intValue(), spawnReason,
                player, tool, fortune, looting, true, miningFortune, silkDrop, smeltedDrop);

        Server.callEvent(itemSpawnEvent);
        PlayerUtil.giveItemsOrDrop(player, itemSpawnEvent.getDrops());
    }

}
