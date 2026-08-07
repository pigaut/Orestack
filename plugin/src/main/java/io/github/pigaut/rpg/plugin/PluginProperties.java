package io.github.pigaut.rpg.plugin;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.boot.phase.*;
import io.github.pigaut.yaml.configurator.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PluginProperties {

    default boolean isPremium() {
        return false;
    }

    @NotNull Configurator createConfigurator();

    default void registerCommands(@NotNull CommandRegistry commands) {}
    default void registerTools(@NotNull ToolRegistry tools) {}
    default void registerListeners() {}
    default void registerHooks() {}

    default @Nullable String getDatabaseName() {
        return null;
    }

    @Nullable default String getLogo() {
        return null;
    }

    default @Nullable Integer getMetricsId() {
        return null;
    }

    default @Nullable Integer getResourceId() {
        return null;
    }

    default List<BootPhase> getStartupRequirements() {
        return List.of();
    }

    default List<StartupTask> getStartupTasks() {
        return List.of();
    }

    default @NotNull List<Integer> getIncompatibleVersions() {
        return List.of();
    }

    default @NotNull List<String> getCompatiblePlugins() {
        return List.of();
    }

    default List<String> getDefaultResources() {
        return List.of();
    }

    default Map<Integer, List<String>> getExamplesByVersion() {
        return Map.of();
    }

    default Map<String, List<String>> getExamplesByPlugin() {
        return Map.of();
    }

    default boolean forceLogoDump() {
        return !isPremium();
    }

    default boolean forceUpdateChecker() {
        return !isPremium();
    }

}
