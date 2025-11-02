package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class BlockBreakListener implements Listener {

    private final OrestackPlugin plugin;

    public BlockBreakListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Generator generator = plugin.getGenerator(block.getLocation());
        if (generator == null) {
            return;
        }

        event.setCancelled(true);
        GeneratorLogic.breakBlock(generator, event.getPlayer(), block, event.getExpToDrop());
    }

}
