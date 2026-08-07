package io.github.pigaut.rpg.module.command;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CustomCommandLoader implements ConfigLoader<CustomCommand> {

    private final EnhancedPlugin plugin;

    public CustomCommandLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid command";
    }

    @Override
    public @NotNull CustomCommand loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        ConfigSection commandSection = sequence.getRequiredSection(0);
        CustomCommand command = commandSection.getRequired(CustomCommand.class);

        for (int i = 1; i < sequence.size(); i++) {
            ConfigSection subCommandSection = sequence.getRequiredSection(i);
            String subCommandDefinition = subCommandSection.getRequiredString("sub-command");
            Function onSubCommandExecute = subCommandSection.get("on-execute", Function.class)
                    .withDefault(null);

            SubCommand subCommand = command.createSubCommand(subCommandDefinition);
            subCommand.withPlayerExecution((player, context, args) -> {
                if (onSubCommandExecute != null) {
                    onSubCommandExecute.run(context);
                }
            });
        }

        return command;
    }

    @Override
    public @NotNull CustomCommand loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getRequiredString("command");
        String group = Group.byCommandFile(section.getRoot().getFile());
        List<String> aliases = section.getStringList("aliases")
                .withDefault(List.of());
        Function onExecute = section.get("on-execute", Function.class)
                .withDefault(null);

        if (CommandUtil.isRegistered(name)) {
            throw new InvalidConfigException(section, "command", "Command with that name already exists");
        }

        CustomCommand command = new CustomCommand(plugin, name, group);
        command.setAliases(aliases);
        command.getRootCommand().withPlayerExecution((player, context, args) -> {
            if (onExecute != null) {
                onExecute.run(context);
            }
        });

        return command;
    }

}
