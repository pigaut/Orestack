package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GenericMessageEditor extends FramedEditor {

    public GenericMessageEditor(@NotNull String title, @NotNull ConfigSection section) {
        super(section, title, MenuSize.BIG);
    }

    @Override
    public Button getFrameButton() {
        return Buttons.LIGHT_BLUE_PANEL;
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final Integer delay = section.getInteger("delay").orElse(null);
        final ButtonBuilder delayButton = Button.builder()
                .withType(Material.CLOCK)
                .withDisplay("&f&lDelay")
                .enchanted(true)
                .addLore("")
                .addLore(delay != null ? (delay + " ticks") : "none")
                .addLore("")
                .addLeftClickLore("To set message delay")
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Integer.class)
                            .description("Enter delay amount in chat")
                            .onInput(input -> {
                                section.set("delay", input);
                                view.open();
                            })
                            .start();
                });

        final ButtonBuilder repetitionsButton = Button.builder()
                .withType(Material.REPEATER)
                .withDisplay("&f&lRepetitions")
                .enchanted(true)
                .addLore("")
                .addLore(section.getString("repeat|repetitions").orElse("none"))
                .addLore("")
                .addLeftClickLore("To set message repetitions")
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Integer.class)
                            .description("Enter repetitions amount in chat")
                            .onInput(input -> {
                                section.set("repeat|repetitions", input);
                                view.open();
                            })
                            .start();
                });

        final Integer interval = section.getInteger("interval").orElse(null);
        final ButtonBuilder intervalButton = Button.builder()
                .withType(Material.COMPARATOR)
                .withDisplay("&f&lInterval")
                .enchanted(true)
                .addLore("")
                .addLore(interval != null ? (interval + " ticks") : "none")
                .addLore("")
                .addLeftClickLore("To set message interval")
                .onLeftClick((view, player) -> {
                    player.collectChatInput(Integer.class)
                            .description("Enter interval amount in chat")
                            .onInput(input -> {
                                if (!section.isSet("repeat|repetitions")) {
                                    section.set("repeat|repetitions", 2);
                                }
                                section.set("interval", input);
                                view.open();
                            })
                            .start();
                });

        buttons[15] = delayButton.buildButton();
        buttons[24] = repetitionsButton.buildButton();
        buttons[33] = intervalButton.buildButton();

        return buttons;
    }

}
