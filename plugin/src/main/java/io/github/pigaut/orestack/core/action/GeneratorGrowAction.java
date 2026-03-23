package io.github.pigaut.orestack.core.action;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.data.function.action.block.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorGrowAction implements BlockAction {

    private final OrestackPlugin plugin = OrestackPlugin.getInstance();

    @Override
    public void execute(@NotNull Block block) {
        Generator generator = plugin.getGenerator(block.getLocation());
        if (generator != null) {
            generator.grow();
        }
    }

}
