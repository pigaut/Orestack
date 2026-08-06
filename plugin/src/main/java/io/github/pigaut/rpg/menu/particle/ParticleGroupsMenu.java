package io.github.pigaut.rpg.menu.particle;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ParticleGroupsMenu extends FramedSelectionMenu {

    private final RpgMakerPlugin plugin;

    public ParticleGroupsMenu(RpgMakerPlugin plugin) {
        super("Particle Effect Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        return plugin.getParticles().getAllGroups().stream()
                .map(group -> Button.builder()
                        .type(Material.CHEST)
                        .name("&d&l" + CaseFormatter.toTitleCase(group))
                        .addEmptyLine()
                        .addLine("&eLeft-Click: &fView all particle effects")
                        .onLeftClick((view, player) -> player.openMenu(new ParticlesMenu(plugin, group)))
                        .buildButton())
                .toList();
    }

}
