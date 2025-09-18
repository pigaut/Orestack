package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.orestack.menu.hologram.*;
import io.github.pigaut.orestack.menu.hologram.editor.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
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
                .onRightClick((view, player, event) -> {
                    final ConfigSection hologramSection = section.getSectionOrCreate("hologram");
                    player.openMenu(new HologramCreationMenu(hologramSection, true));
                });

        final ConfigField hologramField = section.getRequiredField("hologram");
        if (hologramField instanceof ConfigSection hologramSection) {
            final String hologramType = hologramSection.getString("type", CaseStyle.CONSTANT).orElse("");
            switch (hologramType) {
                case "STATIC" -> hologramButton.withDisplay("&f&lStatic Hologram")
                            .onLeftClick((view, player, event) -> player.openMenu(new StaticHologramEditor(hologramSection)));
                case "ANIMATED" -> hologramButton.withDisplay("&f&lAnimated Hologram")
                        .onLeftClick((view, player, event) -> player.openMenu(new AnimatedHologramEditor(hologramSection)));
                case "ITEM_DISPLAY" -> hologramButton.withDisplay("&f&lItem Hologram")
                        .onLeftClick((view, player, event) -> player.openMenu(new ItemHologramEditor(hologramSection)));
                case "BLOCK_DISPLAY" -> hologramButton.withDisplay("&f&lBlock Hologram")
                        .onLeftClick((view, player, event) -> player.openMenu(new BlockHologramEditor(hologramSection)));
                default -> {
                    hologramSection.set("type", "static");
                    hologramButton.withDisplay("&f&lStatic Hologram")
                            .onLeftClick((view, player, event) -> player.openMenu(new StaticHologramEditor(hologramSection)));
                }
            }
        }
        else {
            ConfigSequence hologramSequence = section.getSequenceOrCreate("hologram");
            hologramButton.withDisplay("&f&lMulti-line Hologram")
                    .onLeftClick((view, player, event) -> player.openMenu(new MultiHologramEditor(hologramSequence)));
        }

        final ButtonBuilder xRangeButton = Button.builder()
                .withType(Material.RED_WOOL)
                .withDisplay("&f&lRange X")
                .enchanted(true)
                .addLore("")
                .addLore(section.getString("radius.x").orElse("none"))
                .addLore("")
                .addLeftClickLore("To set hologram x radius")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter x-range amount in chat")
                            .withInputCollector(input -> {
                                section.set("radius.x", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder yRangeButton = Button.builder()
                .withType(Material.LIME_WOOL)
                .withDisplay("&f&lRange Y")
                .enchanted(true)
                .addLore("")
                .addLore(section.getString("radius.y").orElse("none"))
                .addLore("")
                .addLeftClickLore("To set hologram y radius")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter y-radius amount in chat")
                            .withInputCollector(input -> {
                                section.set("radius.y", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder zRangeButton = Button.builder()
                .withType(Material.BLUE_WOOL)
                .withDisplay("&f&lRange Z")
                .enchanted(true)
                .addLore("")
                .addLore(section.getString("radius.z").orElse("none"))
                .addLore("")
                .addLeftClickLore("To set hologram z radius")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter z-radius amount in chat")
                            .withInputCollector(input -> {
                                section.set("radius.z", input);
                                view.open();
                            })
                            .collect();
                });

        final String duration = section.getString("duration").orElse(null);
        final ButtonBuilder durationButton = Button.builder()
                .withType(Material.GLASS_BOTTLE)
                .withDisplay("&f&lDuration")
                .enchanted(true)
                .addLore("")
                .addLore(duration != null ? (duration + " ticks") : "none")
                .addLore("")
                .addLeftClickLore("To set hologram duration")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter duration amount in chat")
                            .withInputCollector(input -> {
                                section.set("duration", input);
                                view.open();
                            })
                            .collect();
                });

        buttons[11] = hologramButton.buildButton();
        buttons[19] = xRangeButton.buildButton();
        buttons[20] = yRangeButton.buildButton();
        buttons[21] = zRangeButton.buildButton();
        buttons[29] = durationButton.buildButton();

        return buttons;
    }
}
