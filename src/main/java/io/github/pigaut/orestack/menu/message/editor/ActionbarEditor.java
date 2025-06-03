package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.formatter.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class ActionbarEditor extends GenericMessageEditor {

    public ActionbarEditor(ConfigSection parent, String name) {
        super("Edit Action Bar", parent, name);
        parent.getSectionOrCreate(name).set("type", "actionbar");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final ConfigSection section = parent.getSectionOrCreate(name);

        final Button[] buttons = super.createButtons();
        final ButtonBuilder setMessageButton = Button.builder()
                .withType(Material.OAK_SIGN)
                .withDisplay("&f&lMessage")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter message in chat")
                            .onInput(input -> {
                                section.set("message", input);
                            })
                            .collect();
                });

        setMessageButton.addLore("");
        final String message = section.getOptionalString("message", StringColor.FORMATTER).orElse("");
        for (String splitMessage : StringUtil.splitByLength(message, 35)) {
            setMessageButton.addLore(ChatColor.WHITE + splitMessage);
        }
        setMessageButton.addLore("")
                .addLore("&7Left-Click: &fTo set the message");

        buttons[20] = setMessageButton.buildButton();

        return buttons;
    }

}
