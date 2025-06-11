package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GenericHologramEditor extends FramedEditorMenu {

    protected final ConfigSection hologramSection;

    public GenericHologramEditor(ConfigSection hologramSection) {
        super(hologramSection.getRoot(), "Edit Hologram", MenuSize.BIG);
        this.hologramSection = hologramSection;
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final Integer update = hologramSection.getOptionalInteger("update").orElse(null);
        final ButtonBuilder updateButton = Button.builder()
                .withType(Material.REDSTONE_LAMP)
                .withDisplay("&f&lUpdate")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter update amount in chat")
                            .onInput(input -> {
                                final Integer amount = Deserializers.getInteger(input);
                                if (amount != null) {
                                    hologramSection.set("update", amount);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(update != null ? (update + " ticks") : "none")
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram update");

        final ButtonBuilder xOffsetButton = Button.builder()
                .withType(Material.RED_WOOL)
                .withDisplay("&f&lOffset X")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter x-offset amount in chat")
                            .onInput(input -> {
                                final Integer xOffset = Deserializers.getInteger(input);
                                if (xOffset != null) {
                                    hologramSection.set("offset.x", xOffset);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(hologramSection.getOptionalString("offset.x").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram x offset");

        final ButtonBuilder yOffsetButton = Button.builder()
                .withType(Material.LIME_WOOL)
                .withDisplay("&f&lOffset Y")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter y-offset amount in chat")
                            .onInput(input -> {
                                final Integer yOffset = Deserializers.getInteger(input);
                                if (yOffset != null) {
                                    hologramSection.set("offset.y", yOffset);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(hologramSection.getOptionalString("offset.y").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram y offset");

        final ButtonBuilder zOffsetButton = Button.builder()
                .withType(Material.BLUE_WOOL)
                .withDisplay("&f&lOffset Z")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter z-offset amount in chat")
                            .onInput(input -> {
                                final Integer zOffset = Deserializers.getInteger(input);
                                if (zOffset != null) {
                                    hologramSection.set("offset.z", zOffset);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(hologramSection.getOptionalString("offset.z").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram z offset");

        buttons[21] = updateButton.buildButton();
        buttons[15] = xOffsetButton.buildButton();
        buttons[24] = yOffsetButton.buildButton();
        buttons[33] = zOffsetButton.buildButton();

        return buttons;
    }
}
