package io.github.pigaut.rpg.command.sound;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class PlaySoundSubCommand extends SubCommand {
    public PlaySoundSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "play");
        withPermission(plugin.getPermission("sound.play"));
        withDescription(plugin.getTranslation("sound-play-command"));
        withParameter(CommandParameters.soundName(plugin));
        withParameter(CommandParameters.WORLD_NAME);
        withParameter(CommandParameters.X_COORDINATE);
        withParameter(CommandParameters.Y_COORDINATE);
        withParameter(CommandParameters.Z_COORDINATE);
        withCommandExecution((sender, context, args) -> {
            SoundEffect sound = plugin.getSound(args[0]);
            if (sound == null) {
                plugin.sendMessage(sender, context, "sound-not-found");
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

            sound.play(null, new Location(world, x, y, z));
            plugin.sendMessage(sender, context, "played-sound");
        });
    }
}
