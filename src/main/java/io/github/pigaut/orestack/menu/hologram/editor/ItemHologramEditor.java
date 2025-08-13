package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.voxel.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ItemHologramEditor extends GenericHologramEditor {

    public ItemHologramEditor(ConfigSection hologramSection) {
        super(hologramSection);
        hologramSection.set("type", "item_display");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final ButtonBuilder blockButton = Button.builder()
                .withType(Material.GRASS_BLOCK)
                .withDisplay("&f&lItem Type")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter item type in chat")
                            .checkInput(input -> {
                                try {
                                    SpigotLibs.deserializeMaterial(input);
                                } catch (DeserializationException e) {
                                    return e.getMessage();
                                }
                                return null;
                            })
                            .collectInput(input -> {
                                hologramSection.set("item", input);
                                view.open();
                            })
                            .beginCollection();
                })
                .addLore("")
                .addLore(hologramSection.getOptionalString("item", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram item");

        buttons[20] = blockButton.buildButton();

        return buttons;
    }

}
