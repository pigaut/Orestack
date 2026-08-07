package io.github.pigaut.rpg.command.particle;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class SpawnParticleSubCommand extends SubCommand {

    public SpawnParticleSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "spawn");
        withPermission(plugin.getPermission("particle.spawn"));
        withDescription(plugin.getTranslation("particle-spawn-command"));
        withParameter(CommandParameters.particleName(plugin));
        withParameter(CommandParameters.WORLD_NAME);
        withParameter(CommandParameters.X_COORDINATE);
        withParameter(CommandParameters.Y_COORDINATE);
        withParameter(CommandParameters.Z_COORDINATE);
        withCommandExecution((sender, context, args) -> {
            ParticleEffect particle = plugin.getParticle(args[0]);
            if (particle == null) {
                plugin.sendMessage(sender, context, "particle-not-found");
                return;
            }

            World world = Bukkit.getWorld(args[1]);
            if (world == null) {
                plugin.sendMessage(sender, context, "world-not-found");
                return;
            }

            Double x = ParseUtil.parseDoubleOrNull(args[2]);
            Double y = ParseUtil.parseDoubleOrNull(args[3]);
            Double z = ParseUtil.parseDoubleOrNull(args[4]);

            if (x == null || y == null || z == null) {
                plugin.sendMessage(sender, context, "expected-coordinates");
                return;
            }

            particle.spawn(new Location(world, x, y, z), null);
            plugin.sendMessage(sender, context, "spawned-particle");
        });
    }
}
