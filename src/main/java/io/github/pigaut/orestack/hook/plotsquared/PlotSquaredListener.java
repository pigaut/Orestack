package io.github.pigaut.orestack.hook.plotsquared;

import com.plotsquared.core.events.*;
import com.plotsquared.core.plot.*;
import com.plotsquared.core.plot.flag.*;
import com.plotsquared.core.plot.flag.implementations.*;
import com.sk89q.worldedit.util.eventbus.*;

public class PlotSquaredListener {

    private static final BreakFlag BREAK_ALL_FLAG = null;

    @Subscribe
    public void onFlagAdd(PlotFlagAddEvent event) {
        PlotFlag<?, ?> flag = event.getFlag();
        if (!(flag instanceof OrestackFlag orestackFlag)) {
            return;
        }

        Plot plot = event.getPlot();
        boolean allowResources = orestackFlag.getValue();

    }

    @Subscribe
    public void onFlagRemove(PlotFlagAddEvent event) {

    }

}
