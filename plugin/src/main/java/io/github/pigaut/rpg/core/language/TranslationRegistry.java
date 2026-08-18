package io.github.pigaut.rpg.core.language;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.node.section.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class TranslationRegistry implements ConfigBacked {

    private final EnhancedPlugin plugin;
    private final Map<String, String> dictionary = new HashMap<>();

    public TranslationRegistry(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public String get(@NotNull String name) throws TranslationNotFoundException {
        final String lang = dictionary.get(name);
        if (lang == null) {
            throw new TranslationNotFoundException(name);
        }
        return lang;
    }

    @NotNull
    public String getOrDefault(@NotNull String name, @NotNull String def) {
        return dictionary.getOrDefault(name, def);
    }

    public void register(@NotNull String name, @NotNull String message) {
        dictionary.put(name, message);
    }

    public void unregister(@NotNull String name) {
        dictionary.remove(name);
    }

    public void clear() {
        dictionary.clear();
    }

    @Override
    public @NotNull ErrorCollector loadConfiguration() {
        Settings settings = plugin.getSettings();
        File file = settings.getLanguageFile();
        RootSection existingConfig = new RootSection(file, plugin.getConfigurator(), "Language");
        if (file.exists() || settings.isGenerateLanguageFiles()) {
            existingConfig.loadOrEmpty();
        }

        RootSection defaultConfig = YamlConfig.createEmptySection();
        try (InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream("languages/" + file.getName())) {
            if (inputStream != null) {
                defaultConfig.loadFromStream(inputStream);
            }
        }
        catch (IOException | ConfigLoadException ignored) {
            // Ignore errors loading internal language file
        }

        // Add missing keys in language config with default values
        Set<String> existingKeys = existingConfig.getKeys();
        for (String key : defaultConfig.getKeys()) {
            if (!existingKeys.contains(key)) {
                String defaultTranslation = defaultConfig.getString(key).orElse("not set");
                existingConfig.set(key, defaultTranslation); // write into existingConfig instead of dictionary directly

                if (file.exists()) {
                    existingConfig.collectWarning(
                            new InvalidConfigException(existingConfig, key, "Translation not found: " + key + " (Fix or regenerate the language file)")
                    );
                }
            }
        }

        settings.applyConfigShortcuts(existingConfig);

        // Register existing translations in language config
        for (String key : existingConfig.getKeys()) {
            String translation = existingConfig.getString(key)
                    .withDefault("not set");

            register(CaseFormatter.toKebabCase(key), translation);
        }

        return existingConfig;
    }

}
