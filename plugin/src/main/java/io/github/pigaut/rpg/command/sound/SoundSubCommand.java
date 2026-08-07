package io.github.pigaut.rpg.command.sound;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class SoundSubCommand extends SubCommand {

    public SoundSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "sound");
        withPermission(plugin.getPermission("sound"));
        withDescription(plugin.getTranslation("sound-play-command"));
        addSubCommand(new PlaySoundSubCommand(plugin));
        addSubCommand(new PlaySoundToSubCommand(plugin));
    }

}
