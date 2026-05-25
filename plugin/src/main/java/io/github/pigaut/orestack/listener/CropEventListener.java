package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.data.function.action.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.*;

public class CropEventListener implements Listener {

    private final OrestackPlugin plugin;

    public CropEventListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWaterFlow(BlockFromToEvent event) {
        Location targetLocation = event.getToBlock().getLocation();

        GlobalGenerator generator = plugin.getGlobalGenerator(targetLocation);
        if (generator != null) {
            Location location = generator.getOrigin();
            generator.remove();
            plugin.getLogger().warning("Removed generator at " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ". " +
                    "Reason: water/lava destroyed the block.");
        }

        Gate gate = plugin.getGate(targetLocation);
        if (gate != null) {
            Location location = gate.getOrigin();
            gate.remove();
            plugin.getLogger().warning("Removed gate at " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ". " +
                    "Reason: water/lava destroyed the block.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCropPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (MaterialUtil.isCrop(block.getType())) {
            Location loc = block.getLocation();
            if (plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onStructureGrowth(StructureGrowEvent event) {
        Location loc = event.getLocation();
        if (plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpread(BlockSpreadEvent event) {
        Location loc = event.getSource().getLocation();
        if (plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onForm(BlockFormEvent event) {
        Location loc = event.getBlock().getLocation();
        if (plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGrowth(BlockGrowEvent event) {
        Location loc = event.getBlock().getLocation();
        if (plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTrample(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && event.getAction() == Action.PHYSICAL && block.getType() == Material.FARMLAND) {
            Location cropLocation = block.getLocation().add(0, 1, 0);

            if (plugin.getGlobalGenerator(cropLocation) != null
                    || plugin.getGenerators().isGenerator(cropLocation)
                    || plugin.getGates().isGate(cropLocation)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTrample(EntityInteractEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.FARMLAND) {
            Location cropLocation = block.getLocation().add(0, 1, 0);

            if (plugin.getGlobalGenerator(cropLocation) != null
                    || plugin.getGenerators().isGenerator(cropLocation)
                    || plugin.getGates().isGate(cropLocation)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMoistureChange(MoistureChangeEvent event) {
        Location cropLocation = event.getBlock().getLocation().add(0, 1, 0);
        if (plugin.getGenerators().isGenerator(cropLocation) || plugin.getGates().isGate(cropLocation)) {
            event.setCancelled(true);
        }
    }

}
