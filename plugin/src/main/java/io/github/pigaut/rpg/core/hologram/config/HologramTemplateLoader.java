package io.github.pigaut.rpg.core.hologram.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.hook.decentholograms.*;
import io.github.pigaut.rpg.hook.fancyholograms.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.hook.decentholograms.*;
import io.github.pigaut.rpg.hook.fancyholograms.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class HologramTemplateLoader implements ConfigLoader<HologramTemplate> {

    private final EnhancedPlugin plugin;

    public HologramTemplateLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid hologram";
    }

    @Override
    public @NotNull HologramTemplate loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new MultiHologramTemplate(sequence.getAllRequired(HologramTemplate.class));
    }

    @Override
    public @NotNull HologramTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        HologramTemplate hologramTemplate = null;

        HologramProvider preferredHologramProvider = plugin.getSettings().getPreferredHologramProvider();
        if (preferredHologramProvider.isAvailable()) {
            switch (preferredHologramProvider) {
                case FANCY_HOLOGRAMS -> hologramTemplate = loadFancyHologramTemplate(section);
                case DECENT_HOLOGRAMS -> hologramTemplate = loadDecentHologramTemplate(section);
            }
        }
        else {
            findProvider:
            for (HologramProvider hologramProvider : HologramProvider.values()) {
                if (hologramProvider == preferredHologramProvider || !hologramProvider.isAvailable()) {
                    continue;
                }

                switch (hologramProvider) {
                    case FANCY_HOLOGRAMS -> {
                        hologramTemplate = loadFancyHologramTemplate(section);
                        break findProvider;
                    }
                    case DECENT_HOLOGRAMS -> {
                        hologramTemplate = loadDecentHologramTemplate(section);
                        break findProvider;
                    }
                }
            }
        }

        if (hologramTemplate == null) {
            hologramTemplate = new FallbackHologramTemplate();
        }

        double offsetX = section.getDouble("offset.x").withDefault(0d);
        double offsetY = section.getDouble("offset.y").orElse(0d);
        double offsetZ = section.getDouble("offset.z").orElse(0d);
        if (offsetX != 0 || offsetY != 0 || offsetZ != 0) {
            return new OffsetHologramTemplate(hologramTemplate, offsetX, offsetY, offsetZ);
        }

        return hologramTemplate;
    }

    private @NotNull HologramTemplate loadFancyHologramTemplate(@NotNull ConfigSection section) throws InvalidConfigException {
        HologramStyle hologramStyle = section.get("style", HologramStyle.class)
                .withDefault(plugin.getSettings().getDefaultHologramStyle());
        section.getInteger("view-distance|view|distance")
                .ifValid(hologramStyle::setViewDistance);
        section.get("update|update-interval", Delay.class)
                .mapIfValid(Delay::toTicks)
                .ifValid(hologramStyle::setUpdateInterval);

        if (section.isSet("line|text")) {
            String line = section.getRequiredString("line|text");
            return new FancySingleLineHologramTemplate(plugin, hologramStyle, line);
        }

        else if (section.isSet("lines")) {
            List<String> lines = section.getStringList("lines")
                    .require(Requirements.minSize(2), "Multi-line hologram must have at least 2 lines")
                    .orThrow();

            return new FancyMultiLineHologramTemplate(plugin, hologramStyle, lines);
        }

        else if (section.isSet("frames")) {
            List<String> frames = section.getStringList("frames")
                    .require(Requirements.minSize(2), "Animated hologram must have at least 2 frames")
                    .orThrow();

            return new FancyAnimatedHologramTemplate(plugin, hologramStyle, frames);
        }

        else if (section.isSet("item")) {
            ItemStack item = section.getRequired("item", ItemStack.class);
            return new FancyItemHologramTemplate(hologramStyle, item);
        }

        else if (section.isSet("block")) {
            Material material = section.get("block", Material.class)
                    .requireOrThrow(Material::isBlock, "Material is not a block");
            return new FancyBlockHologramTemplate(hologramStyle, material);
        }

        else {
            throw new InvalidConfigException(section, "Unknown hologram type. Expected one of: line, lines, frames, item, block");
        }
    }

    private @NotNull HologramTemplate loadDecentHologramTemplate(@NotNull ConfigSection section) throws InvalidConfigException {
        int viewDistance = section.getInteger("view-distance|view|distance")
                .withDefault(plugin.getSettings().getHologramViewDistance());

        if (section.isSet("line|text")) {
            String line = section.getRequiredString("line|text");
            int update = section.get("update", Delay.class)
                    .mapIfValid(Delay::toTicks)
                    .withDefault(0);
            return new DecentSingleLineHologramTemplate(plugin, line, update, viewDistance);
        }

        else if (section.isSet("lines")) {
            List<String> lines = section.getStringList("lines")
                    .require(Requirements.minSize(2), "Multi-line hologram must have at least 2 lines")
                    .orThrow();

            int update = section.get("update", Delay.class)
                    .mapIfValid(Delay::toTicks)
                    .withDefault(0);

            return new DecentMultiLineHologramTemplate(plugin, lines, update, viewDistance);
        }

        else if (section.isSet("frames")) {
            List<String> frames = section.getStringList("frames")
                    .require(Requirements.minSize(2), "Animated hologram must have at least 2 frames")
                    .orThrow();

            int update = section.get("update", Delay.class)
                    .mapIfValid(Delay::toTicks)
                    .require(Requirements.positive())
                    .withDefault(3);

            return new DecentAnimatedHologramTemplate(plugin, frames, update, viewDistance);
        }

        else if (section.isSet("item")) {
            ItemStack item = section.getRequired("item", ItemStack.class);
            return new DecentItemHologramTemplate(plugin, item, viewDistance);
        }

        else if (section.isSet("block")) {
            Material material = section.get("block", Material.class)
                    .require(Material::isBlock, "Material is not a block")
                    .orThrow();
            return new DecentBlockHologramTemplate(plugin, material, viewDistance);
        }

        else {
            throw new InvalidConfigException(section, "Unknown hologram type. Expected one of: line, lines, frames, item, block");
        }
    }

}
