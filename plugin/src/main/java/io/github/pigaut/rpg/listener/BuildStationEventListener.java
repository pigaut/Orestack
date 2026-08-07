package io.github.pigaut.rpg.listener;

import io.github.pigaut.rpg.core.buildstation.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.buildstation.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class BuildStationEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public BuildStationEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getBuildStations().isBuildStation(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Block block = event.getClickedBlock();
        Location location = block.getLocation();
        BuildStation builder = plugin.getBuildStation(location);
        if (builder == null) {
            return;
        }

        if (location.equals(builder.getPreviousButtonLocation())) {
            event.setCancelled(true);
            builder.previousStructure();
        }
        else if (location.equals(builder.getSaveButtonLocation())) {
            event.setCancelled(true);
            builder.saveStructureToFile(event.getPlayer());
        }
        else if (location.equals(builder.getNextButtonLocation())) {
            event.setCancelled(true);
            builder.nextStructure();
        }
        else if (location.equals(builder.getClearButtonLocation())) {
            event.setCancelled(true);
            builder.clearBuildArea();
        }

    }

}
