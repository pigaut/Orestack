package io.github.pigaut.rpg.module.mob.bossbar;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.jetbrains.annotations.*;

public class MobBossBarTemplate {

    private final EnhancedPlugin plugin;
    private final String title;
    private final double range;
    private final BarColor color;
    private final BarStyle style;
    private final boolean createFog;
    private final boolean darkenSky;
    private final boolean playMusic;

    public MobBossBarTemplate(@NotNull EnhancedPlugin plugin, @NotNull String title, double range,
                              @NotNull BarColor color, @NotNull BarStyle style,
                              boolean createFog, boolean darkenSky, boolean playMusic) {
        this.plugin = plugin;
        this.title = title;
        this.range = range;
        this.color = color;
        this.style = style;
        this.createFog = createFog;
        this.darkenSky = darkenSky;
        this.playMusic = playMusic;
    }

    public @NotNull MobBossBar create(@NotNull Mob mob) {
        Context context = Context.fromMob(plugin, mob);
        String parsedTitle = PlaceholderUtil.parseAll(context, title);

        BossBar bossBar = Bukkit.createBossBar(parsedTitle, color, style);

        if (createFog) {
            bossBar.addFlag(BarFlag.CREATE_FOG);
        }

        if (darkenSky) {
            bossBar.addFlag(BarFlag.DARKEN_SKY);
        }

        if (playMusic) {
            bossBar.addFlag(BarFlag.PLAY_BOSS_MUSIC);
        }

        return new MobBossBar(plugin, mob, bossBar, title, range);
    }

}