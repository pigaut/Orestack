package io.github.pigaut.rpg.plugin.manager.module;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.yaml.util.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;

public enum Module {

    ITEMS("items", ConfigType.SECTION_KEY),
    MESSAGES("messages", ConfigType.SECTION_KEY),
    PARTICLES("effects/particles", ConfigType.SECTION_KEY),
    SOUNDS("effects/sounds", ConfigType.SECTION_KEY),
    MENUS("menus", ConfigType.ANY),
    RECIPES("recipes", ConfigType.SECTION_KEY),
    FUNCTIONS("functions", ConfigType.SECTION_KEY),
    STRUCTURES("structures", ConfigType.SEQUENCE),

    COLLECTIONS("collections", ConfigType.SEQUENCE),
    GENERATORS("generators", ConfigType.SEQUENCE),
    GATES("gates", ConfigType.SEQUENCE),

    COMMANDS("commands", ConfigType.ANY),
    MOBS("mobs", ConfigType.ANY),
    SKILLS("skills", ConfigType.SEQUENCE),
    STATS,
    PARTIES;

//    PARTIES,
//    QUESTS,
//    MOUNTS,
//    FISHING;

    private final String directory;
    private final ConfigType configType;

    Module() {
        this(null, null);
    }

    Module(String directory, ConfigType configType) {
        this.directory = directory;
        this.configType = configType;
    }

    public boolean hasDirectory() {
        return directory != null;
    }

    public String getDirectory() {
        Preconditions.checkOperation(directory != null, "Module does not have a directory");
        return directory;
    }

    public boolean hasConfigType() {
        return configType != null;
    }

    public ConfigType getConfigType() {
        Preconditions.checkOperation(configType != null, "Module does not have a config type");
        return configType;
    }

    public static @Nullable Module fromDirectory(@NotNull EnhancedPlugin plugin, @NotNull File file) {
        Path base = plugin.getDataFolder().toPath().normalize();
        Path target = file.toPath().normalize();

        Path relative;
        try {
            relative = base.relativize(target);
        } catch (IllegalArgumentException e) {
            return null; // file isn't under the plugin's data folder
        }

        String relativePath = relative.toString().replace(File.separatorChar, '/');

        for (Module module : values()) {
            if (!module.hasDirectory()) {
                continue;
            }

            String dir = module.getDirectory();
            if (relativePath.equals(dir) || relativePath.startsWith(dir + "/")) {
                return module;
            }
        }

        return null;
    }

}
