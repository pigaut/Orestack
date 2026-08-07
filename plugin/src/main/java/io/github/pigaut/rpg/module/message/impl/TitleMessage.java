package io.github.pigaut.rpg.module.message.impl;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.message.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TitleMessage extends GenericMessage {

    private final String title;
    private final String subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public TitleMessage(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this(UUID.randomUUID().toString(), null, title, subtitle, fadeIn, stay, fadeOut);
    }

    public TitleMessage(String name, @Nullable String group, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        super(name, group);
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public @NotNull MessageType getType() {
        return MessageType.TITLE;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return IconBuilder.of(Material.MAP).buildIcon();
    }

    @Override
    public void send(@NotNull Player player, @NotNull Context context) {
        String parsedTitle = PlaceholderUtil.parseAll(context, title);
        String parsedSubtitle = PlaceholderUtil.parseAll(context, subtitle);
        player.sendTitle(parsedTitle, parsedSubtitle, fadeIn, stay, fadeOut);
    }

}
