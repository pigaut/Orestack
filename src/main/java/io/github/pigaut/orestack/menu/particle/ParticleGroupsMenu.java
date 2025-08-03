package io.github.pigaut.orestack.menu.particle;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

import java.util.*;

public class ParticleGroupsMenu extends FramedSelectionMenu {

    private final OrestackPlugin plugin;

    public ParticleGroupsMenu(OrestackPlugin plugin) {
        super("Particle Effect Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getParticles().getAllGroups().stream()
                .map(group -> Button.builder()
                        .withType(Material.CHEST)
                        .withDisplay("&d&l" + StringFormatter.toTitleCase(group))
                        .addLore("")
                        .addLore("&eLeft-Click: &fView all particle effects")
                        .onLeftClick((view, player, event) -> player.openMenu(new ParticlesMenu(plugin, group)))
                        .buildButton())
                .toList();
    }

}
