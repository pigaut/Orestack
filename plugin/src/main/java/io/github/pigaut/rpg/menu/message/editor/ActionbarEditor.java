package io.github.pigaut.rpg.menu.message.editor;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ActionbarEditor extends GenericMessageEditor {

    public ActionbarEditor(ConfigSection section) {
        super("Edit Action Bar", section);
        if (!section.isScalar("actionbar|action-bar")) {
            section.set("actionbar|action-bar", "not set");
        }
    }

    @Override
    public @Nullable Button[] createButtons(@NotNull Context context) {
        Button[] buttons = super.createButtons(context);

        ButtonBuilder messageButton = Button.builder()
                .type(Material.OAK_SIGN)
                .name("&f&lMessage")
                .enchanted(true)
                .onLeftClick((view, player) -> {
                    player.collectChatInput()
                            .description("Enter message in chat")
                            .onInput(input -> section.set("message", input))
                            .start();
                })
                .addEmptyLine()
                .addLine(section.getString("message", ColorUtil.FORMATTER).orElse("not set"))
                .addEmptyLine()
                .addLine("&eLeft-Click: &fTo set the message");

        buttons[20] = messageButton.buildButton();

        return buttons;
    }

}
