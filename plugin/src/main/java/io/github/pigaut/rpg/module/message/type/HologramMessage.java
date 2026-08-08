package io.github.pigaut.rpg.module.message.type;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class HologramMessage extends GenericMessage {

    private final EnhancedPlugin plugin;
    private final HologramTemplate hologramTemplate;
    private final Delay duration;
    private final Double radiusX, radiusY, radiusZ;

    public HologramMessage(@NotNull EnhancedPlugin plugin, @NotNull HologramTemplate hologram, Delay duration,
                           @Nullable Double radiusX, @Nullable Double radiusY, @Nullable Double radiusZ) {
        this(plugin, UUID.randomUUID().toString(), null, hologram, duration, radiusX, radiusY, radiusZ);
    }

    public HologramMessage(@NotNull EnhancedPlugin plugin, String name, @Nullable String group,
                           @NotNull HologramTemplate hologramTemplate, Delay duration, @Nullable Double radiusX,
                           @Nullable Double radiusY, @Nullable Double radiusZ) {
        super(name, group);
        this.plugin = plugin;
        this.hologramTemplate = hologramTemplate;
        this.duration = duration;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
    }

    @Override
    public @NotNull MessageType getType() {
        return MessageType.HOLOGRAM;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return IconBuilder.of(Material.BEACON).buildIcon();
    }

    @Override
    public void send(@NotNull Player player, @NotNull Context context) {
        if (hologramTemplate == null) {
            player.sendMessage(ChatColor.RED + "Could not find any compatible hologram plugin installed.");
            return;
        }

        Location location = player.getLocation();
        location.add(player.getFacing().getDirection().multiply(2));

        if (radiusX != null && radiusX > 0) {
            location.add(ThreadLocalRandom.current().nextDouble(-radiusX, radiusX), 0, 0);
        }

        if (radiusY != null && radiusY > 0) {
            location.add(0, ThreadLocalRandom.current().nextDouble(-radiusY, radiusY), 0);
        }

        if (radiusZ != null && radiusZ > 0) {
            location.add(0, 0, ThreadLocalRandom.current().nextDouble(-radiusZ, radiusZ));
        }

        Hologram hologram = hologramTemplate.spawn(location, context);

        if (hologram != null) {
            plugin.getScheduler().runTaskLater(duration.toTicks(), hologram::remove);
        }
    }

}
