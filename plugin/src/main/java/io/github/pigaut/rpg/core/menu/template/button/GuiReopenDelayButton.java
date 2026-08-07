package io.github.pigaut.rpg.core.menu.template.button;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;

public class GuiReopenDelayButton extends SimpleButton {

    private final Settings settings;

    public GuiReopenDelayButton(EnhancedPlugin plugin) {
        super(IconBuilder.of(Material.LEVER)
                .enchanted(true)
                .name("&aView Time: &f" + plugin.getSettings().guiReopenDelay + " ticks" +
                        (plugin.getSettings().guiReopenDelay == 200 ? " &c(max)" : plugin.getSettings().guiReopenDelay == 5 ? " &c(min)" : ""))
                .addLines("The time delay after which the menu reopens")
                .addLines("when viewing messages, particles, etc..")
                .addLines("")
                .addLeftClickLine("To increment by 5 ticks")
                .addRightClickLine("To decrease by 5 ticks")
                .addShiftLeftClickLine("To increment by 20 ticks")
                .addShiftRightClickLine("To decrease by 20 ticks")
                .buildIcon());
        this.settings = plugin.getSettings();
    }

    @Override
    public boolean isUpdateOnClick() {
        return true;
    }

    @Override
    public void onLeftClick(MenuView view, PlayerState player) {
        settings.guiReopenDelay = Math.min(settings.guiReopenDelay + 5, 200);
    }

    @Override
    public void onRightClick(MenuView view, PlayerState player) {
        settings.guiReopenDelay = Math.max(settings.guiReopenDelay - 5, 5);
    }

    @Override
    public void onShiftLeftClick(MenuView view, PlayerState player) {
        settings.guiReopenDelay = Math.min(settings.guiReopenDelay + 20, 200);
    }

    @Override
    public void onShiftRightClick(MenuView view, PlayerState player) {
        settings.guiReopenDelay = Math.max(settings.guiReopenDelay - 20, 5);
    }

}
