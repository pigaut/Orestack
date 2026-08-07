package io.github.pigaut.rpg.command.particle;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ShowMeGroupParticleSubCommand extends SubCommand {

    public ShowMeGroupParticleSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "show-me-group");
        withPermission(plugin.getPermission("particle.show-me-group"));
        withDescription(plugin.getTranslation("particle-show-me-group-command"));
        withParameter(CommandParameters.particleGroup(plugin));
        withParameter(CommandParameter.create("group-particle",
                (sender, args) -> plugin.getParticles().getAllNames(args[0])));
        withPlayerExecution((player, context, args) -> {
            String particleGroup = args[0];
            if (!plugin.getParticles().containsGroup(particleGroup)) {
                plugin.sendMessage(player, context, "particle-group-not-found");
                return;
            }

            ParticleEffect particle = plugin.getParticle(args[1]);
            if (particle == null || !particleGroup.equals(particle.getGroup())) {
                plugin.sendMessage(player, context, "group-particle-not-found");
                return;
            }

            Location playerLocation = player.getLocation();
            particle.spawn(playerLocation.add(player.getFacing().getDirection().multiply(2)).add(0, 1, 0), player);
            plugin.sendMessage(player, context, "showed-me-particle");
        });
    }

}
