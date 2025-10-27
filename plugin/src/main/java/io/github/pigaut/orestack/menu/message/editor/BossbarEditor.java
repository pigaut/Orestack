package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class BossbarEditor extends GenericMessageEditor {

    private final ConfigSequence progressSequence;

    public BossbarEditor(ConfigSection messageSection) {
        super("Edit Boss Bar", messageSection);
        section.set("type", "bossbar");
        progressSequence = section.getSequenceOrCreate("progress");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final ButtonBuilder titleButton = Button.builder()
                .withType(Material.OAK_SIGN)
                .withDisplay("&f&lTitle")
                .enchanted(true)
                .addLore("")
                .addLore(section.getString("title", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLeftClickLore("To set bar title")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter title in chat")
                            .withInputCollector(input -> {
                                section.set("title", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder styleButton = Button.builder()
                .withType(Material.CHAIN)
                .withDisplay("&f&lStyle")
                .enchanted(true)
                .addLore("")
                .addLore(section.getString("style", CaseStyle.TITLE).orElse("none"))
                .addLore("")
                .addLeftClickLore("To set bar style")
                .onLeftClick((view, player, event) -> {
                    player.createMenuInputSelection()
                            .withDescription("Select Bar Style")
                            .addValue(new ValueInputButton(Material.OAK_WOOD, "SOLID"))
                            .addValue(new ValueInputButton(Material.OAK_LOG, "SEGMENTED_6"))
                            .addValue(new ValueInputButton(Material.OAK_PLANKS, "SEGMENTED_10"))
                            .addValue(new ValueInputButton(Material.OAK_STAIRS, "SEGMENTED_12"))
                            .addValue(new ValueInputButton(Material.OAK_SLAB, "SEGMENTED_20"))
                            .withInputCollector(input -> {
                                section.set("style", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder durationButton = Button.builder()
                .withType(Material.GLASS_BOTTLE)
                .withDisplay("&f&lDuration")
                .enchanted(true)
                .addLore("")
                .addLore(section.getInteger("duration").orElse(100) + " ticks")
                .addLore("")
                .addLeftClickLore("To set bar duration")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Integer.class)
                            .withDescription("Enter duration amount in chat")
                            .withInputCollector(input -> {
                                section.set("duration", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder colorButton = Button.builder()
                .withType(Material.RED_DYE)
                .withDisplay("&f&lColor")
                .enchanted(true)
                .addLore("")
                .addLore(section.getString("color", CaseStyle.TITLE).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar color")
                .onLeftClick((view, player, event) -> {
                    player.createMenuInputSelection()
                            .withDescription("Select Bar Color")
                            .addValue(new ValueInputButton(Material.PINK_DYE, "PINK"))
                            .addValue(new ValueInputButton(Material.BLUE_DYE, "BLUE"))
                            .addValue(new ValueInputButton(Material.RED_DYE, "RED"))
                            .addValue(new ValueInputButton(Material.GREEN_DYE, "GREEN"))
                            .addValue(new ValueInputButton(Material.YELLOW_DYE, "YELLOW"))
                            .addValue(new ValueInputButton(Material.PURPLE_DYE, "PURPLE"))
                            .addValue(new ValueInputButton(Material.WHITE_DYE, "WHITE"))
                            .withInputCollector(input -> {
                                section.set("color", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder progressButton = Button.builder()
                .withType(Material.EXPERIENCE_BOTTLE)
                .withDisplay("&f&lProgress")
                .enchanted(true)
                .addLore("&7Info: &fBar progress must range from 0.0 (empty) to 1.0 (full)")
                .addLore("")
                .addLore("")
                .addLeftClickLore("To add element to list")
                .addRightClickLore("To remove element from list")
                .addLore(progressSequence.stream().map(amount -> "- " + amount).toList())
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter bar progress in chat")
                            .withInputCollector(input -> {
                                progressSequence.add(input);
                                view.open();
                            })
                            .collect();
                })
                .onRightClick((view, player, event) -> {
                    if (!progressSequence.isEmpty()) {
                        progressSequence.remove(progressSequence.size() - 1);
                    }
                    view.update();
                });

        buttons[11] = titleButton.buildButton();
        buttons[19] = styleButton.buildButton();
        buttons[20] = durationButton.buildButton();
        buttons[21] = colorButton.buildButton();
        buttons[29] = progressButton.buildButton();

        return buttons;
    }

}
