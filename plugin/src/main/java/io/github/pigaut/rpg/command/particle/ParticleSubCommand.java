package io.github.pigaut.rpg.command.particle;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class ParticleSubCommand extends SubCommand {

    public ParticleSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "particle");
        withPermission(plugin.getPermission("particle"));
        withDescription(plugin.getTranslation("particle-command"));
        addSubCommand(new SpawnParticleSubCommand(plugin));
        addSubCommand(new SpawnParticleToSubCommand(plugin));
        addSubCommand(new ShowMeParticleSubCommand(plugin));
        addSubCommand(new ShowMeGroupParticleSubCommand(plugin));
    }

}
