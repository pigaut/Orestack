package io.github.pigaut.rpg.config.misc;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class OffsetLoader implements ConfigLoader.Line<Offset> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid offset";
    }

    @Override
    public @NotNull Offset loadFromLine(ConfigLine line) throws InvalidConfigException {
        int x = line.getInteger("offsetX|offX|x").withDefault(0);
        int y = line.getInteger("offsetY|offY|y").withDefault(0);
        int z = line.getInteger("offsetZ|offZ|z").withDefault(0);
        return new Offset(x, y, z);
    }

    @Override
    public @NotNull Offset loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        int x = section.getInteger("offset.x").withDefault(0);
        int y = section.getInteger("offset.y").withDefault(0);
        int z = section.getInteger("offset.z").withDefault(0);
        return new Offset(x, y, z);
    }

}
