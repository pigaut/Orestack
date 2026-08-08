package io.github.pigaut.rpg.module.message.type;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ActionBarMessage extends GenericMessage {

    private final EnhancedPlugin plugin;
    private final String message;
    private final @Nullable BarAlignment statusBarAlignment;

    public ActionBarMessage(@NotNull EnhancedPlugin plugin, @NotNull String message, @Nullable BarAlignment statusBarAlignment) {
        super(UUID.randomUUID().toString(), null);
        this.plugin = plugin;
        this.message = message;
        this.statusBarAlignment = statusBarAlignment;
    }

    public ActionBarMessage(@NotNull EnhancedPlugin plugin, @NotNull String name,
                            @Nullable String group, @NotNull String message, @Nullable BarAlignment statusBarAlignment) {
        super(name, group);
        this.plugin = plugin;
        this.message = message;
        this.statusBarAlignment = statusBarAlignment;
    }

    @Override
    public @NotNull MessageType getType() {
        return MessageType.ACTIONBAR;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return IconBuilder.of(Material.NAME_TAG).buildIcon();
    }

    @Override
    public void send(@NotNull Player player, @NotNull Context context) {
        String parsedMessage = PlaceholderUtil.parseAll(context, message);
        PlayerState playerState = plugin.getPlayerState(player);

        if (statusBarAlignment != null) {
            playerState.sendActionBar(parsedMessage, statusBarAlignment);
        } else {
            playerState.sendActionBar(parsedMessage);
        }
    }

}
