package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ChatMessageEditor extends GenericMessageEditor {

    public ChatMessageEditor(ConfigSection messageSection) {
        super("Edit Chat Message", messageSection);
        messageSection.set("type", "chat");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final String message = section.getString("message", StringColor.FORMATTER).orElse("none");
        final ButtonBuilder messageButton = Button.builder()
                .withType(Material.OAK_SIGN)
                .withDisplay("&f&lMessage")
                .enchanted(true)
                .addLore("")
                .addLore(StringUtil.splitByLength(message, 35))
                .addLore("")
                .addLeftClickLore("To set the message")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter message in chat")
                            .withInputCollector(input -> {
                                section.set("message", input);
                                view.open();
                            })
                            .collect();
                });

        buttons[20] = messageButton.buildButton();

        return buttons;
    }
}
