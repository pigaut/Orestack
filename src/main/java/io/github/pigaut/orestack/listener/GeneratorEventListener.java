package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.event.*;
import io.github.pigaut.voxel.core.function.*;
import org.bukkit.event.*;

public class GeneratorEventListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onMine(GeneratorMineEvent event) {
        Function breakFunction = event.getGenerator().getCurrentStage().getBreakFunction();
        if (breakFunction != null) {
            breakFunction.run(event.getPlayer(), event, event.getBlockMined());
        }
    }

}
