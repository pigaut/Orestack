package io.github.pigaut.rpg.menu.hologram.editor;

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

public class ItemHologramEditor extends GenericHologramEditor {

    public ItemHologramEditor(ConfigSection hologramSection) {
        super(hologramSection);
        if (!hologramSection.isScalar("item")) {
            section.set("item", Material.IRON_PICKAXE);
        }
    }

    @Override
    public @Nullable Button[] createButtons(@NotNull Context context) {
        Button[] buttons = super.createButtons(context);

        ButtonBuilder blockButton = Button.builder()
                .type(Material.GRASS_BLOCK)
                .enchanted(true)
                .name("&f&lItem Type")
                .addEmptyLine()
                .addLine(section.getString("item", ColorUtil.FORMATTER).orElse("not set"))
                .addEmptyLine()
                .addLeftClickLine("To set the hologram item")
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Material.class)
                            .description("Enter item type in chat")
                            .onInput(input -> {
                                section.set("item", input);
                                view.open();
                            })
                            .start();
                });

        buttons[20] = blockButton.buildButton();

        return buttons;
    }

}
