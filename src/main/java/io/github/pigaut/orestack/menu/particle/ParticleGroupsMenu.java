package io.github.pigaut.orestack.menu.particle;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class ParticleGroupsMenu extends GenericGroupsMenu {

    public ParticleGroupsMenu(OrestackPlugin plugin) {
        super("Particle Effect Groups");

        for (String group : plugin.getParticles().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&d&l" + StringFormatter.toTitleCase(group))
                    .addLore("&7Left-Click: &fView all")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new ParticlesMenu(group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
