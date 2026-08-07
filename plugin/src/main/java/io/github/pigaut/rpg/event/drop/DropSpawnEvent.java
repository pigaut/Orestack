package io.github.pigaut.rpg.event.drop;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.event.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.event.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DropSpawnEvent extends CancellableEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final EnhancedPlugin plugin;

    private ItemStack item;
    private int amount;
    private final ItemSpawnReason spawnReason;

    private final @Nullable Player player;
    private @Nullable ItemStack tool;

    private boolean allowFortune;
    private boolean allowLooting;
    private boolean allowTelepathy;
    private boolean allowMiningFortune;
    private @Nullable Material silkDrop;
    private @Nullable Material smeltedDrop;

    private ItemStack[] drops = null;

    public DropSpawnEvent(@NotNull EnhancedPlugin plugin,
                          @NotNull ItemStack item, int amount, @NotNull ItemSpawnReason spawnCause,
                          @Nullable Player player, @Nullable ItemStack tool,
                          boolean allowFortune, boolean allowLooting, boolean allowTelepathy,
                          boolean miningFortune,
                          @Nullable Material silkDrop, @Nullable Material smeltedDrop) {
        this.plugin = plugin;
        this.spawnReason = spawnCause;
        this.item = item;
        this.amount = amount;
        this.player = player;
        this.tool = tool;
        this.allowFortune = allowFortune;
        this.allowLooting = allowLooting;
        this.allowTelepathy = allowTelepathy;
        this.allowMiningFortune = miningFortune;
        this.silkDrop = silkDrop;
        this.smeltedDrop = smeltedDrop;
    }

    public @NotNull ItemSpawnReason getSpawnReason() {
        return spawnReason;
    }

    public @NotNull ItemStack getItem() {
        return item;
    }

    public void setItem(@NotNull ItemStack item) {
        this.item = item;
        this.drops = null;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.drops = null;
    }

    public @Nullable Player getPlayer() {
        return player;
    }

    public @Nullable ItemStack getTool() {
        return tool;
    }

    public void setTool(@Nullable ItemStack tool) {
        this.tool = tool;
        this.drops = null;
    }

    public boolean isAllowFortune() {
        return allowFortune;
    }

    public void setAllowFortune(boolean allowFortune) {
        this.allowFortune = allowFortune;
        this.drops = null;
    }

    public boolean isAllowLooting() {
        return allowLooting;
    }

    public void setAllowLooting(boolean allowLooting) {
        this.allowLooting = allowLooting;
        this.drops = null;
    }

    public boolean isAllowTelepathy() {
        return allowTelepathy;
    }

    public void setAllowTelepathy(boolean telepathy) {
        this.allowTelepathy = telepathy;
        this.drops = null;
    }

    public boolean isAllowMiningFortune() {
        return allowMiningFortune;
    }

    public void setAllowMiningFortune(boolean allowMiningFortune) {
        this.allowMiningFortune = allowMiningFortune;
    }

    public @Nullable Material getSilkDrop() {
        return silkDrop;
    }

    public void setSilkDrop(@Nullable Material silkDrop) {
        this.silkDrop = silkDrop;
        this.drops = null;
    }

    public @Nullable Material getSmeltedDrop() {
        return smeltedDrop;
    }

    public void setSmeltedDrop(@Nullable Material smeltedDrop) {
        this.smeltedDrop = smeltedDrop;
        this.drops = null;
    }

    public @NotNull ItemStack[] getDrops() {
        if (drops != null) {
            return drops;
        }

        if (tool == null) {
            ItemStack drop = item.clone();
            drop.setAmount(amount);
            return (drops = new ItemStack[] { drop });
        }

        Settings settings = plugin.getSettings();

        // Fortune and Looting
        int dropAmount = amount;
        if (allowFortune && !settings.isFortuneEnchantAsStat()) {
            dropAmount = Fortune.getDropAmount(tool, dropAmount);
        } else if (allowLooting) {
            dropAmount = Looting.getDropAmount(tool, dropAmount);
        }

        // Mining Fortune
        if (allowMiningFortune && player != null) {
            PlayerState playerState = plugin.getPlayerState(player);
            int miningFortune = playerState.getMiningFortune();
            int guaranteed = miningFortune / 100;
            double rollChance = (miningFortune % 100) / 100.0;
            int bonus = Probability.test(rollChance) ? 1 : 0;
            dropAmount += guaranteed + bonus;
        }

        // Silk Touch
        if (silkDrop != null && EnchantUtil.hasEnchant(tool, Enchants.SILK_TOUCH)) {
            ItemStack drop = new ItemStack(silkDrop, 1);
            return (drops = new ItemStack[] { drop });
        }

        // Auto Smelt
        if (smeltedDrop != null) {
            double smeltChance = settings.getSmeltChance(tool);
            return (drops = AutoSmelt.getDrops(item, dropAmount, smeltedDrop, smeltChance));
        }

        ItemStack drop = item.clone();
        drop.setAmount(dropAmount);
        return (drops = new ItemStack[] { drop });
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
