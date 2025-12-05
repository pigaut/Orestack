package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GenericHologramEditor extends FramedEditor {

    public GenericHologramEditor(ConfigSection hologramSection) {
        super(hologramSection, "Edit Hologram", MenuSize.BIG);
    }

    @Override
    public Button getFrameButton() {
        return Buttons.WHITE_PANEL;
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final Integer update = section.getInteger("update").orElse(null);
        final ButtonBuilder updateButton = Button.builder()
                .withType(Material.REDSTONE_LAMP)
                .withDisplay("&f&lUpdate")
                .enchanted(true)
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Integer.class)
                            .description("Enter update amount in chat")
                            .onInput(input -> {
                                section.set("update", input);
                                view.open();
                            })
                            .start();
                })
                .addLore("")
                .addLore(update != null ? (update + " ticks") : "none")
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram update");

        final ButtonBuilder xOffsetButton = Button.builder()
                .withType(Material.RED_WOOL)
                .withDisplay("&f&lOffset X")
                .enchanted(true)
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Double.class)
                            .description("Enter x-offset amount in chat")
                            .onInput(input -> {
                                section.set("offset.x", input);
                                view.open();
                            })
                            .start();
                })
                .addLore("")
                .addLore(section.getString("offset.x").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram x offset");

        final ButtonBuilder yOffsetButton = Button.builder()
                .withType(Material.LIME_WOOL)
                .withDisplay("&f&lOffset Y")
                .enchanted(true)
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Double.class)
                            .description("Enter y-offset amount in chat")
                            .onInput(input -> {
                                section.set("offset.y", input);
                                view.open();
                            })
                            .start();
                })
                .addLore("")
                .addLore(section.getString("offset.y").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram y offset");

        final ButtonBuilder zOffsetButton = Button.builder()
                .withType(Material.BLUE_WOOL)
                .withDisplay("&f&lOffset Z")
                .enchanted(true)
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Double.class)
                            .description("Enter z-offset amount in chat")
                            .onInput(input -> {
                                section.set("offset.z", input);
                                view.open();
                            })
                            .start();
                })
                .addLore("")
                .addLore(section.getString("offset.z").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram z offset");

        buttons[21] = updateButton.buildButton();
        buttons[15] = xOffsetButton.buildButton();
        buttons[24] = yOffsetButton.buildButton();
        buttons[33] = zOffsetButton.buildButton();

        return buttons;
    }
}
