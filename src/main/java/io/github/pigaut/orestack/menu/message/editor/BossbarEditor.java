package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.formatter.*;
import io.github.pigaut.yaml.parser.*;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class BossbarEditor extends GenericMessageEditor {

    public BossbarEditor(EnhancedPlugin plugin, ConfigSection parent, String name) {
        super(plugin, "Edit Boss Bar", parent, name);
        final ConfigSection section = parent.getSectionOrCreate(name);
        section.set("type", "bossbar");
        section.set("title", "none");
    }

    @Override
    public @Nullable Button[] createButtons() {
        final ConfigSection section = parent.getSectionOrCreate(name);

        final Button[] buttons = super.createButtons();
        final ButtonBuilder titleButton = Button.builder()
                .withType(Material.OAK_SIGN)
                .withDisplay("&f&lTitle")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter title in chat")
                            .onInput(input -> {
                                section.set("title", input);
                                player.setOpenView(view);
                            })
                            .collect();
                });

        final String title = section.getOptionalString("title", StringColor.FORMATTER).orElse("");
        titleButton.addLore("")
                .addLore("&f" + title)
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar title");

        final ButtonBuilder styleButton = Button.builder()
                .withType(Material.CHAIN)
                .withDisplay("&f&lStyle")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createMenuSelector()
                            .withDescription("Select Bar Style")
                            .addValue(new ValueInputButton(Material.OAK_WOOD, "SOLID"))
                            .addValue(new ValueInputButton(Material.OAK_LOG, "SEGMENTED_6"))
                            .addValue(new ValueInputButton(Material.OAK_PLANKS, "SEGMENTED_10"))
                            .addValue(new ValueInputButton(Material.OAK_STAIRS, "SEGMENTED_12"))
                            .addValue(new ValueInputButton(Material.OAK_SLAB, "SEGMENTED_20"))
                            .onInput(input -> {
                                section.set("style", input);
                                player.setOpenView(view);
                            })
                            .collect();
                });

        final String style = section.getOptionalString("style", StringStyle.TITLE).orElse("none");
        styleButton.addLore("")
                .addLore("&f" + style)
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar style");

        final ButtonBuilder durationButton = Button.builder()
                .withType(Material.GLASS_BOTTLE)
                .withDisplay("&f&lDuration")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter duration amount in chat")
                            .onInput(input -> {
                                final Integer duration = Deserializers.getInteger(input);
                                if (duration != null) {
                                    section.set("duration", duration);
                                }
                                player.setOpenView(view);
                            })
                            .collect();
                });

        final int duration = section.getOptionalInteger("duration").orElse(100);
        durationButton.addLore("")
                .addLore("&f" + duration + " ticks")
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar duration");


        final ButtonBuilder colorButton = Button.builder()
                .withType(Material.RED_DYE)
                .withDisplay("&f&lColor")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createMenuSelector()
                            .withDescription("Select Bar Color")
                            .addValue(new ValueInputButton(Material.PINK_DYE, "PINK"))
                            .addValue(new ValueInputButton(Material.BLUE_DYE, "BLUE"))
                            .addValue(new ValueInputButton(Material.RED_DYE, "RED"))
                            .addValue(new ValueInputButton(Material.GREEN_DYE, "GREEN"))
                            .addValue(new ValueInputButton(Material.YELLOW_DYE, "YELLOW"))
                            .addValue(new ValueInputButton(Material.PURPLE_DYE, "PURPLE"))
                            .addValue(new ValueInputButton(Material.WHITE_DYE, "WHITE"))
                            .onInput(input -> {
                                section.set("color", input);
                                player.setOpenView(view);
                            })
                            .collect();
                });

        final String color = section.getOptionalString("color", StringStyle.TITLE).orElse("none");
        colorButton.addLore("")
                .addLore("&f" + color)
                .addLore("")
                .addLore("&eLeft-Click: &fTo set bar color");

        final ButtonBuilder progressButton = Button.builder()
                .withType(Material.EXPERIENCE_BOTTLE)
                .withDisplay("&f&lProgress")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final ConfigSequence sequence = section.getSequenceOrCreate("progress");
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter bar progress in chat")
                            .onInput(input -> {
                                final Double progress = Deserializers.getDouble(input);
                                if (progress != null) {
                                    sequence.add(progress);
                                }
                                player.setOpenView(view);
                            })
                            .collect();
                })
                .onRightClick((view, event) -> {
                    final ConfigSequence sequence = section.getSequenceOrCreate("progress");
                    if (!sequence.isEmpty()) {
                        sequence.remove(sequence.size() - 1);
                    }
                    view.update();
                });

        progressButton.addLore("&7Info: &fBar progress must range from 0.0 (empty) to 1.0 (full)")
                .addLore("");
        section.getStringList("progress").forEach(amount ->
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
