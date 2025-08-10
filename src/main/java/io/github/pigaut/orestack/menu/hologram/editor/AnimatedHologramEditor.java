package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AnimatedHologramEditor extends GenericHologramEditor {

    private final ConfigSequence frameSequence;

    public AnimatedHologramEditor(ConfigSection hologramSection) {
        super(hologramSection);
        hologramSection.set("type", "animated");
        frameSequence = hologramSection.getSequenceOrCreate("frames");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final ButtonBuilder framesButton = Button.builder()
                .withType(Material.OAK_SIGN)
                .enchanted(true)
                .withDisplay("&f&lFrames")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter frame text in chat")
                            .onInput(input -> {
                                frameSequence.add(input);
                                view.open();
                            })
                            .collect();
                })
                .onRightClick((view, player, event) -> {
                    if (!frameSequence.isEmpty()) {
                        frameSequence.remove(frameSequence.size() - 1);
                    }
                    view.update();
                })
                .addLore("")
                .addLore(frameSequence.isEmpty() ? List.of("none") : frameSequence.toStringList().stream().map(lore -> "- " + lore).toList())
                .addLore("")
                .addLore("&eLeft-Click: &fTo add element to list")
                .addLore("&cRight-Click: &fTo remove element from list");

        buttons[20] = framesButton.buildButton();
        return buttons;
    }

}
