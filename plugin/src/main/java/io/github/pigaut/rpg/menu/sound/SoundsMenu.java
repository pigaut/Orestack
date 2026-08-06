package io.github.pigaut.rpg.menu.sound;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SoundsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;

    public SoundsMenu(EnhancedPlugin plugin, String group) {
        super(CaseFormatter.toTitleCase(group) + " Sound Effects", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        return plugin.getSounds().getAll(group).stream()
                .map(sound -> Button.builder()
                        .type(sound.getIcon().getType())
                        .name("&3&o" + CaseFormatter.toTitleCase(sound.getName()))
                        .addEmptyLine()
                        .addLine("&eLeft-Click: &fPlay-me sound")
                        .onLeftClick((menuView, playerState) -> {
                            Player player = playerState.asPlayer();
                            sound.play(player, player.getLocation());
                        })
                        .buildButton())
                .toList();
    }

    @Override
    public Button getToolbarButton4() {
        return Buttons.MAIN_MENU;
    }

}
