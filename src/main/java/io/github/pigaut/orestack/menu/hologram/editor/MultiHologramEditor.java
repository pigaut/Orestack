package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.orestack.menu.hologram.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.*;
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
            final ConfigSection hologramSection = hologramSequence.getSectionOrCreate(i);
            final String hologramType = hologramSection.getOptionalString("type", StringStyle.CONSTANT).orElse("");

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

            switch (hologramType) {
                case "STATIC" ->
                    hologramButton.withType(Material.PAINTING)
                            .withDisplay(hologramSection.getOptionalString("text", StringColor.FORMATTER).orElse("none"))
                            .onLeftClick((view, player, event) -> player.openMenu(new StaticHologramEditor(hologramSection)));
                case "ANIMATED" -> hologramButton.withType(Material.ITEM_FRAME)
                        .withDisplay(hologramSection.getSequence("frames").getOptionalString(0).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new AnimatedHologramEditor(hologramSection)));
                case "ITEM_DISPLAY" -> {
                    hologramButton.withType(Material.IRON_PICKAXE)
                            .onLeftClick((view, player, event) -> player.openMenu(new ItemHologramEditor(hologramSection)));

                    if (hologramSection.isSet("item")) {
                        hologramButton.withDisplay(hologramSection.getOptionalString("item", StringStyle.CONSTANT).orElse("none"));
                    }
                    else if (hologramSection.isSet("item.material")) {
                        hologramButton.withDisplay(hologramSection.getOptionalString("item.material", StringStyle.CONSTANT).orElse("none"));
                    }
                    else {
                        hologramButton.withDisplay("none");
                    }
                }
                case "BLOCK_DISPLAY" -> hologramButton.withType(Material.GRASS_BLOCK)
                        .withDisplay(hologramSection.getOptionalString("block", StringStyle.CONSTANT).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new BlockHologramEditor(hologramSection)));

                default -> {
                    hologramSection.set("type", "static");
                    hologramButton.withType(Material.PAINTING)
                            .withDisplay(hologramSection.getOptionalString("text", StringColor.FORMATTER).orElse("none"))
                            .onLeftClick((view, player, event) -> player.openMenu(new StaticHologramEditor(hologramSection)));
                }
            }

            buttons.add(hologramButton.buildButton());
        }

        ButtonBuilder addHologramLine = Button.builder()
                .withType(Material.LIME_DYE)
                .enchanted(true)
                .withDisplay("&2Add New Hologram")
                .onLeftClick((view, player, event) -> player.openMenu(new HologramCreationMenu(hologramSequence.addSection(), false)));

        buttons.add(addHologramLine.buildButton());

        return buttons;
    }

}
