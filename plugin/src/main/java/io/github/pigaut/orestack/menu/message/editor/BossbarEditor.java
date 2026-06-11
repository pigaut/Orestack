package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.menu.button.*;
import io.github.pigaut.voxel.core.menu.template.button.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BossbarEditor extends GenericMessageEditor {

    public BossbarEditor(ConfigSection section) {
        super("Edit Boss Bar", section);
        if (!section.isSet("bossbar|boss-healthBar")) {
            section.set("bossbar|boss-healthBar", "not set");
        }
    }

    @Override
    public @Nullable Button[] createButtons(@NotNull Context context) {
        Button[] buttons = super.createButtons(context);

        ButtonBuilder barTitleButton = Button.builder()
                .type(Material.OAK_SIGN)
                .name("&f&lTitle")
                .enchanted(true)
                .addEmptyLine()
                .addLine(section.getString("bossbar|boss-healthBar", StringColor.FORMATTER).orElse("not set"))
                .addEmptyLine()
                .addLeftClickLine("To set healthBar title")
                .onLeftClick((view, player) -> {
                    player.collectChatInput()
                            .description("Enter bossbar title in chat")
                            .onInput(input -> section.set("bossbar|boss-healthBar", input))
                            .start();
                });

        ButtonBuilder styleButton = Button.builder()
                .type(MaterialUtil.getMaterial("CHAIN"))
                .name("&f&lStyle")
                .enchanted(true)
                .addEmptyLine()
                .addLine(section.getString("style", CaseStyle.TITLE).orElse("not set"))
                .addEmptyLine()
                .addLeftClickLine("To select the healthBar style")
                .onLeftClick((view, player) -> {
                    player.collectMenuSelection()
                            .description("Select Bar Style")
                            .addValue(new ValueInputButton(Material.OAK_WOOD, "SOLID"))
                            .addValue(new ValueInputButton(Material.OAK_LOG, "SEGMENTED_6"))
                            .addValue(new ValueInputButton(Material.OAK_PLANKS, "SEGMENTED_10"))
                            .addValue(new ValueInputButton(Material.OAK_STAIRS, "SEGMENTED_12"))
                            .addValue(new ValueInputButton(Material.OAK_SLAB, "SEGMENTED_20"))
                            .onInput(input -> section.set("style", input))
                            .start();
                });

        ButtonBuilder durationButton = Button.builder()
                .type(Material.GLASS_BOTTLE)
                .name("&f&lDuration")
                .enchanted(true)
                .addEmptyLine()
                .addLine(section.getInteger("duration").orElse(100) + " ticks")
                .addEmptyLine()
                .addLeftClickLine("To set healthBar duration")
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Delay.class)
                            .description("Enter duration amount in chat")
                            .onInput(input -> section.set("duration", input))
                            .start();
                });

        ButtonBuilder colorButton = Button.builder()
                .type(Material.RED_DYE)
                .name("&f&lColor")
                .enchanted(true)
                .addEmptyLine()
                .addLine(section.getString("color", CaseStyle.TITLE).orElse("not set"))
                .addEmptyLine()
                .addLine("&eLeft-Click: &fTo select the healthBar color")
                .onLeftClick((view, player) -> {
                    player.collectMenuSelection()
                            .description("Select Bar Color")
                            .addValue(new ValueInputButton(Material.PINK_DYE, "PINK"))
                            .addValue(new ValueInputButton(Material.BLUE_DYE, "BLUE"))
                            .addValue(new ValueInputButton(Material.RED_DYE, "RED"))
                            .addValue(new ValueInputButton(Material.GREEN_DYE, "GREEN"))
                            .addValue(new ValueInputButton(Material.YELLOW_DYE, "YELLOW"))
                            .addValue(new ValueInputButton(Material.PURPLE_DYE, "PURPLE"))
                            .addValue(new ValueInputButton(Material.WHITE_DYE, "WHITE"))
                            .onInput(input -> section.set("color", input))
                            .start();
                });

        ButtonBuilder progressButton = Button.builder()
                .type(Material.EXPERIENCE_BOTTLE)
                .name("&f&lProgress")
                .enchanted(true)
                .addLine("&7Info: &fBar progress must range from 0.0 (empty) to 1.0 (full)")
                .addEmptyLine()
                .addLines(section.getStringList("progress").stream().map(StringUtil::prefixDash).toList())
                .addEmptyLine()
                .addLeftClickLine("To add element to list")
                .addRightClickLine("To remove element from list")
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Double.class)
                            .description("Enter bossbar progress in chat")
                            .onInput(input -> {
                                section.getSequenceOrCreate("progress").add(input);
                                view.update();
                            })
                            .start();
                })
                .onRightClick((view, player) -> {
                    ConfigSequence progressSequence = section.getSequenceOrCreate("progress");
                    if (!progressSequence.isEmpty()) {
                        progressSequence.remove(progressSequence.size() - 1);
                        view.update();
                    }
                });

        buttons[11] = barTitleButton.buildButton();
        buttons[19] = styleButton.buildButton();
        buttons[20] = durationButton.buildButton();
        buttons[21] = colorButton.buildButton();
        buttons[29] = progressButton.buildButton();

        return buttons;
    }

}
