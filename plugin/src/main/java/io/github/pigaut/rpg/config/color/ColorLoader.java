package io.github.pigaut.rpg.config.color;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ColorLoader implements ConfigLoader<Color> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid RGB color";
    }

    // styles:
    // color: red=5 green=4 blue=10  (aliases: r g b)
    // color: #RRGGBB
    // color: color_name

    @Override
    public @NotNull Color loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        return Color.fromRGB(scalar.toInteger().orThrow());
    }

    @Override
    public @NotNull Color loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        final int red = section.getInteger("red").withDefault(0);
        final int green = section.getInteger("green").withDefault(0);
        final int blue = section.getInteger("blue").withDefault(0);
        return Color.fromRGB(red, green, blue);
    }

}
