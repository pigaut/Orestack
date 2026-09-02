package io.github.pigaut.rpg.hook.veinminer;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.generator.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class GeneratorBlockVein {

    private static final RpgMakerPlugin plugin = RpgMakerPlugin.getInstance();
    private static final VeinMiningPattern veinMiningPattern = VeinMiningPattern.getInstance();

    private GeneratorBlockVein() {}

    public static void mineBlocks(@NotNull Generator generator, @NotNull Player player, int maxVeinSize, int expToDrop) {
        if (generator.getTemplate().isMultiBlock()) {
            return;
        }

        if (!plugin.getSettings().isVeinGenerator(generator)) {
            return;
        }

        for (Generator veinGenerator : veinMiningPattern.allocateGenerators(player, generator, maxVeinSize)) {
            Location origin = veinGenerator.getOrigin();
            veinGenerator.mineBlock(player, origin.getBlock(), expToDrop);
        }
    }

}
