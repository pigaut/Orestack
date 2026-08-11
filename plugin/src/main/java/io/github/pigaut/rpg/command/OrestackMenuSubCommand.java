package io.github.pigaut.rpg.command;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.menu.*;
import io.github.pigaut.rpg.menu.*;
import org.jetbrains.annotations.*;

public class OrestackMenuSubCommand extends MenuSubCommand {

    public OrestackMenuSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin);
        withPlayerStateExecution((player, args, placeholders) -> {
            player.openMenu(new RpgMakerMenu(plugin));
        });
    }

}
