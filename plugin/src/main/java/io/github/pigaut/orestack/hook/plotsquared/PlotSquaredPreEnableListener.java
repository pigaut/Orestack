package io.github.pigaut.orestack.hook.plotsquared;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.event.*;
import org.bukkit.event.server.*;

public class PlotSquaredPreEnableListener implements Listener {

    private final OrestackPlugin plugin;
    private boolean worldEditEnabled = false;

    public PlotSquaredPreEnableListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldEditEnable(PluginEnableEvent event) {
        if (worldEditEnabled) {
            return;
        }

        if (event.getPlugin().getName().equals("WorldEdit")) {
            plugin.registerListener(new PlotBlockBreakListener(plugin));
            worldEditEnabled = true;
        }
    }

}
