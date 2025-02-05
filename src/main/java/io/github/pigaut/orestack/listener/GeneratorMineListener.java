package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.event.*;
import io.github.pigaut.voxel.function.*;
import org.bukkit.event.*;

public class GeneratorMineListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onMine(GeneratorMineEvent event) {
        final Function breakFunction = event.getGenerator().getCurrentStage().getBreakFunction();
        if (breakFunction != null) {
            breakFunction.run(event.getPlayer(), event, event.getBlockMined());
        }
    }

}
