package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.formatter.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class ActionbarEditor extends GenericMessageEditor {

    public ActionbarEditor(EnhancedPlugin plugin, ConfigSection parent, String name) {
        super(plugin, "Edit Action Bar", parent, name);
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
                                player.setOpenView(view);
                            })
                            .collect();
                });

        setMessageButton.addLore("")
                .addLore(ChatColor.WHITE + section.getOptionalString("message", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fSet the message");

        buttons[20] = setMessageButton.buildButton();

        return buttons;
    }

}
