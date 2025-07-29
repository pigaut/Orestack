package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.orestack.menu.hologram.*;
import io.github.pigaut.orestack.menu.hologram.editor.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class HologramMessageEditor extends GenericMessageEditor {

    public HologramMessageEditor(ConfigSection messageSection) {
        super("Edit Hologram Message", messageSection);
        messageSection.set("type", "hologram");
        if (!messageSection.isSection("hologram") && !messageSection.isSequence("hologram")) {
            messageSection.getSectionOrCreate("hologram");
        }
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();
        final ButtonBuilder hologramButton = Button.builder()
                .enchanted(true)
                .withType(Material.BEACON)
                .addLore("")
                .addLore("&eLeft-Click: &fTo edit hologram")
                .addLore("&6Right-Click: &fTo change hologram type")
                .onRightClick((view, player, event) -> player.openMenu(new HologramCreationMenu(messageSection, "Select Hologram Type")));

        final ConfigField hologramField = messageSection.getField("hologram");
        if (hologramField instanceof ConfigSection hologramSection) {
            boolean animated = hologramSection.contains("frames");
            if (animated) {
                hologramButton.withDisplay("&f&lAnimated Hologram")
                        .onLeftClick((view, player, event) -> player.openMenu(new AnimatedHologramEditor(hologramSection)));
            }
            else {
                hologramButton.withDisplay("&f&lFixed Hologram")
                        .onLeftClick((view, player, event) -> player.openMenu(new FixedHologramEditor(hologramSection)));
            }
        }
        else {
            ConfigSequence hologramSequence = messageSection.getSequenceOrCreate("hologram");
            hologramButton.withDisplay("&f&lMulti-line Hologram")
                    .onLeftClick((view, player, event) -> player.openMenu(new MultiHologramEditor(hologramSequence)));
        }

        final ButtonBuilder xRangeButton = Button.builder()
                .withType(Material.RED_WOOL)
                .withDisplay("&f&lRange X")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter x-range amount in chat")
                            .onInput(input -> {
                                final Integer xRange = Deserializers.getInteger(input);
                                if (xRange != null) {
                                    messageSection.set("radius.x", xRange);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(messageSection.getOptionalString("radius.x").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram x radius");

        final ButtonBuilder yRangeButton = Button.builder()
                .withType(Material.LIME_WOOL)
                .withDisplay("&f&lRange Y")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter y-radius amount in chat")
                            .onInput(input -> {
                                final Integer yRange = Deserializers.getInteger(input);
                                if (yRange != null) {
                                    messageSection.set("radius.y", yRange);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(messageSection.getOptionalString("radius.y").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram y radius");

        final ButtonBuilder zRangeButton = Button.builder()
                .withType(Material.BLUE_WOOL)
                .withDisplay("&f&lRange Z")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter z-radius amount in chat")
                            .onInput(input -> {
                                final Integer zRange = Deserializers.getInteger(input);
                                if (zRange != null) {
                                    messageSection.set("radius.z", zRange);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(messageSection.getOptionalString("radius.z").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram z radius");

        final String duration = messageSection.getOptionalString("duration").orElse(null);
        final ButtonBuilder durationButton = Button.builder()
                .withType(Material.GLASS_BOTTLE)
                .withDisplay("&f&lDuration")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter duration amount in chat")
                            .onInput(input -> {
                                final Integer amount = Deserializers.getInteger(input);
                                if (amount != null) {
                                    messageSection.set("duration", amount);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(duration != null ? (duration + " ticks") : "none")
                .addLore("")
                .addLore("&eLeft-Click: &fTo set hologram duration");

        buttons[11] = hologramButton.buildButton();
        buttons[19] = xRangeButton.buildButton();
        buttons[20] = yRangeButton.buildButton();
        buttons[21] = zRangeButton.buildButton();
        buttons[29] = durationButton.buildButton();

        return buttons;
    }
}
