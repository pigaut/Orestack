package io.github.pigaut.rpg.listener.item;

import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.view.*;
import org.jetbrains.annotations.*;

public class ItemEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public ItemEventListener(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getSettings().isBreakingPower()) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();
        Context context = Context.fromPlayerAndBlock(plugin, player, block, event);

        ItemStack tool = player.getInventory().getItemInMainHand();
        ItemUtil.checkBreakingPower(event, context, block, tool);
        if (event.isCancelled()) {
            return;
        }

        ItemTemplate toolTemplate = plugin.getItemTemplate(tool);
        if (toolTemplate != null) {
            Function onMineBlock = toolTemplate.getOnMineBlock();
            if (onMineBlock != null) {
                onMineBlock.run(context);
                if (event.isCancelled()) {
                    return;
                }
            }
        }

        for (ItemStack equipment : PlayerUtil.getEquippedItems(player)) {
            ItemTemplate equipmentTemplate = plugin.getItemTemplate(equipment);
            if (equipmentTemplate == null) {
                continue;
            }

            Function onBlockBreak = equipmentTemplate.getOnBlockBreak();
            if (onBlockBreak == null) {
                continue;
            }

            onBlockBreak.run(context);
            if (event.isCancelled()) {
                return;
            }
        }

        event.setDropItems(false);
        for (BlockItemDrop blockDrop : ItemDrop.fromBlock(plugin, block)) {
            blockDrop.spawn(context, ItemSpawnReason.BLOCK_DROPS);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGeneratorMine(GeneratorMineEvent event) {
        if (!plugin.getSettings().isBreakingPower()) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlockMined();
        Context context = Context.fromPlayerAndBlock(plugin, player, block);

        ItemStack tool = player.getInventory().getItemInMainHand();
        ItemUtil.checkBreakingPower(event, context, block, tool);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        ItemTemplate itemTemplate = plugin.getItemTemplate(item);
        if (itemTemplate == null) {
            return;
        }

        if (itemTemplate.isUnplaceable()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false) // Clicks with no result are treated as cancelled
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        ItemTemplate itemTemplate = plugin.getItemTemplate(event.getItem());
        if (itemTemplate == null) {
            return;
        }

        Block block = event.getClickedBlock();
        Action action = event.getAction();
        Player player = event.getPlayer();
        Context context = Context.fromPlayerAndBlock(plugin, player, block, action, event);

        Function onLeftClick = itemTemplate.getOnLeftClick();
        Function onRightClick = itemTemplate.getOnRightClick();

        switch (action) {
            case LEFT_CLICK_BLOCK -> {
                if (onLeftClick != null) onLeftClick.run(context);

                Function onLeftClickBlock = itemTemplate.getOnLeftClickBlock();
                if (onLeftClickBlock != null) onLeftClickBlock.run(context);
            }

            case LEFT_CLICK_AIR -> {
                if (onLeftClick != null) onLeftClick.run(context);

                Function onLeftClickAir = itemTemplate.getOnLeftClickAir();
                if (onLeftClickAir != null) onLeftClickAir.run(context);
            }

            case RIGHT_CLICK_BLOCK -> {
                if (onRightClick != null) onRightClick.run(context);

                Function onRightClickBlock = itemTemplate.getOnRightClickBlock();
                if (onRightClickBlock != null) onRightClickBlock.run(context);
            }

            case RIGHT_CLICK_AIR -> {
                if (onRightClick != null) onRightClick.run(context);

                Function onRightClickAir = itemTemplate.getOnRightClickAir();
                if (onRightClickAir != null) onRightClickAir.run(context);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent event) {
        ItemStack offHandItem = event.getOffHandItem();
        if (offHandItem == null) {
            return;
        }

        ItemTemplate itemTemplate = plugin.getItemTemplate(offHandItem);
        if (itemTemplate == null) {
            return;
        }

        Function onSwapHand = itemTemplate.getOnSwapHand();
        if (onSwapHand != null) {
            Player player = event.getPlayer();
            Block block = player.getTargetBlockExact(5);
            Context context = Context.fromPlayerAndBlock(plugin, player, block, event);
            onSwapHand.run(context);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        ItemTemplate itemTemplate = plugin.getItemTemplate(item);
        if (itemTemplate == null) {
            return;
        }

        Function onDrop = itemTemplate.getOnDrop();
        if (onDrop != null) {
            Player player = event.getPlayer();
            Block block = player.getTargetBlockExact(5);
            Context context = Context.fromPlayerAndBlock(plugin, player, block, event);
            onDrop.run(context);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        PlayerState playerState = plugin.getPlayerState(event.getPlayer());
        playerState.refreshStats();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        PlayerState playerState = plugin.getPlayerState(event.getPlayer());
        playerState.clearStats();
    }

    @SuppressWarnings("removal")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvil(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null) {
            return;
        }

        ItemTemplate itemTemplate = plugin.getItemTemplate(result);
        if (itemTemplate == null) {
            return;
        }

        ItemStack input;
        String renameText;

        if (Server.getVersion() > Version.V1_21) {
            AnvilView anvilView = event.getView();
            input = anvilView.getItem(0);
            renameText = anvilView.getRenameText();
        } else {
            AnvilInventory anvilInventory = event.getInventory();
            input = anvilInventory.getItem(0);
            renameText = anvilInventory.getRenameText();
        }

        if (input != null && renameText != null) {
            String currentName = input.hasItemMeta() && input.getItemMeta().hasDisplayName()
                    ? input.getItemMeta().getDisplayName()
                    : null;

            if (!renameText.equals(currentName)) {
                ItemUtil.modifyMeta(result, meta -> PersistentData.setTag(meta, plugin.getItemTemplates().getRenamedKey()));
            }
        }

        Player player = (Player) event.getView().getPlayer();
        itemTemplate.updateItemMeta(result, player);
        event.setResult(result);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnchant(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        ItemTemplate itemTemplate = plugin.getItemTemplate(item);
        if (itemTemplate == null) {
            return;
        }

        Player player = event.getEnchanter();
        Bukkit.getScheduler().runTask(plugin, () -> itemTemplate.updateItemMeta(item, player));
    }

}
