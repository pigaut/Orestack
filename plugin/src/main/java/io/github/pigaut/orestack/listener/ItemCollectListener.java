package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.event.drop.*;
import io.github.pigaut.voxel.event.drop.ItemSpawnEvent;
import io.github.pigaut.voxel.player.data.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class ItemCollectListener implements Listener {

    private final OrestackPlugin plugin;

    public ItemCollectListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent event) {
        if (!plugin.getSettings().isCollectionSourceEnabled(event.getSpawnReason())) {
            return;
        }

        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        RpgPlayerData playerData = plugin.getPlayerData(player);
        Context context = Context.builder(plugin)
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withTool(player.getInventory().getItemInMainHand())
                .build();

        for (ItemStack drop : event.getDrops()) {
            Collection collection = playerData.getItemCollection(drop);
            if (collection == null) {
                return;
            }
            collection.increaseAmount(context.with(Collection.class, collection), drop.getAmount());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockDrop(BlockDropItemEvent event) {
        if (!plugin.getSettings().isCollectionSourceEnabled(ItemSpawnReason.BLOCK_DROPS)) {
            return;
        }

        if (plugin.isPlayerPlacedBlock(event.getBlock())) {
            return;
        }

        Player player = event.getPlayer();
        RpgPlayerData playerData = plugin.getPlayerData(player);

        Context context = Context.builder(plugin)
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withTool(player.getInventory().getItemInMainHand())
                .build();

        for (Item item : event.getItems()) {
            ItemStack drop = item.getItemStack();
            Collection collection = playerData.getItemCollection(drop);
            if (collection == null) {
                return;
            }
            collection.increaseAmount(context.with(Collection.class, collection), drop.getAmount());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) {
            return;
        }

        if (!plugin.getSettings().isCollectionSourceEnabled(ItemSpawnReason.ENTITY_DROPS)) {
            return;
        }

        RpgPlayerData playerData = plugin.getPlayerData(killer);
        Context context = Context.builder(plugin)
                .withPlayer(killer)
                .withPlayerState(plugin.getPlayerState(killer))
                .withTool(killer.getInventory().getItemInMainHand())
                .build();

        for (ItemStack drop : event.getDrops()) {
            if (drop == null || drop.getType().isAir()) {
                continue;
            }

            Collection collection = playerData.getItemCollection(drop);
            if (collection == null) {
                continue;
            }

            collection.increaseAmount(context.with(Collection.class, collection), drop.getAmount());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFishingDrop(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        if (!plugin.getSettings().isCollectionSourceEnabled(ItemSpawnReason.FISHING_DROPS)) {
            return;
        }

        if (!(event.getCaught() instanceof Item caughtEntity)) {
            return;
        }

        ItemStack item = caughtEntity.getItemStack();

        Player player = event.getPlayer();
        RpgPlayerData playerData = plugin.getPlayerData(player);
        Collection collection = playerData.getItemCollection(item);
        if (collection == null) {
            return;
        }

        Context context = Context.builder(plugin)
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withTool(player.getInventory().getItemInMainHand())
                .build();

        collection.increaseAmount(context, item.getAmount());
    }

}
