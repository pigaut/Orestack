package io.github.pigaut.rpg.menu.message;

import io.github.pigaut.rpg.menu.message.editor.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;

import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.menu.message.editor.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.node.section.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class MessagesMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;
    private final RootSection config;

    public MessagesMenu(EnhancedPlugin plugin, String group) {
        super(CaseFormatter.toTitleCase(group) + " Messages", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
        File file = Group.getFile(plugin, "messages", group);
        this.config = YamlConfig.loadSectionOrEmpty(file, plugin.getConfigurator());
    }

    @Override
    public void onOpen(MenuView view) {
        plugin.getMessages().reload(errorsFound -> {
            view.update();
        });
    }

    @Override
    public Button getFrameButton() {
        return Buttons.LIGHT_BLUE_PANEL;
    }

    @Override
    public @Nullable Button[] createButtons(@NotNull Context context) {
        Button[] buttons = super.createButtons(context);

        buttons[41] = new ConfigLoadButton(config);

        return buttons;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        final List<Button> entries = new ArrayList<>();

        for (Message message : plugin.getMessages().getAll(group)) {
            String name = message.getName();
            Button messageButton = Button.builder()
                    .type(message.getIcon().getType())
                    .name("&b&o" + CaseFormatter.toTitleCase(name))
                    .addEmptyLine()
                    .addLine("&eLeft-Click: &fTo edit message")
                    .addLine("&6Right-Click: &fTo receive message")
                    .onLeftClick((view, player) -> {
                        PlayerState viewer = view.getViewer();
                        switch (message.getType()) {
                            case CHAT -> viewer.openMenu(new ChatMessageEditor(config.getSectionOrCreate(name)));
                            case ACTIONBAR -> viewer.openMenu(new ActionbarEditor(config.getSectionOrCreate(name)));
                            case TITLE -> viewer.openMenu(new TitleEditor(config.getSectionOrCreate(name)));
                            case BOSSBAR -> viewer.openMenu(new BossbarEditor(config.getSectionOrCreate(name)));
                            case HOLOGRAM -> viewer.openMenu(new HologramMessageEditor(config.getSectionOrCreate(name)));
                            case MULTI -> viewer.openMenu(new MultiMessageEditor(config.getSequenceOrCreate(name)));
                        }
                    })
                    .onRightClick((view, playerState) -> {
                        view.close();

                        Player player = playerState.asPlayer();
                        message.send(player, context);

                        int guiReopenDelay = plugin.getSettings().guiReopenDelay;
                        playerState.sendMessage(ChatColor.RED + "The menu will reopen in " + (guiReopenDelay / 20) + " seconds...");
                        plugin.getScheduler().runTaskLater(guiReopenDelay, view::open);
                    })
                    .buildButton();
            entries.add(messageButton);
        }

        entries.add(Button.builder()
                .type(Material.LIME_DYE)
                .name("&2Create New Message")
                .enchanted(true)
                .onLeftClick((view, player) -> {
                    player.collectChatInput()
                            .description("Enter message name in chat")
                            .onInput(input -> {
                                player.openMenu(new MessageCreationMenu(config.getSectionOrCreate(input), true), view);
                            })
                            .start();
                })
                .buildButton());

        return entries;
    }

    @Override
    public Button getToolbarButton4() {
        return Buttons.MAIN_MENU;
    }

}
