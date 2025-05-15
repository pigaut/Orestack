package io.github.pigaut.orestack.action;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.core.function.action.block.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorPreviousStageAction implements BlockAction {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    @Override
    public void execute(@NotNull Block block) {
        final Generator generator = plugin.getGenerator(block.getLocation());
        if (generator != null && !generator.isUpdating() && !generator.isFirstStage()) {
            generator.previousStage();
        }
    }

}
