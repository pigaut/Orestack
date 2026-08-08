package io.github.pigaut.rpg.module.function.action.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.settings.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageGateWithTool implements Action {

    private final RpgMakerPlugin plugin;

    public DamageGateWithTool(RpgMakerPlugin plugin) {
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

        RpgSettings settings = plugin.getSettings();
        gate.damage(player, context, settings.getStructureDamage(player, block));
    }

}
