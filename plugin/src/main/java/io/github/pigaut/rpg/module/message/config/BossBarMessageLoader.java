package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.message.type.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.boss.*;
import org.jetbrains.annotations.*;

public class BossBarMessageLoader implements ConfigLoader<BossBarMessage> {

    private final EnhancedPlugin plugin;

    public BossBarMessageLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid boss-bar message";
    }

    @Override
    public @NotNull BossBarMessage loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byMessageFile(section.getRoot().getFile());
        return new BossBarMessage(plugin, name, group,
                section.getRequiredString("bossbar|boss-bar"),
                section.get("style", BarStyle.class).withDefault(BarStyle.SOLID),
                section.get("color", BarColor.class).withDefault(BarColor.RED),
                section.get("duration", Delay.class).mapIfValid(Delay::toTicks).withDefault(100),
                section.getDoubleList("progress").orEmpty()
        );
    }
}
