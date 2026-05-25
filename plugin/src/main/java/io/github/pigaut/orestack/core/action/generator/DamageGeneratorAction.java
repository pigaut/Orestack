package io.github.pigaut.orestack.core.action.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.action.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageGeneratorAction implements Action {

    private final OrestackPlugin plugin = OrestackPlugin.getInstance();

    private final Amount damageAmount;

    public DamageGeneratorAction(Amount damageAmount) {
        this.damageAmount = damageAmount;
    }

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

        generator.damage(player, context, damageAmount.doubleValue());
    }

}
