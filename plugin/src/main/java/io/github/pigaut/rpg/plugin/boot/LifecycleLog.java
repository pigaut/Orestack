package io.github.pigaut.rpg.plugin.boot;

import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.logging.*;

public class LifecycleLog {

    public static void startup(@NotNull EnhancedJavaPlugin plugin, @NotNull List<ErrorCollector> errorCollectors) {
        StringJoiner message = new StringJoiner("\n  ");

        String logo = plugin.getLogo() +  " v"  +  plugin.getVersion() + " startup";
        message.add(logo);

        boolean knownVersion = Server.getVersion() != Version.UNKNOWN;
        message.add((knownVersion ? "&aCompatible server version: "
                : "&cUnknown server version: ") + Server.getVersionName());

        message.add("&aCompatible plugins:");
        List<String> compatiblePlugins = plugin.getCompatiblePlugins();
        StringJoiner foundPluginsJoiner = new StringJoiner(", ");
        for (int i = 0; i < compatiblePlugins.size(); i++) {
            String pluginName = compatiblePlugins.get(i);
            boolean enabled = Server.isPluginEnabled(pluginName);

            foundPluginsJoiner.add((enabled ? "&e" : "&7") + pluginName);
            if ((i + 1) % 4 == 0) {
                message.add("  " + foundPluginsJoiner);
                foundPluginsJoiner = new StringJoiner(", ");
            }
        }
        if (foundPluginsJoiner.length() > 0) {
            message.add("  " + foundPluginsJoiner);
        }

        boolean metrics = plugin.getSettings().isMetrics() && plugin.getMetricsId() != null;
        if (metrics) {
            message.add("&aStarting bStats metrics collection...");
        }
        else {
            message.add("&cMetrics collection is disabled");
        }

        boolean updateChecker = plugin.getResourceId() != null && (plugin.forceUpdateChecker() || plugin.getSettings().isCheckForUpdates());
        if (updateChecker) {
            message.add("&aStarting update checker...");
        }
        else {
            message.add("&cUpdate checker is disabled");
        }

        boolean generateExamples = plugin.getSettings().isGenerateExamples();
        if (generateExamples) {
            message.add("&aGenerating example files...");
        }
        else {
            message.add("&cExample files generation is disabled");
        }

        boolean database = plugin.getDatabaseName() != null;
        if (database) {
            message.add("&aEstablishing database connection...");
        }

        message.add("&aLoading configuration and data...");

        List<ConfigException> errors = new ArrayList<>();
        errorCollectors.forEach(collector -> errors.addAll(collector.getErrors()));

        List<ConfigException> warnings = new ArrayList<>();
        errorCollectors.forEach(collector -> warnings.addAll(collector.getWarnings()));

        int errorCount = errors.size();
        int warningCount = warnings.size();
        int issueCount = errorCount + warningCount;

        if (issueCount == 0) {
            message.add("&aStartup completed successfully.");
        }
        else {
            if (plugin.getSettings().isDebug()) {
                message.add("&aStartup completed with (" + errorCount + ") errors and (" + warningCount + ") warnings:");
                plugin.getColoredLogger().log(Level.INFO, message.toString());
                for (ConfigException exception : errors) {
                    exception.printStackTrace();
                }
                for (ConfigException exception : warnings) {
                    exception.printStackTrace();
                }
                return;
            }

            message.add("&aStartup completed with (" + errorCount + ") errors and (" + warningCount + ") warnings:");
            String[] messageLines = ConfigErrorUtil.createFullErrorMessage(plugin, errors, warnings).split("\\n");
            for (String messageLine : messageLines) {
                message.add("  " + messageLine);
            }
        }

        plugin.getColoredLogger().log(Level.INFO, message.toString());
    }

    public static void reload(@NotNull EnhancedJavaPlugin plugin, @NotNull ErrorCollector errorCollector) {
        StringJoiner message = new StringJoiner("\n  ");

        String logo = plugin.getLogo() +  " v"  +  plugin.getVersion() + " reload";
        message.add(logo);

        boolean knownVersion = Server.getVersion() != Version.UNKNOWN;
        message.add((knownVersion ? "&aCompatible server version: "
                : "&cUnknown server version: ") + Server.getVersionName());

        message.add("&aCompatible plugins:");
        List<String> compatiblePlugins = plugin.getCompatiblePlugins();
        StringJoiner foundPluginsJoiner = new StringJoiner(", ");
        for (int i = 0; i < compatiblePlugins.size(); i++) {
            String pluginName = compatiblePlugins.get(i);
            boolean enabled = Server.isPluginEnabled(pluginName);

            foundPluginsJoiner.add((enabled ? "&e" : "&7") + pluginName);
            if ((i + 1) % 4 == 0) {
                message.add("  " + foundPluginsJoiner);
                foundPluginsJoiner = new StringJoiner(", ");
            }
        }
        if (foundPluginsJoiner.length() > 0) {
            message.add("  " + foundPluginsJoiner);
        }

        boolean metrics = plugin.getSettings().isMetrics() && plugin.getMetricsId() != null;
        if (metrics) {
            message.add("&aStarting bStats metrics collection...");
        }
        else {
            message.add("&cMetrics collection is disabled");
        }

        boolean updateChecker = plugin.getResourceId() != null && (plugin.forceUpdateChecker() || plugin.getSettings().isCheckForUpdates());
        if (updateChecker) {
            message.add("&aStarting update checker...");
        }
        else {
            message.add("&cUpdate checker is disabled");
        }

        boolean generateExamples = plugin.getSettings().isGenerateExamples();
        if (generateExamples) {
            message.add("&aGenerating example files...");
        }
        else {
            message.add("&cExample files generation is disabled");
        }

        boolean database = plugin.getDatabaseName() != null;
        if (database) {
            message.add("&aEstablishing database connection...");
        }

        message.add("&aSaving data to database...");
        message.add("&aLoading configuration and data...");

        List<ConfigException> errors = errorCollector.getErrors();
        List<ConfigException> warnings = errorCollector.getWarnings();

        int errorCount = errorCollector.getErrorCount();
        int warningCount = errorCollector.getWarningCount();
        int issueCount = errorCount + warningCount;

        if (issueCount == 0) {
            message.add("&aReload completed successfully.");
        }
        else {
            if (plugin.getSettings().isDebug()) {
                message.add("&aReload completed with &c(" + errorCount + ") errors &aand &e(" + warningCount + ") warnings:");
                plugin.getColoredLogger().log(Level.INFO, message.toString());
                for (ConfigException exception : errors) {
                    exception.printStackTrace();
                }
                for (ConfigException exception : warnings) {
                    exception.printStackTrace();
                }
                return;
            }

            message.add("&aReload completed with &c(" + errorCount + ") errors &aand &e(" + warningCount + ") warnings:");
            String[] messageLines = ConfigErrorUtil.createFullErrorMessage(plugin, errors, warnings).split("\\n");
            for (String messageLine : messageLines) {
                message.add("  " + messageLine);
            }
        }

        plugin.getColoredLogger().log(Level.INFO, message.toString());
    }


    public static void shutdown(EnhancedJavaPlugin plugin) {
        StringJoiner message = new StringJoiner("\n  ");

        String logo = plugin.getLogo() +  " v"  +  plugin.getVersion() + " shutdown";
        message.add(logo);

        message.add("&aSaving data to database...");
        message.add("&cClosing database connection...");

        plugin.getColoredLogger().log(Level.INFO, message.toString());
    }

}
