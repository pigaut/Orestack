package io.github.pigaut.orestack.menu.particle.editor;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.jetbrains.annotations.*;

public class GenericParticleEditor extends FramedEditor {

    public GenericParticleEditor(String title, ConfigSection particleSection) {
        super(particleSection, title, MenuSize.LARGE);
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final Integer delay = section.getInteger("delay").orElse(null);
        final ButtonBuilder delayButton = Button.builder()
                .withType(Material.CLOCK)
                .withDisplay("&f&lDelay")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter delay amount in chat")
                            .withInputCollector(input -> {
                                final Integer amount = ParseUtil.parseIntegerOrNull(input);
                                if (amount != null) {
                                    section.set("delay", amount);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(delay != null ? (delay + " ticks") : "none")
                .addLore("")
                .addLore("&eLeft-Click: &fSet message delay");

        final ButtonBuilder repetitionsButton = Button.builder()
                .withType(Material.REPEATER)
                .enchanted(true)
                .withDisplay("&f&lRepetitions")
                .addLore("")
                .addLore(section.getString("repeat|repetitions").orElse("none"))
                .addLore("")
                .addLeftClickLore("To set message repetitions")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Integer.class)
                            .withDescription("Enter repetitions amount in chat")
                            .withInputCollector(input -> {
                                section.set("repeat|repetitions", input);
                                view.open();
                            })
                            .collect();
                });

        final Integer interval = section.getInteger("interval").orElse(null);
        final ButtonBuilder intervalButton = Button.builder()
                .withType(Material.REDSTONE)
                .enchanted(true)
                .withDisplay("&f&lInterval")
                .addLore("")
                .addLore(interval != null ? (interval + " ticks") : "none")
                .addLore("")
                .addLeftClickLore("To set message interval")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Integer.class)
                            .withDescription("Enter interval amount in chat")
                            .withInputCollector(input -> {
                                if (!section.isSet("repeat|repetitions")) {
                                    section.set("repeat|repetitions", 2);
                                }
                                section.set("interval", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder offsetButton = Button.builder()
                .withType(Material.FEATHER)
                .enchanted(true)
                .withDisplay("&f&lOffset")
                .addLore("")
                .addLore("&cx: &f" + section.getString("offset.x").orElse("none"))
                .addLore("&ay: &f" + section.getString("offset.y").orElse("none"))
                .addLore("&9z: &f" + section.getString("offset.z").orElse("none"))
                .addLore("")
                .addLeftClickLore("To set &cx &foffset")
                .addRightClickLore("To set &ay &foffset")
                .addLeftClickLore("To set &9z &foffset")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter x offset in chat")
                            .withInputCollector(input -> {
                                section.set("offset.x", input);
                                view.open();
                            })
                            .collect();
                })
                .onRightClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter y offset in chat")
                            .withInputCollector(input -> {
                                section.set("offset.y", input);
                                view.open();
                            })
                            .collect();
                })
                .onShiftLeftClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter z offset in chat")
                            .withInputCollector(input -> {
                                section.set("offset.z", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder rangeButton = Button.builder()
                .withType(Material.FEATHER)
                .enchanted(true)
                .withDisplay("&f&lRange")
                .addLore("")
                .addLore("&cx: &f" + section.getString("range.x").orElse("none"))
                .addLore("&ay: &f" + section.getString("range.y").orElse("none"))
                .addLore("&9z: &f" + section.getString("range.z").orElse("none"))
                .addLore("")
                .addLeftClickLore("To set &cx &foffset")
                .addRightClickLore("To set &ay &foffset")
                .addLeftClickLore("To set &9z &foffset")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter x offset in chat")
                            .withInputCollector(input -> {
                                section.set("offset.x", input);
                                view.open();
                            })
                            .collect();
                })
                .onRightClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter y offset in chat")
                            .withInputCollector(input -> {
                                section.set("offset.y", input);
                                view.open();
                            })
                            .collect();
                })
                .onShiftLeftClick((view, player, event) -> {
                    player.createChatInput(Double.class)
                            .withDescription("Enter z offset in chat")
                            .withInputCollector(input -> {
                                section.set("offset.z", input);
                                view.open();
                            })
                            .collect();
                });

        buttons[15] = delayButton.buildButton();
        buttons[23] = rangeButton.buildButton();
        buttons[24] = repetitionsButton.buildButton();
        buttons[25] = null;
        buttons[33] = intervalButton.buildButton();

        return buttons;
    }

}
