package io.github.pigaut.orestack.core.action.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.action.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageGateWithTool implements Action {

    private final OrestackPlugin plugin;

    public DamageGateWithTool(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        Block block = context.block();
        if (player == null || block == null) {
            return;
        }

        Gate gate = context.get(Gate.class);
        if (gate == null || !gate.exists()) {
            return;
        }

        OrestackSettings settings = plugin.getSettings();
        gate.damage(player, context, settings.getStructureDamage(player, block));
    }

}
