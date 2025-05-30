package io.github.pigaut.orestack.menu.particle;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class ParticleGroupsMenu extends FramedSelectionMenu {

    public ParticleGroupsMenu(OrestackPlugin plugin) {
        super("Particle Effect Groups", MenuSize.BIG);

        for (String group : plugin.getParticles().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&d&l" + StringFormatter.toTitleCase(group))
                    .addLore("")
                    .addLore("&7Left-Click: &fView all")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new ParticlesMenu(plugin, group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
