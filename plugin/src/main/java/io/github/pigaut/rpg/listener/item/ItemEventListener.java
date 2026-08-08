package io.github.pigaut.rpg.listener.item;

import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.event.item.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class ItemEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public ItemEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        ItemStack tool = player.getInventory().getItemInMainHand();
        ItemTemplate itemTemplate = plugin.getItemTemplate(tool);
        if (itemTemplate == null) {
            return;
        }

        Context context = Context.fromPlayerAndBlock(plugin, player, block, event);

        BlockBreakingPower blockBreakingPower = plugin.getSettings().getBlockBreakingPower(block);
        if (blockBreakingPower != null) {
            int blockPower = blockBreakingPower.getAmount();
            context.addPlaceholder("block_" + blockBreakingPower.getName(), blockPower);

            ToolBreakingPower toolBreakingPower = itemTemplate.getBreakingPower();

            int toolPower = plugin.getSettings().getDefaultBreakingPower();
            context.addPlaceholder("tool_" + blockBreakingPower.getName(), toolPower);
            if (toolBreakingPower != null) {
                toolPower = toolBreakingPower.getAmount();
                context.addPlaceholder("tool_" + blockBreakingPower.getName(), toolPower);

                if (!toolBreakingPower.getType().equals(blockBreakingPower.getType())) {
                    event.setCancelled(true);
                    Function onWrongTool = blockBreakingPower.getOnWrongTool();
                    if (onWrongTool != null) {
                        onWrongTool.run(context);
                    }
                    return;
                }
            }

            if (blockPower > toolPower) {
                event.setCancelled(true);
                context.addPlaceholder("block_" + blockBreakingPower.getName(), blockPower);
                context.addPlaceholder("tool_" + blockBreakingPower.getName(), toolPower);
                Function onInsufficientPower = blockBreakingPower.getOnInsufficientPower();
                if (onInsufficientPower != null) {
                    onInsufficientPower.run(context);
                }
                return;
            }
        }

        Function onBlockBreak = itemTemplate.getOnBlockBreak();
        if (onBlockBreak != null) {
            onBlockBreak.run(context);
        }

        if (event.isCancelled()) {
            return;
        }

        event.setDropItems(false);
        for (BlockItemDrop blockDrop : ItemDrop.fromBlock(plugin, block)) {
            blockDrop.spawn(context, ItemSpawnReason.BLOCK_DROPS);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGeneratorMine(GeneratorMineEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockMined();

        ItemStack tool = player.getInventory().getItemInMainHand();
        ItemTemplate itemTemplate = plugin.getItemTemplate(tool);
        if (itemTemplate == null) {
            return;
        }

        Context context = Context.fromPlayerAndBlock(plugin, player, block);

        BlockBreakingPower blockBreakingPower = plugin.getSettings().getBlockBreakingPower(block);
        if (blockBreakingPower != null) {
            int blockPower = blockBreakingPower.getAmount();
            context.addPlaceholder("block_" + blockBreakingPower.getName(), blockPower);

            ToolBreakingPower toolBreakingPower = itemTemplate.getBreakingPower();

            int toolPower = plugin.getSettings().getDefaultBreakingPower();
            context.addPlaceholder("tool_" + blockBreakingPower.getName(), toolPower);
            if (toolBreakingPower != null) {
                toolPower = toolBreakingPower.getAmount();
                context.addPlaceholder("tool_" + blockBreakingPower.getName(), toolPower);

                if (!toolBreakingPower.getType().equals(blockBreakingPower.getType())) {
                    event.setCancelled(true);
                    Function onWrongTool = blockBreakingPower.getOnWrongTool();
                    if (onWrongTool != null) {
                        onWrongTool.run(context);
                    }
                    return;
                }
            }

            if (blockPower > toolPower) {
                event.setCancelled(true);
                context.addPlaceholder("block_" + blockBreakingPower.getName(), blockPower);
                context.addPlaceholder("tool_" + blockBreakingPower.getName(), toolPower);
                Function onInsufficientPower = blockBreakingPower.getOnInsufficientPower();
                if (onInsufficientPower != null) {
                    onInsufficientPower.run(context);
                }
                return;
            }
        }
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEquip(PlayerEquipmentChangeEvent event) {
        PlayerState playerState = plugin.getPlayerState(event.getPlayer());
        playerState.refreshStats(event.getSlot());
    }

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
