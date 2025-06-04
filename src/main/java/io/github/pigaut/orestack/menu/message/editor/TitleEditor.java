package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.formatter.*;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class TitleEditor extends GenericMessageEditor {

    public TitleEditor(EnhancedPlugin plugin, ConfigSection parent, String name) {
        super(plugin, "Edit Title", parent, name);
        parent.getSectionOrCreate(name).set("type", "title");
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

        titleButton.addLore("")
                .addLore(ChatColor.WHITE + section.getOptionalString("title", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fSet the title");

        final ButtonBuilder subtitleButton = Button.builder()
                .withType(Material.SPRUCE_SIGN)
                .withDisplay("&f&lSubtitle")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter subtitle in chat")
                            .onInput(input -> {
                                section.set("subtitle", input);
                                player.setOpenView(view);
                            })
                            .collect();
                });

        titleButton.addLore("")
                .addLore(ChatColor.WHITE + section.getOptionalString("subtitle", StringColor.FORMATTER).orElse("none"))
                .addLore("")
                .addLore("&eLeft-Click: &fSet the subtitle");

        final ButtonBuilder fadeInButton = Button.builder()
                .withType(Material.ANVIL)
                .withDisplay("&f&lFade In")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter fade-in amount in chat")
                            .onInput(input -> {
                                final Integer fadeIn = Deserializers.getInteger(input);
                                if (fadeIn != null) {
                                    section.set("fade-in", fadeIn);
                                }
                                player.setOpenView(view);
                            })
                            .collect();
                });

        final int fadeIn = section.getOptionalInteger("fade-in").orElse(0);
        fadeInButton.addLore("")
                .addLore("&f" + fadeIn + " ticks")
                .addLore("")
                .addLore("&eLeft-Click: &fSet title fade in");

        final ButtonBuilder stayButton = Button.builder()
                .withType(Material.WHITE_BED)
                .withDisplay("&f&lStay")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter stay amount in chat")
                            .onInput(input -> {
                                final Integer stay = Deserializers.getInteger(input);
                                if (stay != null) {
                                    section.set("stay", stay);
                                }
                                player.setOpenView(view);
                            })
                            .collect();
                });

        final int stay = section.getOptionalInteger("stay").orElse(0);
        stayButton.addLore("")
                .addLore("&f" + stay + " ticks")
                .addLore("")
                .addLore("&eLeft-Click: &fSet title stay");

        final ButtonBuilder fadeOutButton = Button.builder()
                .withType(Material.DAMAGED_ANVIL)
                .withDisplay("&f&lFade out")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    player.createChatInput()
                            .withDescription("Enter fade-out amount in chat")
                            .onInput(input -> {
                                final Integer fadeOut = Deserializers.getInteger(input);
                                if (fadeOut != null) {
                                    section.set("fade-out", fadeOut);
                                }
                                player.setOpenView(view);
                            })
                            .collect();
                });

        final int fadeOut = section.getOptionalInteger("fade-out").orElse(0);
        fadeOutButton.addLore("")
                .addLore("&f" + fadeOut + " ticks")
                .addLore("")
                .addLore("&eLeft-Click: &fSet title fade out");

        buttons[11] = titleButton.buildButton();
        buttons[20] = subtitleButton.buildButton();
        buttons[19] = fadeInButton.buildButton();
        buttons[29] = stayButton.buildButton();
        buttons[21] = fadeOutButton.buildButton();

        return buttons;
    }


}
