package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.voxel.util.StringColor;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ChatMessageEditor extends GenericMessageEditor {

    public ChatMessageEditor(EnhancedPlugin plugin, ConfigSection config, String name) {
        super(plugin, "Edit Chat Message", config, name);
        messageSection.set("type", "chat");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final String message = messageSection.getOptionalString("message", StringColor.FORMATTER).orElse("none");
        final ButtonBuilder messageButton = Button.builder()
                .withType(Material.OAK_SIGN)
                .withDisplay("&f&lMessage")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter message in chat")
                            .onInput(input -> {
                                messageSection.set("message", input);
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(StringUtil.splitByLength(message, 35))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set the message");

        buttons[20] = messageButton.buildButton();

        return buttons;
    }
}
