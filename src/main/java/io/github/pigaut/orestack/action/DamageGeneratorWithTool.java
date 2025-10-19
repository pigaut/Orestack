package io.github.pigaut.orestack.action;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.core.function.action.*;
import io.github.pigaut.voxel.player.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class DamageGeneratorWithTool implements Action {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    @Override
    public void execute(@Nullable PlayerState playerState, @Nullable Event event, @Nullable Block block, @Nullable Entity entity) {
        if (playerState == null || block == null) {
            return;
        }

        Generator generator = plugin.getGenerator(block.getLocation());
        if (generator == null) {
            return;
        }

        generator.damage(playerState, Orestack.getGeneratorDamage(playerState.asPlayer(), block));
    }

}
