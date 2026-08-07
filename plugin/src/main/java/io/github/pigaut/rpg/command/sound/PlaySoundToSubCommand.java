package io.github.pigaut.rpg.command.sound;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlaySoundToSubCommand extends SubCommand {

    public PlaySoundToSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "play-to");
        withPermission(plugin.getPermission("sound.play-to"));
        withDescription(plugin.getTranslation("sound-play-to-command"));
        withParameter(CommandParameters.ONLINE_PLAYER);
        withParameter(CommandParameters.soundName(plugin));
        withCommandExecution((sender, context, args) -> {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                plugin.sendMessage(sender, context, "player-not-online");
                return;
            }
            SoundEffect sound = plugin.getSound(args[1]);
            if (sound == null) {
                plugin.sendMessage(sender, context, "sound-not-found");
                return;
            }
            sound.play(player, player.getLocation());
            plugin.sendMessage(sender, context, "played-sound-at-player");
        });
    }

}
