package io.github.pigaut.orestack.menu.hologram;

import io.github.pigaut.orestack.menu.hologram.editor.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class HologramCreationMenu extends FramedMenu {

    protected ConfigSection config;

    public HologramCreationMenu(ConfigSection config) {
        this(config, "Hologram Creation");
    }

    public HologramCreationMenu(ConfigSection config, String title) {
        super(title, MenuSize.SMALL);
        this.config = config;
    }

    @Override
    public boolean backOnClose() {
        return true;
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[11] = Button.builder()
                .withType(Material.PAINTING)
                .withDisplay("&fFixed Hologram")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new fixed hologram")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    ConfigSection hologramSection = config.getSectionOrCreate("hologram");
                    player.openMenu(new FixedHologramEditor(hologramSection), view.getPreviousView());
                })
                .buildButton();

        buttons[13] = Button.builder()
                .withType(Material.ITEM_FRAME)
                .withDisplay("&fAnimated Hologram")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new animated hologram")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    ConfigSection hologramSection = config.getSectionOrCreate("hologram");
                    player.openMenu(new AnimatedHologramEditor(hologramSection), view.getPreviousView());
                })
                .buildButton();

        buttons[15] = Button.builder()
                .withType(Material.BOOKSHELF)
                .withDisplay("&fMulti-Line Hologram")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new multi line hologram")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    ConfigSequence hologramSequence = config.getSequenceOrCreate("hologram");
                    player.openMenu(new MultiHologramEditor(hologramSequence), view.getPreviousView());
                })
                .buildButton();

        return buttons;
    }

}
