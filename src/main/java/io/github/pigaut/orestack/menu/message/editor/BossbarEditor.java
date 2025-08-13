package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.*;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class BossbarEditor extends GenericMessageEditor {

    private final ConfigSequence progressSequence;

    public BossbarEditor(ConfigSection messageSection) {
        super("Edit Boss Bar", messageSection);
        this.messageSection.set("type", "bossbar");
        progressSequence = this.messageSection.getSequenceOrCreate("progress");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final ButtonBuilder titleButton = Button.builder()
                .withType(Material.OAK_SIGN)
                .withDisplay("&f&lTitle")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter title in chat")
                            .collectInput(input -> {
                                messageSection.set("title", input);
                                view.open();
                            })
                            .beginCollection();
                });
        final String title = messageSection.getOptionalString("title", StringColor.FORMATTER).orElse("none");
        titleButton.addLore("")
                .addLore("&f" + title)
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar title");

        final ButtonBuilder styleButton = Button.builder()
                .withType(Material.CHAIN)
                .withDisplay("&f&lStyle")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createMenuSelector()
                            .withDescription("Select Bar Style")
                            .addValue(new ValueInputButton(Material.OAK_WOOD, "SOLID"))
                            .addValue(new ValueInputButton(Material.OAK_LOG, "SEGMENTED_6"))
                            .addValue(new ValueInputButton(Material.OAK_PLANKS, "SEGMENTED_10"))
                            .addValue(new ValueInputButton(Material.OAK_STAIRS, "SEGMENTED_12"))
                            .addValue(new ValueInputButton(Material.OAK_SLAB, "SEGMENTED_20"))
                            .collectInput(input -> {
                                messageSection.set("style", input);
                                view.open();
                            })
                            .beginCollection();
                });
        final String style = messageSection.getOptionalString("style", StringStyle.TITLE).orElse("none");
        styleButton.addLore("")
                .addLore("&f" + style)
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar style");

        final ButtonBuilder durationButton = Button.builder()
                .withType(Material.GLASS_BOTTLE)
                .withDisplay("&f&lDuration")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter duration amount in chat")
                            .collectInput(input -> {
                                final Integer duration = Deserializers.getInteger(input);
                                if (duration != null) {
                                    messageSection.set("duration", duration);
                                }
                                view.open();
                            })
                            .beginCollection();
                });
        final int duration = messageSection.getOptionalInteger("duration").orElse(100);
        durationButton.addLore("")
                .addLore("&f" + duration + " ticks")
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar duration");


        final ButtonBuilder colorButton = Button.builder()
                .withType(Material.RED_DYE)
                .withDisplay("&f&lColor")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createMenuSelector()
                            .withDescription("Select Bar Color")
                            .addValue(new ValueInputButton(Material.PINK_DYE, "PINK"))
                            .addValue(new ValueInputButton(Material.BLUE_DYE, "BLUE"))
                            .addValue(new ValueInputButton(Material.RED_DYE, "RED"))
                            .addValue(new ValueInputButton(Material.GREEN_DYE, "GREEN"))
                            .addValue(new ValueInputButton(Material.YELLOW_DYE, "YELLOW"))
                            .addValue(new ValueInputButton(Material.PURPLE_DYE, "PURPLE"))
                            .addValue(new ValueInputButton(Material.WHITE_DYE, "WHITE"))
                            .collectInput(input -> {
                                messageSection.set("color", input);
                                view.open();
                            })
                            .beginCollection();
                });
        final String color = messageSection.getOptionalString("color", StringStyle.TITLE).orElse("none");
        colorButton.addLore("")
                .addLore("&f" + color)
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar color");

        final ButtonBuilder progressButton = Button.builder()
                .withType(Material.EXPERIENCE_BOTTLE)
                .withDisplay("&f&lProgress")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter bar progress in chat")
                            .collectInput(input -> {
                                final Double progress = Deserializers.getDouble(input);
                                if (progress != null) {
                                    progressSequence.add(progress);
                                }
                                view.open();
                            })
                            .beginCollection();
                })
                .onRightClick((view, player, event) -> {
                    if (!progressSequence.isEmpty()) {
                        progressSequence.remove(progressSequence.size() - 1);
                    }
                    view.update();
                });
        progressButton.addLore("&7Info: &fBar progress must range from 0.0 (empty) to 1.0 (full)")
                .addLore("");
        progressSequence.toStringList().forEach(amount ->
                progressButton.addLore("&f- " + amount));
        progressButton.addLore("")
                .addLore("&eLeft-Click: &fTo add element to list")
                .addLore("&cRight-Click: &fTo remove element from list");

        buttons[11] = titleButton.buildButton();
        buttons[19] = styleButton.buildButton();
        buttons[20] = durationButton.buildButton();
        buttons[21] = colorButton.buildButton();
        buttons[29] = progressButton.buildButton();

        return buttons;
    }

}
