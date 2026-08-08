package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.message.type.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ActionBarMessageLoader implements ConfigLoader<ActionBarMessage> {

    private final EnhancedPlugin plugin;

    public ActionBarMessageLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid action-bar message";
    }

    @Override
    public @NotNull ActionBarMessage loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byMessageFile(section.getRoot().getFile());
        String actionbar = section.getRequiredString("actionbar|action-bar", ColorUtil.FORMATTER);
        BarAlignment statusBarAlign = section.get("align", BarAlignment.class).withDefault(null);
        return new ActionBarMessage(plugin, name, group, actionbar, statusBarAlign);
    }

}
