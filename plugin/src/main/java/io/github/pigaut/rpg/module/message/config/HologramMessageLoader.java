package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.module.message.type.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

public class HologramMessageLoader implements ConfigLoader<HologramMessage> {

    private final EnhancedPlugin plugin;

    public HologramMessageLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid hologram message";
    }

    @Override
    public @NotNull HologramMessage loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byMessageFile(section.getRoot().getFile());
        return new HologramMessage(plugin, name, group,
                section.getRequired("hologram", HologramTemplate.class),
                section.get("duration", Delay.class).withDefault(Delay.fromSeconds(2)),
                section.getDouble("range|radius.x").withDefault(null),
                section.getDouble("range|radius.y").withDefault(null),
                section.getDouble("range|radius.z").withDefault(null)
        );
    }
}
