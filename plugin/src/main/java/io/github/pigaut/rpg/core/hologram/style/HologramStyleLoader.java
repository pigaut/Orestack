package io.github.pigaut.rpg.core.hologram.style;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.line.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class HologramStyleLoader implements ConfigLoader<HologramStyle> {

    private final EnhancedPlugin plugin;

    public HologramStyleLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid hologram style";
    }

    @Override
    public @NotNull HologramStyle loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String styleName = scalar.toString();

        HologramStyle hologramStyle = plugin.getSettings().getHologramStyle(styleName);
        if (hologramStyle == null) {
            throw new InvalidConfigException(scalar, "Could not find hologram style with name: " + styleName);
        }

        return hologramStyle;
    }

    @Override
    public @NotNull HologramStyle loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        int viewDistance = section.getInteger("view-distance|view")
                .require(Requirements.between(3, 100))
                .withDefault(plugin.getSettings().getHologramViewDistance());

        int updateInterval = section.get("update|update-interval", Delay.class)
                .map(Delay::toTicks)
                .withDefault(0);

        boolean seeThrough = section.getBoolean("see-through|see-through-blocks")
                .withDefault(false);

        Display.Billboard billboard = section.get("billboard|board", Display.Billboard.class)
                .withDefault(Display.Billboard.VERTICAL);

        TextDisplay.TextAlignment alignment = section.get("text-alignment|alignment", TextDisplay.TextAlignment.class)
                .withDefault(TextDisplay.TextAlignment.CENTER);

        Display.Brightness brightness = null;

        ConfigLine lightLine = section.getLine("brightness|light", LineStyle.COMMA).withDefault(null);
        if (lightLine != null) {
            if (!lightLine.equalsIgnoreCase("none") && !lightLine.equalsIgnoreCase("false")) {
                Integer light = lightLine.getInteger(0)
                        .require(Requirements.between(0, 15))
                        .withDefault(null);

                if (light != null) {
                    brightness = new Display.Brightness(light, 0);
                }
                else {
                    Integer blockLight = lightLine.getInteger("block")
                            .require(Requirements.between(0, 15))
                            .withDefault(0);

                    Integer skyLight = lightLine.getInteger("sky")
                            .require(Requirements.between(0, 15))
                            .withDefault(0);

                    brightness = new Display.Brightness(blockLight, skyLight);
                }
            }
        }

        Color background = section.get("background|background-color", Color.class)
                .withDefault(null);

        float scaleX = 1, scaleY = 1, scaleZ = 1;

        ConfigLine scaleLine = section.getLine("scale", LineStyle.COMMA).withDefault(null);
        if (scaleLine != null) {
            float uniformScale = scaleLine.getFloat(0).withDefault(1f);
            scaleX = scaleLine.getFloat("x").withDefault(uniformScale);
            scaleY = scaleLine.getFloat("y").withDefault(uniformScale);
            scaleZ = scaleLine.getFloat("z").withDefault(uniformScale);
        }

        boolean shadow = true;
        float shadowRadius = 1.0f;
        float shadowStrength = 1.0f;

        ConfigLine shadowLine = section.getLine("shadow", LineStyle.COMMA).withDefault(null);
        if (shadowLine != null) {
            if (shadowLine.equalsIgnoreCase("none") || shadowLine.equalsIgnoreCase("false")) {
                shadow = false;
            }
            else {
                shadowRadius = shadowLine.getFloat("radius").withDefault(1.0f);
                shadowStrength = shadowLine.getFloat("strength").withDefault(1.0f);
            }
        }

        return new HologramStyle(viewDistance, updateInterval, seeThrough, shadow, billboard, alignment,
                brightness, background, scaleX, scaleY, scaleZ, shadowStrength, shadowRadius);
    }

}
