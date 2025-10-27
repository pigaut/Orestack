package io.github.pigaut.orestack.hook.plotsquared;

import com.google.common.eventbus.*;
import com.plotsquared.bukkit.listener.*;
import com.plotsquared.core.*;
import com.plotsquared.core.events.*;
import com.plotsquared.core.plot.*;
import com.plotsquared.core.plot.flag.*;
import com.plotsquared.core.plot.flag.implementations.*;
import com.plotsquared.core.plot.flag.types.*;
import com.plotsquared.core.util.*;
import com.sk89q.worldedit.world.block.BlockState;
import io.github.pigaut.orestack.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

import java.util.*;

public class PlotSquaredListener implements Listener {

    private final BreakFlag breakAllFlag;
    private final OrestackPlugin plugin;

    public PlotSquaredListener(OrestackPlugin plugin) {
        this.plugin = plugin;

        StringJoiner joiner = new StringJoiner(",");
        for (Material material : Material.values()) {
            joiner.add(material.toString());
        }

        BreakFlag breakAllFlag = null;
        try {
            breakAllFlag = GlobalFlagContainer.getInstance().getFlag(BreakFlag.class).parse(joiner.toString());
        }
        catch (FlagParseException e) {
            e.printStackTrace();
            plugin.getColoredLogger().warning("Could not create break flag for PlotSquared.");
        }
        this.breakAllFlag = breakAllFlag;
    }

    private static class BreakAllFlag extends BreakFlag {
        public BreakAllFlag() {
            super(getAllBlocks());
        }

        public static List<BlockTypeWrapper> getAllBlocks() {
            final List<BlockTypeWrapper> parsedBlocks = new ArrayList<>();
            for (Material material : Material.values()) {
                final BlockState blockState = BlockUtil.get(material.toString());
                if (blockState != null) {
                    final BlockTypeWrapper blockTypeWrapper = BlockTypeWrapper.get(blockState.getBlockType());
                    if (blockTypeWrapper != null) {
                        parsedBlocks.add(blockTypeWrapper);
                    }
                }
            }
            return parsedBlocks;
        }
    }

    @Subscribe
    public void onFlagAdd(PlotFlagAddEvent event) {
        Plot plot = event.getPlot();
        PlotFlag<?, ?> flag = event.getFlag();

        if (flag instanceof BreakFlag && plot.getFlag(OrestackFlag.class)) {
            event.setEventResult(Result.DENY);
            return;
        }

        if (flag instanceof OrestackFlag orestackFlag && orestackFlag.getValue()) {
            plot.setFlag(breakAllFlag);
        }
    }

    @Subscribe
    public void onFlagRemove(PlotFlagRemoveEvent event) {
        Plot plot = event.getPlot();
        PlotFlag<?, ?> flag = event.getFlag();
        if (flag instanceof BreakFlag && plot.getFlag(OrestackFlag.class)) {
            event.setEventResult(Result.DENY);
            return;
        }

        if (flag instanceof OrestackFlag) {
            plot.setFlag(BreakFlag.BREAK_NONE);
        }
    }

    private final BlockEventListener plotSquaredListener;
    {
        PlotSquared plotSquared = PlotSquared.get();
        plotSquaredListener = new BlockEventListener(plotSquared.getPlotAreaManager(), plotSquared.getWorldEdit());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        if (plugin.getGenerators().isGenerator(event.getBlock().getLocation())) {
            plotSquaredListener.blockDestroy(event);
        }
    }

}
