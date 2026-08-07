package io.github.pigaut.rpg.module.menu.config;

import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ButtonMapLoader implements ConfigLoader<ButtonMap> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid buttons";
    }

    @Override
    public @NotNull ButtonMap loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        ButtonMap buttons = new ButtonMap();

        ConfigSection buttonSection = section.getSection("buttons").withDefault(null);
        if (buttonSection == null) {
            return buttons;
        }

        Map<String, ConfigSection> buttonConfigTemplates = new HashMap<>();

        for (String buttonName : buttonSection.getKeys()) {
            if (StringUtil.isAnyEqual(buttonName, "_", "?")) {
                throw new InvalidConfigException(buttonSection, "Cannot use reserved button ids: [_, ?]");
            }

            if (buttons.contains(buttonName)) {
                throw new InvalidConfigException(buttonSection, "Duplicate button id: " + buttonName);
            }

            String templateName = buttonSection.getString("template").withDefault(null);

            if (templateName != null) {
                if (StringUtil.isAnyEqualIgnoreCase(templateName, "true", "false")) {
                    buttonConfigTemplates.put(buttonName, buttonSection);
                    continue;
                }

                ConfigSection buttonTemplateSection = buttonConfigTemplates.get(templateName);
                if (buttonTemplateSection == null) {
                    throw new InvalidConfigException(buttonSection, "template", "Could not find button template with name: " + templateName);
                }

                buttonTemplateSection = buttonTemplateSection.copy();
                List<String> args = buttonTemplateSection.getStringList("args").orEmpty();
                for (String arg : args) {
                    String value = buttonSection.getRequiredString("args." + arg);
                    buttonTemplateSection.replaceAll(arg, value);
                }

                buttons.put(buttonName, buttonTemplateSection.getRequired(ButtonTemplate.class));
                continue;
            }

            buttons.put(buttonName, buttonSection.getRequired(ButtonTemplate.class));
        }

        return buttons;
    }

    @Override
    public @NotNull ButtonMap loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        ButtonMap buttons = new ButtonMap();

        Map<String, ConfigSection> buttonConfigTemplates = new HashMap<>();

        // Skip first value, reserved for menu configuration
        for (int i = 1; i < sequence.size(); i++) {
            ConfigSection buttonSection = sequence.getRequiredSection(i);

            String buttonName = buttonSection.getRequiredString("id");

            if (StringUtil.isAnyEqual(buttonName, "_", "?")) {
                throw new InvalidConfigException(buttonSection, "Cannot use reserved button ids: [_, ?]");
            }

            if (buttons.contains(buttonName)) {
                throw new InvalidConfigException(buttonSection, "Duplicate button id: " + buttonName);
            }

            String templateName = buttonSection.getString("template").withDefault(null);

            if (templateName != null) {
                if (StringUtil.isAnyEqualIgnoreCase(templateName, "true", "false")) {
                    buttonConfigTemplates.put(buttonName, buttonSection);
                    continue;
                }

                ConfigSection buttonTemplateSection = buttonConfigTemplates.get(templateName);
                if (buttonTemplateSection == null) {
                    throw new InvalidConfigException(buttonSection, "template", "Could not find button template with name: " + templateName);
                }

                buttonTemplateSection = buttonTemplateSection.copy();
                List<String> args = buttonTemplateSection.getStringList("args").orEmpty();
                for (String arg : args) {
                    String value = buttonSection.getRequiredString("args." + arg);
                    buttonTemplateSection.replaceAll(arg, value);
                }

                buttons.put(buttonName, buttonTemplateSection.getRequired(ButtonTemplate.class));
                continue;
            }

            buttons.put(buttonName, buttonSection.getRequired(ButtonTemplate.class));
        }

        return buttons;
    }
}
