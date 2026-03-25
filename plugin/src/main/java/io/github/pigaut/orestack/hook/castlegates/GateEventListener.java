package io.github.pigaut.orestack.hook.castlegates;

import io.github.pigaut.castlegates.api.event.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import org.bukkit.block.*;
import org.bukkit.event.*;

public class GateEventListener implements Listener {

    private final OrestackPlugin plugin;

    public GateEventListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void test(GeneratorInteractEvent event) {
        System.out.println("click");
    }

    @EventHandler
    public void test(GeneratorHitEvent event) {
        System.out.println("left click");
    }

    @EventHandler
    public void test(GeneratorHarvestEvent event) {
        System.out.println("right click");
    }


    @EventHandler
    public void onGatePlace(GatePlaceEvent event) {
        for (Block block : event.getOccupiedBlocks()) {
            if (plugin.getGenerators().isGenerator(block.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }
    }

}
