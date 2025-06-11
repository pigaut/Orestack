package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GenericMessageEditor extends FramedEditorMenu {

    protected final ConfigSection config;
    protected final String name;
    protected final ConfigSection messageSection;

    public GenericMessageEditor(@NotNull EnhancedPlugin plugin, @NotNull String title,
                                @NotNull ConfigSection config, @NotNull String name) {
        super(plugin, config.getRoot(), title, MenuSize.BIG);
        this.config = config;
        this.name = name;
        messageSection = config.getSectionOrCreate(name);
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        final Integer delay = messageSection.getOptionalInteger("delay").orElse(null);
        final ButtonBuilder delayButton = Button.builder()
                .withType(Material.CLOCK)
                .withDisplay("&f&lDelay")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter delay amount in chat")
                            .onInput(input -> {
                                final Integer amount = Deserializers.getInteger(input);
                                if (amount != null) {
                                    messageSection.set("delay", amount);
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
                .withDisplay("&f&lRepetitions")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter repetitions amount in chat")
                            .onInput(input -> {
                                final Integer amount = Deserializers.getInteger(input);
                                if (amount != null) {
                                    messageSection.set("repetitions|loops", amount);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(messageSection.getOptionalString("repetitions|loops").orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fSet message repetitions");

        final Integer interval = messageSection.getOptionalInteger("interval").orElse(null);
        final ButtonBuilder intervalButton = Button.builder()
                .withType(Material.COMPARATOR)
                .withDisplay("&f&lInterval")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter interval amount in chat")
                            .onInput(input -> {
                                final Integer amount = Deserializers.getInteger(input);
                                if (amount != null) {
                                    if (!messageSection.isSet("repetitions|loops")) {
                                        messageSection.set("repetitions|loops", 2);
                                    }
                                    messageSection.set("interval", amount);
                                }
                                view.open();
                            })
                            .collect();
                })
                .addLore("")
                .addLore(interval != null ? (interval + " ticks") : "none")
                .addLore("")
                .addLore("&eLeft-Click: &fSet message interval");

        buttons[15] = delayButton.buildButton();
        buttons[24] = repetitionsButton.buildButton();
        buttons[33] = intervalButton.buildButton();

        return buttons;
    }

}
