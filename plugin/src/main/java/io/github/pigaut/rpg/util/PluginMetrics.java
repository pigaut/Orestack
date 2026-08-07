package io.github.pigaut.rpg.util;

import io.github.pigaut.rpg.plugin.*;
import org.bstats.*;
import org.bstats.charts.*;
import org.bstats.json.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.*;

public class PluginMetrics {

    private final EnhancedPlugin plugin;
    private final MetricsBase metricsBase;

    public PluginMetrics(EnhancedPlugin plugin, int serviceId, boolean enabled) {
        this.plugin = plugin;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", true);
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", false);
            config.addDefault("logSentData", false);
            config.addDefault("logResponseStatusText", false);
            config.options().header("bStats (https://bStats.org) collects some basic information for plugin authors, like how\nmany people use their plugin and their total player count. It's recommended to keep bStats\nenabled, but if you're not comfortable with this, you can turn this setting off. There is no\nperformance penalty associated with having metrics enabled, and data sent to bStats is fully\nanonymous.").copyDefaults(true);

            try {
                config.save(configFile);
            } catch (IOException var11) {
            }
        }

        String serverUUID = config.getString("serverUuid");
        boolean logErrors = config.getBoolean("logFailedRequests", false);
        boolean logSentData = config.getBoolean("logSentData", false);
        boolean logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        Consumer<JsonObjectBuilder> var10007 = this::appendPlatformData;
        Consumer<JsonObjectBuilder> var10008 = this::appendServiceData;
        Consumer<Runnable> var10009 = (submitDataTask) -> {
            plugin.getScheduler().runTask(submitDataTask);
        };
        Objects.requireNonNull(plugin);

        this.metricsBase = new MetricsBase("bukkit", serverUUID, serviceId, enabled, var10007, var10008, var10009, plugin::isEnabled, (message, error) -> {
            this.plugin.getLogger().log(Level.WARNING, message, error);
        }, (message) -> {
            this.plugin.getLogger().log(Level.INFO, message);
        }, logErrors, logSentData, logResponseStatusText);
    }

    public void shutdown() {
        this.metricsBase.shutdown();
    }

    public void addCustomChart(CustomChart chart) {
        this.metricsBase.addCustomChart(chart);
    }

    private void appendPlatformData(JsonObjectBuilder builder) {
        builder.appendField("playerAmount", this.getPlayerAmount());
        builder.appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
        builder.appendField("bukkitVersion", Bukkit.getVersion());
        builder.appendField("bukkitName", Bukkit.getName());
        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", this.plugin.getDescription().getVersion());
    }

    private int getPlayerAmount() {
        try {
            Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");
            return onlinePlayersMethod.getReturnType().equals(Collection.class) ? ((Collection)onlinePlayersMethod.invoke(Bukkit.getServer())).size() : ((Player[])onlinePlayersMethod.invoke(Bukkit.getServer())).length;
        } catch (Exception var2) {
            return Bukkit.getOnlinePlayers().size();
        }
    }

}
