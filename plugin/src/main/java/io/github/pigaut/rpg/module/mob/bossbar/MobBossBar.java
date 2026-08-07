package io.github.pigaut.rpg.module.mob.bossbar;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class MobBossBar {

    private final EnhancedPlugin plugin;
    private final Context context;
    private final Mob mob;
    private final BossBar bossBar;
    private final String title;
    private final double range;
    private Task updateTask;

    public MobBossBar(@NotNull EnhancedPlugin plugin, @NotNull Mob mob,
                      @NotNull BossBar bossBar, @NotNull String title, double range) {
        this.plugin = plugin;
        this.context = Context.fromMob(plugin, mob);
        this.mob = mob;
        this.bossBar = bossBar;
        this.title = title;
        this.range = range;
    }

    public void update() {
        String parsedTitle = PlaceholderUtil.parseAll(context, title);
        bossBar.setTitle(parsedTitle);

        double healthPercentage = mob.getHealth() / mob.getMaxHealth();
        bossBar.setProgress(healthPercentage);
    }

    public void start() {
        updateTask = plugin.getScheduler().runTaskTimer(1, 10, () -> {
            if (!mob.isValid()) {
                stop();
                return;
            }

            update();

            Location center = mob.getLocation();
            double rangeSquared = range * range;

            Set<Player> inRange = center.getWorld().getPlayers().stream()
                    .filter(p -> p.getLocation().distanceSquared(center) <= rangeSquared)
                    .collect(Collectors.toSet());

            for (Player player : inRange) {
                if (!bossBar.getPlayers().contains(player)) {
                    bossBar.addPlayer(player);
                }
            }

            for (Player player : bossBar.getPlayers()) {
                if (!inRange.contains(player)) {
                    bossBar.removePlayer(player);
                }
            }
        });
    }

    public void stop() {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
        bossBar.removeAll();
    }

    public @NotNull BossBar getBossBar() {
        return bossBar;
    }

    public @NotNull Mob getMob() {
        return mob;
    }

}
