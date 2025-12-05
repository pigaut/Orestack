package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class StaticHologramEditor extends GenericHologramEditor {

    public StaticHologramEditor(ConfigSection hologramSection) {
        super(hologramSection);
        hologramSection.set("type", "static");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final ButtonBuilder textButton = Button.builder()
                .withType(Material.OAK_SIGN)
                .withDisplay("&f&lText")
                .enchanted(true)
                .onLeftClick((view, player) -> {
                    player.collectChatInput()
                            .description("Enter text in chat")
                            .onInput(input -> {
                                section.set("text", input);
                                view.open();
                            })
                            .start();
                })
                .addLore("")
                .addLore(section.getString("text", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram text");

        buttons[20] = textButton.buildButton();

        return buttons;
    }

}
