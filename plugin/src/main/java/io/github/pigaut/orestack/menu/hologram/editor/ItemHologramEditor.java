package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ItemHologramEditor extends GenericHologramEditor {

    public ItemHologramEditor(ConfigSection hologramSection) {
        super(hologramSection);
        section.set("type", "item_display");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final ButtonBuilder blockButton = Button.builder()
                .withType(Material.GRASS_BLOCK)
                .withDisplay("&f&lItem Type")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Material.class)
                            .withDescription("Enter item type in chat")
                            .withInputCollector(input -> {
                                section.set("item", input);
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(section.getString("item", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram item");

        buttons[20] = blockButton.buildButton();

        return buttons;
    }

}
