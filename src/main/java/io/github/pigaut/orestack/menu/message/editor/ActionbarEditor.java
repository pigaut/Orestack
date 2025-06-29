package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ActionbarEditor extends GenericMessageEditor {

    public ActionbarEditor(EnhancedPlugin plugin, ConfigSection config, String name) {
        super(plugin, "Edit Action Bar", config, name);
        messageSection.set("type", "actionbar");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

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
                .addLore(messageSection.getOptionalString("message", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set the message");

        buttons[20] = messageButton.buildButton();

        return buttons;
    }

}
