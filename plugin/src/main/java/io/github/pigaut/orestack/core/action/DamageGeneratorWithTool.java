package io.github.pigaut.orestack.core.action;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.action.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageGeneratorWithTool implements Action {

    private final OrestackPlugin plugin = OrestackPlugin.getInstance();

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        Block block = context.block();
        if (player == null || block == null) {
            return;
        }

        Generator generator = plugin.getGenerator(player, block.getLocation());
        if (generator == null) {
            return;
        }

        OrestackSettings settings = plugin.getSettings();
        generator.damage(player, context, settings.getGeneratorDamage(player, block));
    }

}
