package io.github.pigaut.rpg.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class ConfigErrorUtil {

    public static void logAll(EnhancedPlugin plugin, List<ConfigException> errors, List<ConfigException> warnings) {
        if ((errors == null || errors.isEmpty()) && (warnings == null || warnings.isEmpty())) {
            return;
        }

        errors = errors != null ? errors : Collections.emptyList();
        warnings = warnings != null ? warnings : Collections.emptyList();

        PluginLogger logger = plugin.getColoredLogger();

        if (plugin.getSettings().isDebug()) {
            errors.forEach(Throwable::printStackTrace);
            warnings.forEach(Throwable::printStackTrace);
            return;
        }

        for (String message : createFormattedMessagesByFile(plugin, errors, warnings)) {
            logger.log(Level.SEVERE, message);
        }
    }

    public static void sendAll(@NotNull EnhancedPlugin plugin, @NotNull Player player,
                               @NotNull List<ConfigException> errors, @NotNull List<ConfigException> warnings) {
        int totalIssues = errors.size() + warnings.size();
        if (totalIssues == 0) {
            return;
        }

        player.sendMessage("");

        List<String> errorMessages = createFormattedMessagesByFile(plugin, errors, warnings);
        for (String errorMessage : errorMessages) {
            player.sendMessage(ColorUtil.translateColors(errorMessage));
            player.sendMessage("");
        }
    }

    /**
     * Creates a single combined String containing all formatted errors and warnings, grouped by file.
     */
    public static String createFullErrorMessage(EnhancedPlugin plugin, List<ConfigException> errors, List<ConfigException> warnings) {
        return String.join("\n", createFormattedMessagesByFile(plugin, errors, warnings));
    }

    /**
     * Groups errors and warnings by file and creates one formatted message block per file.
     */
    public static List<String> createFormattedMessagesByFile(@NotNull EnhancedPlugin plugin, @NotNull List<ConfigException> errors, @NotNull List<ConfigException> warnings) {
        Map<String, FileReport> reports = new LinkedHashMap<>();

        for (ConfigException error : errors) {
            File file = getFile(error);
            ConfigType configType;
            if (file != null) {
                Module module = Module.fromDirectory(plugin, file);
                configType = module != null && module.hasConfigType() ? module.getConfigType() : null;
            } else {
                configType = null;
            }
            String path = getFilePath(plugin, error);
            reports.computeIfAbsent(path != null ? path : "unknown", k -> new FileReport(getPrefix(error), path, configType)).errors.add(error);
        }

        for (ConfigException warning : warnings) {
            File file = getFile(warning);
            ConfigType configType;
            if (file != null) {
                Module module = Module.fromDirectory(plugin, file);
                configType = module != null && module.hasConfigType() ? module.getConfigType() : null;
            } else {
                configType = null;
            }
            String path = getFilePath(plugin, warning);
            reports.computeIfAbsent(path != null ? path : "unknown", k -> new FileReport(getPrefix(warning), path, configType)).warnings.add(warning);
        }

        List<String> formattedMessages = new ArrayList<>();
        for (FileReport report : reports.values()) {
            StringBuilder builder = new StringBuilder();

            String optionalPrefix = report.prefix != null ? (report.prefix + " ") : "";
            String optionalFile = report.filePath != null ? report.filePath : "Unknown File";

            builder.append("&5&l").append(optionalPrefix).append("Config: &d&o").append(optionalFile).append("\n");

            appendExceptionGroup(builder, report.errors, report.configType, false);
            appendExceptionGroup(builder, report.warnings, report.configType, true);

            formattedMessages.add(builder.toString());
        }

        return formattedMessages;
    }

    private static String[] renderExceptionParts(ConfigException exception, ConfigType configType, boolean isWarning) {
        String label = isWarning ? "  &e&lWARNING: &8" : "  &c&lERROR: &8";
        String keyColor = isWarning ? "&e&l" : "&c&l";

        String labelLine;
        StringBuilder body = new StringBuilder();

        if (exception instanceof InvalidConfigException invalidException) {
            String problem = invalidException.getError();
            String problemText = problem != null ? problem.toUpperCase() : "INVALID CONFIGURATION";
            String key = configType == ConfigType.SECTION_KEY ? invalidException.getTopLevelKey() : invalidException.getFileName();
            String line = invalidException.getLine();

            labelLine = label + problemText;
            if (key != null && !key.isEmpty()) {
                body.append("    ").append(keyColor).append("Name >> &3").append(key).append("\n");
            }
            if (line != null) {
                body.append("    ").append(keyColor).append("Field >> &f").append(line).append("\n");
            }
            body.append("    ").append(keyColor).append("Desc >> &6").append(invalidException.getDetails());
        } else if (exception instanceof ConfigLoadException loadException) {
            labelLine = label + "INVALID YAML FORMAT";
            body.append("    ").append(keyColor).append("  Desc &6>> ").append(loadException.getDetails());
        } else {
            labelLine = label + exception.getMessage();
        }

        return new String[] { labelLine, body.toString() };
    }

    private static void appendExceptionGroup(StringBuilder builder, List<ConfigException> exceptions, ConfigType configType, boolean isWarning) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Map<String, String[]> content = new LinkedHashMap<>();

        for (ConfigException exception : exceptions) {
            String[] parts = renderExceptionParts(exception, configType, isWarning);
            String key = parts[0] + "\u0000" + parts[1];
            counts.merge(key, 1, Integer::sum);
            content.putIfAbsent(key, parts);
        }

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            String[] parts = content.get(entry.getKey());
            int count = entry.getValue();

            builder.append(parts[0]);
            if (count > 1) {
                builder.append(" x").append(count);
            }
            builder.append("\n");
            if (!parts[1].isEmpty()) {
                builder.append(parts[1]).append("\n");
            }
        }
    }


    private static File getFile(ConfigException exception) {
        if (exception instanceof ConfigLoadException loadException) {
            return loadException.getFile();
        } else if (exception instanceof InvalidConfigException invalidException) {
            return invalidException.getFile();
        }
        return null;
    }

    private static String getFilePath(EnhancedPlugin plugin, ConfigException exception) {
        String dataFolderPath = plugin.getDataFolder().getPath();
        if (exception instanceof ConfigLoadException loadException) {
            return loadException.getFilePath(dataFolderPath);
        } else if (exception instanceof InvalidConfigException invalidException) {
            return invalidException.getFilePath(dataFolderPath);
        }
        return null;
    }

    private static String getPrefix(ConfigException exception) {
        if (exception instanceof ConfigLoadException loadException) {
            return loadException.getPrefix();
        } else if (exception instanceof InvalidConfigException invalidException) {
            return invalidException.getPrefix();
        }
        return null;
    }

    private static class FileReport {
        final String prefix;
        final String filePath;
        final ConfigType configType;
        final List<ConfigException> errors = new ArrayList<>();
        final List<ConfigException> warnings = new ArrayList<>();

        FileReport(String prefix, String filePath, ConfigType configType) {
            this.prefix = prefix;
            this.filePath = filePath;
            this.configType = configType;
        }
    }
}
