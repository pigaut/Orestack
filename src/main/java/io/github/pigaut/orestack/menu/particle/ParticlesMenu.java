package io.github.pigaut.orestack.menu.particle;

import io.github.pigaut.voxel.core.particle.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class ParticlesMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;

    public ParticlesMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Particle Effects", MenuSize.BIG);
        this.plugin = plugin;
        for (ParticleEffect particle : plugin.getParticles().getAll(group)) {
            final Button button = Button.builder()
                    .withType(particle.getIcon().getType())
                    .withDisplay("&d&o" + StringFormatter.toTitleCase(particle.getName()))
                    .addLore("")
                    .addLore("&7Left-Click: &fShow-me particle")
                    .onLeftClick((menuView, event) -> {
                        menuView.close();
                        final PlayerState viewer = menuView.getViewer();
                        viewer.performCommand("orestack particle show-me " + particle.getName());
                        viewer.sendMessage(ChatColor.RED + "The menu will reopen in 3 seconds...");
                        plugin.getScheduler().runTaskLater(60L, () -> {
                            viewer.setOpenView(menuView);
                        });
                    })
                    .buildButton();

            this.addEntry(button);
        }
    }

}
