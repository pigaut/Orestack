package io.github.pigaut.orestack.menu.hologram;

import io.github.pigaut.orestack.menu.hologram.editor.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class HologramCreationMenu extends FramedMenu {

    private final ConfigSection hologramSection;
    private final boolean multiHologramButton;

    public HologramCreationMenu(ConfigSection hologramSection, boolean multiHologramButton) {
        super("Hologram Creation", MenuSize.SMALL);
        this.hologramSection = hologramSection;
        this.multiHologramButton = multiHologramButton;
    }

    @Override
    public boolean backOnClose() {
        return true;
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[10] = Button.builder()
                .withType(Material.PAINTING)
                .withDisplay("&fFixed Hologram")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new fixed hologram")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.openMenu(new StaticHologramEditor(hologramSection), view.getPreviousView());
                })
                .buildButton();

        buttons[11] = Button.builder()
                .withType(Material.ITEM_FRAME)
                .withDisplay("&fAnimated Hologram")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new animated hologram")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.openMenu(new AnimatedHologramEditor(hologramSection), view.getPreviousView());
                })
                .buildButton();

        buttons[12] = Button.builder()
                .withType(Material.IRON_PICKAXE)
                .withDisplay("&fItem Hologram")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new item hologram")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.openMenu(new ItemHologramEditor(hologramSection), view.getPreviousView());
                })
                .buildButton();

        buttons[14] = Button.builder()
                .withType(Material.GRASS_BLOCK)
                .withDisplay("&fBlock Hologram")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new block hologram")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.openMenu(new BlockHologramEditor(hologramSection), view.getPreviousView());
                })
                .buildButton();

        if (multiHologramButton) {
            buttons[15] = Button.builder()
                    .withType(Material.BOOKSHELF)
                    .withDisplay("&fMulti-Line Hologram")
                    .addLore("")
                    .addLore("&eLeft-Click: &fCreate a new multi line hologram")
                    .enchanted(true)
                    .onLeftClick((view, player, event) -> {
                        final ConfigSequence hologramSequence = hologramSection.convertToSequence();
                        player.openMenu(new MultiHologramEditor(hologramSequence), view.getPreviousView());
                    })
                    .buildButton();
        }

        return buttons;
    }

}
