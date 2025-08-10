package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;

import java.util.*;

public class MultiHologramEditor extends FramedSelectionEditor {

    private final ConfigSequence hologramSequence;

    public MultiHologramEditor(ConfigSequence hologramSequence) {
        super(hologramSequence.getRoot(), "Edit Hologram Lines", MenuSize.BIG);
        this.hologramSequence = hologramSequence;
    }

    @Override
    public List<Button> createEntries() {
        final List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < hologramSequence.size(); i++) {
            ConfigSection hologramSection = hologramSequence.getSectionOrCreate(i);
            boolean animated = hologramSection.isSequence("frames");

            final int hologramIndex = i;
            ButtonBuilder hologramButton = Button.builder()
                    .withAmount(hologramIndex + 1)
                    .enchanted(true)
                    .addLore("")
                    .addLore("&eLeft-Click: &fTo edit hologram")
                    .addLore("&cRight-Click: &fTo remove hologram")
                    .onRightClick((view, playerState, event) -> {
                        hologramSequence.remove(hologramIndex);
                        view.update();
                    });

            if (!animated) {
                hologramButton.withType(Material.PAINTING)
                        .withDisplay(hologramSection.getOptionalString("text", StringColor.FORMATTER).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new StaticHologramEditor(hologramSection)));
            }
            else {
                hologramButton.withType(Material.ITEM_FRAME)
                        .withDisplay(hologramSection.getSequence("frames").getOptionalString(0).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new AnimatedHologramEditor(hologramSection)));
            }

            buttons.add(hologramButton.buildButton());
        }

        ButtonBuilder addHologramLine = Button.builder()
                .withType(Material.LIME_DYE)
                .enchanted(true)
                .withDisplay("&a&lAdd Hologram Line")
                .addLore("")
                .addLore("&eLeft-Click: &fTo add a new fixed line")
                .addLore("&6Right-Click: &fTo add a new animated line")
                .onLeftClick((view, player, event) -> player.openMenu(new StaticHologramEditor(hologramSequence.addSection())))
                .onRightClick((view, player, event) -> player.openMenu(new AnimatedHologramEditor(hologramSequence.addSection())));

        buttons.add(addHologramLine.buildButton());

        return buttons;
    }

}
