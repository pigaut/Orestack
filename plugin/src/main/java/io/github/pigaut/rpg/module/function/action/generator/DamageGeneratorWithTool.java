package io.github.pigaut.rpg.module.function.action.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageGeneratorWithTool implements Action.Executor {

    private final RpgMakerPlugin plugin = RpgMakerPlugin.getInstance();

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

        Settings settings = plugin.getSettings();
        generator.damage(player, context, settings.getStructureDamage(player, block));
    }

}
