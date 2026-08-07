package io.github.pigaut.rpg.command.particle;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SpawnParticleToSubCommand extends SubCommand {

    public SpawnParticleToSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "spawn-to");
        withPermission(plugin.getPermission("particle.spawn-to"));
        withDescription(plugin.getTranslation("particle-spawn-to-command"));
        withParameter(CommandParameters.ONLINE_PLAYER);
        withParameter(CommandParameters.particleName(plugin));
        withCommandExecution((sender, context, args) -> {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                plugin.sendMessage(sender, context, "player-not-online");
                return;
            }

            ParticleEffect particle = plugin.getParticle(args[1]);
            if (particle == null) {
                plugin.sendMessage(sender, context, "particle-not-found");
                return;
            }
            particle.emit(player);
            plugin.sendMessage(sender, context, "spawned-particle-at-player");
        });
    }

}
