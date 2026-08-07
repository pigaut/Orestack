package io.github.pigaut.rpg.core.command;

import io.github.pigaut.rpg.core.command.completion.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.core.command.completion.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.command.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface CommandParameter extends CommandCompletion {

    String getName();

    default boolean isOptional() {
        return false;
    }

    default @Nullable String getDefaultValue() {
        return null;
    }

    static boolean isParameter(String name) {
        return StringUtil.isParenthesized(name, "(", ")") ||
                StringUtil.isParenthesized(name, "<", ">");
    }

    static CommandParameter create(String name) {
        return create(name, null, null);
    }

    static CommandParameter create(String name, String defaultValue) {
        return create(name, defaultValue, null);
    }

    static CommandParameter create(String name, CommandCompletion completions) {
        return create(name, null, completions);
    }

    static CommandParameter create(String name, @Nullable String defaultValue, @Nullable CommandCompletion completions) {
        return new CommandParameter() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean isOptional() {
                return defaultValue != null;
            }

            @Override
            public @Nullable String getDefaultValue() {
                return defaultValue;
            }

            @Override
            public @NotNull List<@NotNull String> tabComplete(CommandSender sender, String[] args) {
                return completions != null ? completions.tabComplete(sender, args) : List.of();
            }

            @Override
            public String toString() {
                return isOptional() ? "(" + name + ")" : "<" + name + ">";
            }
        };
    }

}
