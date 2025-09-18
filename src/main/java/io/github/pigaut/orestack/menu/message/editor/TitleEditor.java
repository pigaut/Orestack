package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class TitleEditor extends GenericMessageEditor {

    public TitleEditor(ConfigSection messageSection) {
        super("Edit Title", messageSection);
        section.set("type", "title");
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
                .addLeftClickLore("To set the title")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter title in chat")
                            .withInputCollector(input -> {
                                section.set("title", input);
                                view.open();
                            })
                            .collect();
                });

        final ButtonBuilder subtitleButton = Button.builder()
                .withType(Material.SPRUCE_SIGN)
                .withDisplay("&f&lSubtitle")
                .enchanted(true)
                .addLore("")
                .addLore(section.getString("subtitle", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLeftClickLore("To set the subtitle")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter subtitle in chat")
                            .withInputCollector(input -> {
                                section.set("subtitle", input);
                                view.open();
                            })
                            .collect();
                });

        final Integer fadeIn = section.getInteger("fade-in").orElse(null);
        final ButtonBuilder fadeInButton = Button.builder()
                .withType(Material.ANVIL)
                .withDisplay("&f&lFade In")
                .enchanted(true)
                .addLore("")
                .addLore(fadeIn != null ? (fadeIn + " ticks") : "none")
                .addLore("")
                .addLeftClickLore("To set title fade-in amount")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Integer.class)
                            .withDescription("Enter fade-in amount in chat")
                            .withInputCollector(input -> {
                                section.set("fade-in", input);
                                view.open();
                            })
                            .collect();
                });

        final Integer stay = section.getInteger("stay").orElse(null);
        final ButtonBuilder stayButton = Button.builder()
                .withType(Material.WHITE_BED)
                .withDisplay("&f&lStay")
                .enchanted(true)
                .addLore("")
                .addLore(stay != null ? (stay + " ticks") : "none")
                .addLore("")
                .addLeftClickLore("To set title stay amount")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Integer.class)
                            .withDescription("Enter stay amount in chat")
                            .withInputCollector(input -> {
                                section.set("stay", input);
                                view.open();
                            })
                            .collect();
                });

        final Integer fadeOut = section.getInteger("fade-out").orElse(null);
        final ButtonBuilder fadeOutButton = Button.builder()
                .withType(Material.DAMAGED_ANVIL)
                .withDisplay("&f&lFade out")
                .enchanted(true)
                .addLore("")
                .addLore(fadeOut != null ? (fadeOut + " ticks") : "none")
                .addLore("")
                .addLeftClickLore("To set title fade out")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput(Integer.class)
                            .withDescription("Enter fade-out amount in chat")
                            .withInputCollector(input -> {
                                section.set("fade-out", input);
                                view.open();
                            })
                            .collect();
                });

        buttons[11] = titleButton.buildButton();
        buttons[20] = subtitleButton.buildButton();
        buttons[19] = fadeInButton.buildButton();
        buttons[29] = stayButton.buildButton();
        buttons[21] = fadeOutButton.buildButton();

        return buttons;
    }


}
