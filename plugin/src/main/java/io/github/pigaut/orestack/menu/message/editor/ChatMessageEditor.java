package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.bukkit.*;
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
        Button[] buttons = super.createButtons();

        String message = section.getString("message", StringColor.FORMATTER).orElse("not set");
        ButtonBuilder messageButton = Button.builder()
                .type(Material.OAK_SIGN)
                .name("&f&lMessage")
                .enchanted(true)
                .addEmptyLine()
                .addLines(StringUtil.splitByLength(message, 35))
                .addEmptyLine()
                .addLeftClickLine("To set the message")
                .onLeftClick((view, player) -> {
                    player.collectChatInput()
                            .description("Enter message in chat")
                            .onInput(input -> {
                                section.set("message", input);
                                view.open();
                            })
                            .start();
                });

        buttons[20] = messageButton.buildButton();

        return buttons;
    }
}
