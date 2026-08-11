package io.github.pigaut.rpg.menu.hologram.editor;

import io.github.pigaut.rpg.menu.hologram.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.editor.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.editor.*;
import io.github.pigaut.rpg.menu.hologram.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

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
    public List<Button> createEntries(@NotNull Context context) {
        final List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < hologramSequence.size(); i++) {
            ConfigSection hologramSection = hologramSequence.getSectionOrCreate(i);

            int hologramIndex = i;
            ButtonBuilder hologramButton = Button.builder()
                    .amount(hologramIndex + 1)
                    .enchanted(true)
                    .addEmptyLine()
                    .addLeftClickLine("To edit hologram")
                    .addRightClickLine("To remove hologram")
                    .onRightClick((view, playerState) -> {
                        hologramSequence.remove(hologramIndex);
                        view.update();
                    });

            if (hologramSection.isSet("line|text")) {
                hologramButton.type(Material.NAME_TAG)
                        .name(hologramSection.getString("line|text").orElse("not set"))
                        .onLeftClick((view, player) -> player.openMenu(new SingleLineHologramEditor(hologramSection)));
            }

            else if (hologramSection.isSet("lines")) {
                hologramButton.type(Material.OAK_SIGN)
                        .name(hologramSection.getString("lines[0]").orElse("not set"))
                        .onLeftClick((view, player) -> player.openMenu(new SingleLineHologramEditor(hologramSection)));
            }

            else if (hologramSection.isSet("frames")) {
                hologramButton.type(Material.PAINTING)
                        .name(hologramSection.getString("frames[0]").orElse("not set"))
                        .onLeftClick((view, player) -> player.openMenu(new AnimatedHologramEditor(hologramSection)));
            }

            else if (hologramSection.isSet("item")) {
                hologramButton.type(Material.IRON_PICKAXE)
                        .onLeftClick((view, player) -> player.openMenu(new ItemHologramEditor(hologramSection)));

                if (hologramSection.isSet("item")) {
                    hologramButton.name(hologramSection.getString("item").orElse("not set"));
                }
                else if (hologramSection.isSet("item.material")) {
                    hologramButton.name(hologramSection.getString("item.material", CaseStyle.TITLE).orElse("not set"));
                }
                else {
                    hologramButton.name("not set");
                }
            }

            else if (hologramSection.isSet("block")) {
                hologramButton.type(Material.GRASS_BLOCK)
                        .name(hologramSection.getString("block", CaseStyle.TITLE).orElse("not set"))
                        .onLeftClick((view, player) -> player.openMenu(new BlockHologramEditor(hologramSection)));
            }

            else {
                hologramButton.type(Material.BARRIER).name("&4Invalid");
            }

            buttons.add(hologramButton.buildButton());
        }

        ButtonBuilder addHologramButton = Button.builder()
                .type(Material.LIME_DYE)
                .enchanted(true)
                .name("&2Add New Hologram")
                .onLeftClick((view, player) ->
                        player.openMenu(new HologramCreationMenu(hologramSequence.addEmptySection(), false)));

        buttons.add(addHologramButton.buildButton());

        return buttons;
    }

}
