package io.github.pigaut.rpg.module.mob.bossbar.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.boss.*;
import org.jetbrains.annotations.*;

public class MobBossBarTemplateLoader implements ConfigLoader<MobBossBarTemplate> {

    private final EnhancedPlugin plugin;

    public MobBossBarTemplateLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid mob boss bar";
    }

    @Override
    public @NotNull MobBossBarTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String title = section.getRequiredString("title", ColorUtil.FORMATTER);
        double range = section.getDouble("range").withDefault(50.0);
        BarColor color = section.get("color", BarColor.class).withDefault(BarColor.RED);
        BarStyle style = section.get("style", BarStyle.class).withDefault(BarStyle.SOLID);

        boolean createFog = section.getBoolean("create-fog").withDefault(false);
        boolean darkenSky = section.getBoolean("darken-sky").withDefault(false);
        boolean playMusic = section.getBoolean("play-music").withDefault(false);

        return new MobBossBarTemplate(plugin, title, range, color, style, createFog, darkenSky, playMusic);
    }

}
