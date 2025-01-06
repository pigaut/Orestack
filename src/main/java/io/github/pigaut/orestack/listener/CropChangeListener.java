package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class CropChangeListener implements Listener {

    private final OrestackPlugin plugin;

    public CropChangeListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent event) {
        final BlockGenerator generator = plugin.getBlockGenerator(event.getToBlock().getLocation());
        if (generator != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCropPhysics(BlockPhysicsEvent event) {
        if (Crops.isCrop(event.getChangedType())) {
            final BlockGenerator generator = plugin.getBlockGenerator(event.getSourceBlock().getLocation());
            if (generator != null) {
                event.setCancelled(true);
            }
        }

        final Block attachedCocoa = Crops.getAttachedCocoa(event.getBlock());
        if (attachedCocoa != null) {
            final BlockGenerator generator = plugin.getBlockGenerator(attachedCocoa.getLocation());
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGrowth(BlockGrowEvent event) {
        final BlockGenerator generator = plugin.getBlockGenerator(event.getBlock().getLocation());
        if (generator != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTrample(PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (event.getAction() == Action.PHYSICAL && block.getType() == Material.FARMLAND) {
            final Location cropLocation = block.getLocation().add(0, 1, 0);
            final BlockGenerator generator = plugin.getBlockGenerator(cropLocation);
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTrample(EntityInteractEvent event) {
        final Block block = event.getBlock();
        if (block.getType() == Material.FARMLAND) {
            final Location cropLocation = block.getLocation().add(0, 1, 0);
            final BlockGenerator generator = plugin.getBlockGenerator(cropLocation);
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

}
