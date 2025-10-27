package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class BlockHologramEditor extends GenericHologramEditor {

    public BlockHologramEditor(ConfigSection hologramSection) {
        super(hologramSection);
        section.set("type", "block_display");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final ButtonBuilder blockButton = Button.builder()
                .withType(Material.GRASS_BLOCK)
                .withDisplay("&f&lBlock Type")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Material.class)
                            .withDescription("Enter block type in chat")
                            .withInputCollector(input -> {
                                section.set("block", input);
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(section.getString("block", CaseStyle.CONSTANT).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram block");

        buttons[20] = blockButton.buildButton();

        return buttons;
    }

}
