package io.github.pigaut.orestack.menu.hologram.editor;

import io.github.pigaut.orestack.menu.hologram.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;

import java.util.*;

public class MultiHologramEditor extends FramedSelectionEditor {

    private final ConfigSequence hologramSequence;

    public MultiHologramEditor(ConfigSequence hologramSequence) {
        super(hologramSequence.getRoot(), "Edit Hologram Lines", MenuSize.BIG);
        this.hologramSequence = hologramSequence;
    }

    @Override
    public Button getFrameButton() {
        return Buttons.WHITE_PANEL;
    }

    @Override
    public List<Button> createEntries() {
        final List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < hologramSequence.size(); i++) {
            final ConfigSection hologramSection = hologramSequence.getSectionOrCreate(i);
            final String hologramType = hologramSection.getString("type", CaseStyle.CONSTANT).orElse("");

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
                            .withDisplay(hologramSection.getString("text", StringColor.FORMATTER).orElse("none"))
                            .onLeftClick((view, player, event) -> player.openMenu(new StaticHologramEditor(hologramSection)));
                case "ANIMATED" -> hologramButton.withType(Material.ITEM_FRAME)
                        .withDisplay(hologramSection.getString("frames[0]").orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new AnimatedHologramEditor(hologramSection)));
                case "ITEM_DISPLAY" -> {
                    hologramButton.withType(Material.IRON_PICKAXE)
                            .onLeftClick((view, player, event) -> player.openMenu(new ItemHologramEditor(hologramSection)));

                    if (hologramSection.isSet("item")) {
                        hologramButton.withDisplay(hologramSection.getString("item", CaseStyle.CONSTANT).orElse("none"));
                    }
                    else if (hologramSection.isSet("item.material")) {
                        hologramButton.withDisplay(hologramSection.getString("item.material", CaseStyle.CONSTANT).orElse("none"));
                    }
                    else {
                        hologramButton.withDisplay("none");
                    }
                }
                case "BLOCK_DISPLAY" -> hologramButton.withType(Material.GRASS_BLOCK)
                        .withDisplay(hologramSection.getString("block", CaseStyle.CONSTANT).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new BlockHologramEditor(hologramSection)));

                default -> {
                    hologramSection.set("type", "static");
                    hologramButton.withType(Material.PAINTING)
                            .withDisplay(hologramSection.getString("text", StringColor.FORMATTER).orElse("none"))
                            .onLeftClick((view, player, event) -> player.openMenu(new StaticHologramEditor(hologramSection)));
                }
            }

            buttons.add(hologramButton.buildButton());
        }

        ButtonBuilder addHologramLine = Button.builder()
                .withType(Material.LIME_DYE)
                .enchanted(true)
                .withDisplay("&2Add New Hologram")
                .onLeftClick((view, player, event) ->
                        player.openMenu(new HologramCreationMenu(hologramSequence.addEmptySection(), false)));

        buttons.add(addHologramLine.buildButton());

        return buttons;
    }

}
