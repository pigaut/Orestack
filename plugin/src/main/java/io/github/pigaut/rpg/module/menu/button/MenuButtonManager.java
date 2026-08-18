package io.github.pigaut.rpg.module.menu.button;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class MenuButtonManager extends Manager implements ConfigBacked, Toggleable {

    private final Map<String, ButtonTemplate> buttonsById = new HashMap<>();

    public MenuButtonManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull Module getModule() {
        return Module.MENUS;
    }

    public boolean contains(@NotNull String id) {
        return buttonsById.containsKey(id);
    }

    public @Nullable ButtonTemplate get(@NotNull String id) {
        return buttonsById.get(id);
    }

    @Override
    public @NotNull ErrorCollector loadConfiguration() {
        File file = plugin.getFile("menus/buttons.yml");
        RootSequence sequence = YamlConfig.loadSequenceOrEmpty(file, plugin.getConfigurator(), "Buttons");

        plugin.getSettings().applyConfigShortcuts(sequence);

        for (ConfigSection buttonSection : sequence.getNestedSections()) {
            String id = buttonSection.getString("id")
                    .require(s -> !StringUtil.isAnyEqual(s, "_", "?"),
                            "Cannot use reserved button ids: [_, ?]")
                    .withDefault(null);

            ButtonTemplate button = buttonSection.get(ButtonTemplate.class)
                    .withDefault(null);

            if (id != null && button != null) {
                buttonsById.put(id, button);
            }
        }

        buttonsById.put("_", null);
        buttonsById.put("?", null);

        return sequence;
    }

}
