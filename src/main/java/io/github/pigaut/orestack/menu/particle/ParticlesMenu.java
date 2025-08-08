package io.github.pigaut.orestack.menu.particle;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

import java.util.*;

public class ParticlesMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;

    public ParticlesMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Particle Effects", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getParticles().getAll(group).stream()
                .map(particle -> Button.builder()
                        .withType(particle.getIcon().getType())
                        .withDisplay("&d&o" + StringFormatter.toTitleCase(particle.getName()))
                        .addLore("")
                        .addLore("&eLeft-Click: &fShow-me particle")
                        .onLeftClick((view, player, event) -> {
                            view.close();
                            player.performCommand("orestack particle show-me " + particle.getName());
                            player.sendMessage(ChatColor.RED + "The menu will reopen in 3 seconds...");
                            plugin.getScheduler().runTaskLater(60L, view::open);
                        })
                        .buildButton())
                .toList();
    }

    @Override
    public Button getToolbarButton4() {
        return Buttons.MAIN_MENU;
    }

}
