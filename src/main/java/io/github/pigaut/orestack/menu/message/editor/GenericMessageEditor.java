package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class GenericMessageEditor extends FramedMenu {

    protected final ConfigSection parent;
    protected final String name;

    public GenericMessageEditor(String title, ConfigSection parent, String name) {
        super(title, MenuSize.BIG);
        this.parent = parent;
        this.name = name;
    }

    @Override
    public @Nullable Button[] createButtons() {
        final ConfigSection section = parent.getSectionOrCreate(name);

        final Button[] buttons = super.createButtons();
        final ButtonBuilder delayButton = Button.builder()
                .withType(Material.CLOCK)
                .withDisplay("&f&lDelay")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter delay amount in chat")
                            .onInput(input -> {
                                final Integer delay = Deserializers.getInteger(input);
                                if (delay != null) {
                                    section.set("delay", delay);
                                    player.setOpenView(view);
                                }
                            })
                            .collect();
                });

        final int delay = section.getOptionalInteger("delay").orElse(0);
        delayButton.addLore("")
                .addLore("&f" + delay + " ticks")
                .addLore("")
                .addLore("&7Left-Click: &fSet message delay");

        final ButtonBuilder repetitionsButton = Button.builder()
                .withType(Material.REPEATER)
                .withDisplay("&f&lRepetitions")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter repetitions amount in chat")
                            .onInput(input -> {
                                final Integer repetitions = Deserializers.getInteger(input);
                                if (repetitions != null) {
                                    section.set("repetitions|loops", repetitions);
                                    player.setOpenView(view);
                                }
                            })
                            .collect();
                });

        final int repetitions = section.getOptionalInteger("repetitions|loops").orElse(1);
        repetitionsButton
                .addLore("")
                .addLore("&f" + repetitions)
                .addLore("")
                .addLore("&7Left-Click: &fSet message repetitions");

        final ButtonBuilder intervalButton = Button.builder()
                .withType(Material.COMPARATOR)
                .withDisplay("&f&lInterval")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter interval amount in chat")
                            .onInput(input -> {
                                final Integer interval = Deserializers.getInteger(input);
                                if (interval != null) {
                                    if (!section.isSet("repetitions|loops")) {
                                        section.set("repetitions|loops", 2);
                                    }
                                    section.set("interval", interval);
                                    player.setOpenView(view);
                                }
                            })
                            .collect();
                });

        final int interval = section.getOptionalInteger("interval").orElse(0);
        intervalButton.addLore("")
                .addLore("&f" + interval + " ticks")
                .addLore("")
                .addLore("&7Left-Click: &fSet message interval");

        buttons[15] = delayButton.buildButton();
        buttons[24] = repetitionsButton.buildButton();
        buttons[33] = intervalButton.buildButton();
        buttons[39] = new ConfigLoadButton(parent.getRoot());
        buttons[41] = new ConfigSaveButton(parent.getRoot());
        return buttons;
    }

}
