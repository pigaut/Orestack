package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.message.type.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class TitleMessageLoader implements ConfigLoader<TitleMessage> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid title message";
    }

    @Override
    public @NotNull TitleMessage loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byMessageFile(section.getRoot().getFile());
        return new TitleMessage(name, group,
                section.getRequiredString("title", ColorUtil.FORMATTER),
                section.getString("subtitle", ColorUtil.FORMATTER).withDefault(""),
                section.getInteger("fade-in").withDefault(10),
                section.getInteger("stay").withDefault(70),
                section.getInteger("fade-out").withDefault(20)
        );
    }
}
