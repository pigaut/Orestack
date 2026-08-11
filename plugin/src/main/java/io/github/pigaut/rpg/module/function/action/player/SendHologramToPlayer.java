package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.type.*;
import io.github.pigaut.rpg.hook.decentholograms.*;
import io.github.pigaut.rpg.hook.fancyholograms.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SendHologramToPlayer implements Action {

    private final Message hologram;

    private SendHologramToPlayer(EnhancedPlugin plugin, HologramTemplate hologram, Delay duration,
                                 Double offsetX, Double offsetY, Double offsetZ,
                                 Double radiusX, Double radiusY, Double radiusZ) {
        hologram = new OffsetHologramTemplate(hologram, offsetX, offsetY, offsetZ);
        this.hologram = new HologramMessage(plugin, hologram, duration, radiusX, radiusY, radiusZ);
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            hologram.send(player, context);
        }
    }

    public static @NotNull Action create(EnhancedPlugin plugin, String text, Delay duration,
                                         Double offsetX, Double offsetY, Double offsetZ,
                                         Double radiusX, Double radiusY, Double radiusZ) {
        HologramTemplate hologramTemplate;
        if (Server.isPluginEnabled("FancyHolograms")) {
            hologramTemplate = new FancySingleLineHologramTemplate(plugin, plugin.getSettings().getDefaultHologramStyle(), text);
        }
        else if (Server.isPluginEnabled("DecentHolograms")) {
            hologramTemplate = new DecentSingleLineHologramTemplate(plugin, text);
        }
        else {
            hologramTemplate = new FallbackHologramTemplate();
        }

        return new SendHologramToPlayer(plugin, hologramTemplate, duration, offsetX, offsetY, offsetZ,
                radiusX, radiusY, radiusZ);
    }

}
