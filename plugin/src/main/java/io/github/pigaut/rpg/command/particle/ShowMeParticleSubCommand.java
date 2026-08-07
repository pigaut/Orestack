package io.github.pigaut.rpg.command.particle;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ShowMeParticleSubCommand extends SubCommand {

    public ShowMeParticleSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "show-me");
        withPermission(plugin.getPermission("particle.show-me"));
        withDescription(plugin.getTranslation("particle-show-me-command"));
        withParameters(CommandParameters.particleName(plugin));
        withPlayerExecution((player, context, args) -> {
            ParticleEffect particle = plugin.getParticle(args[0]);
            if (particle == null) {
                plugin.sendMessage(player, context, "particle-not-found");
                return;
            }
            Location playerLocation = player.getLocation();
            particle.spawn(playerLocation.add(player.getFacing().getDirection().multiply(2)).add(0, 1, 0), player);
            plugin.sendMessage(player, context, "showed-me-particle");
        });
    }

}
