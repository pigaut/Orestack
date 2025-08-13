package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class TitleEditor extends GenericMessageEditor {

    public TitleEditor(ConfigSection messageSection) {
        super("Edit Title", messageSection);
        this.messageSection.set("type", "title");
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
                })
                .addLore("")
                .addLore(messageSection.getOptionalString("title", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set the title");

        final ButtonBuilder subtitleButton = Button.builder()
                .withType(Material.SPRUCE_SIGN)
                .withDisplay("&f&lSubtitle")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter subtitle in chat")
                            .collectInput(input -> {
                                messageSection.set("subtitle", input);
                                view.open();
                            })
                            .beginCollection();
                })
                .addLore("")
                .addLore(messageSection.getOptionalString("subtitle", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fTo set the subtitle");

        final Integer fadeIn = messageSection.getOptionalInteger("fade-in").orElse(null);
        final ButtonBuilder fadeInButton = Button.builder()
                .withType(Material.ANVIL)
                .withDisplay("&f&lFade In")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter fade-in amount in chat")
                            .collectInput(input -> {
                                final Integer amount = Deserializers.getInteger(input);
                                if (amount != null) {
                                    messageSection.set("fade-in", amount);
                                }
                                view.open();
                            })
                            .beginCollection();
                })
                .addLore("")
                .addLore(fadeIn != null ? (fadeIn + " ticks") : "none")
                .addLore("")
                .addLore("&eLeft-Click: &fTo set title fade-in amount");

        final Integer stay = messageSection.getOptionalInteger("stay").orElse(null);
        final ButtonBuilder stayButton = Button.builder()
                .withType(Material.WHITE_BED)
                .withDisplay("&f&lStay")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter stay amount in chat")
                            .collectInput(input -> {
                                final Integer amount = Deserializers.getInteger(input);
                                if (amount != null) {
                                    messageSection.set("stay", amount);
                                }
                                view.open();
                            })
                            .beginCollection();
                })
                .addLore("")
                .addLore(stay != null ? (stay + " ticks") : "none")
                .addLore("")
                .addLore("&eLeft-Click: &fTo set title stay amount");

        final Integer fadeOut = messageSection.getOptionalInteger("fade-out").orElse(null);
        final ButtonBuilder fadeOutButton = Button.builder()
                .withType(Material.DAMAGED_ANVIL)
                .withDisplay("&f&lFade out")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter fade-out amount in chat")
                            .collectInput(input -> {
                                final Integer amount = Deserializers.getInteger(input);
                                if (amount != null) {
                                    messageSection.set("fade-out", amount);
                                }
                                view.open();
                            })
                            .beginCollection();
                })
                .addLore("")
                .addLore(fadeOut != null ? (fadeOut + " ticks") : "none")
                .addLore("")
                .addLore("&eLeft-Click: &fTo set title fade out");

        buttons[11] = titleButton.buildButton();
        buttons[20] = subtitleButton.buildButton();
        buttons[19] = fadeInButton.buildButton();
        buttons[29] = stayButton.buildButton();
        buttons[21] = fadeOutButton.buildButton();

        return buttons;
    }


}
