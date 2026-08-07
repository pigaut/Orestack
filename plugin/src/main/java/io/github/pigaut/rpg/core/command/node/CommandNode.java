package io.github.pigaut.rpg.core.command.node;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.completion.*;
import io.github.pigaut.rpg.core.command.execution.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.completion.*;
import io.github.pigaut.rpg.core.command.execution.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class CommandNode implements Iterable<SubCommand> {

    private final EnhancedPlugin plugin;
    private final String name;
    private String fullCommand;
    private final Map<String, SubCommand> children = new LinkedHashMap<>();
    private String description = "";
    private @Nullable String permission = null;
    private List<CommandParameter> parameters = new ArrayList<>();
    private int minArgs = 0;
    private int maxArgs = 0;

    private @Nullable CommandExecution commandExecution = null;
    private @Nullable PlayerExecution playerExecution = null;
    private @NotNull CommandCompletion commandCompletion = new SubCommandCompletion();

    protected CommandNode(@NotNull EnhancedPlugin plugin, @NotNull String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract boolean isRoot();

    @NotNull
    public abstract RootCommand getRoot();

    @NotNull
    public abstract CommandNode getParent();

    public int size() {
        return children.size();
    }

    public void clear() {
        children.clear();
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getCommand() {
        return getRoot().getName();
    }

    @NotNull
    public String getFullCommand() {
        return fullCommand != null ? fullCommand : (fullCommand = String.join(" ", buildCommand()));
    }

    private String[] buildCommand() {
        List<String> branch = parameters.stream().map(CommandParameter::toString).collect(Collectors.toList());

        CommandNode command = this;
        while (!command.isRoot()) {
            branch.add(0, command.getName());
            command = command.getParent();
        }
        branch.add(0, command.getName());
        return branch.toArray(new String[branch.size()]);
    }

    public boolean isSubCommand(String name) {
        return children.get(name) != null;
    }

    @Nullable
    public SubCommand getSubCommand(String name) {
        return children.get(name);
    }

    @NotNull
    public SubCommand getSubCommandOrCreate(String name) {
        final SubCommand foundCommand = getSubCommand(name);
        if (foundCommand != null) {
            return foundCommand;
        }

        final SubCommand newSubCommand = new SubCommand(plugin, name);
        addSubCommand(newSubCommand);
        return newSubCommand;
    }

    public List<SubCommand> getSubCommands() {
        return new ArrayList<>(children.values());
    }

    @NotNull
    public SubCommand createSubCommand(String command) {
        command = command.toLowerCase(Locale.ENGLISH).trim();

        CommandNode currentCommand = this;
        for (String commandPart : command.split(" ")) {
            if (CommandParameter.isParameter(commandPart)) {
                String[] parameterParts = StringUtil.removeParentheses(commandPart).split(":");
                String parameterName = parameterParts[0];
                String defaultValue = parameterParts.length == 2 ? parameterParts[1] : null;
                currentCommand.withParameter(CommandParameter.create(parameterName, defaultValue));
                continue;
            }
            currentCommand = currentCommand.getSubCommandOrCreate(commandPart);
        }
        return (SubCommand) currentCommand;
    }

    public void addSubCommand(SubCommand subCommand) {
        children.put(subCommand.getName(), subCommand);
        subCommand.setParent(this);
    }

    public void removeSubCommand(String name) {
        children.remove(name);
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    public CommandNode withDescription(@NotNull String description) {
        this.description = description;
        return this;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    public CommandNode withPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @NotNull
    public List<CommandParameter> getParameters() {
        return parameters;
    }

    public CommandNode withParameters(CommandParameter... parameters) {
        this.parameters = Arrays.asList(parameters);
        updateParameterBounds();
        return this;
    }

    public CommandNode withParameter(CommandParameter parameter) {
        parameters.add(parameter);
        updateParameterBounds();
        return this;
    }

    private void updateParameterBounds() {
        maxArgs = parameters.size();
        for (int i = 0; i < parameters.size(); i++) {
            if (!parameters.get(i).isOptional()) {
                minArgs = i + 1;
                continue;
            }
            break;
        }
    }

    public boolean isExecutable() {
        return commandExecution != null || playerExecution != null;
    }

    public boolean isPlayerOnly() {
        return commandExecution == null && playerExecution != null;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public int getMaxArgs() {
        return maxArgs;
    }

    public @Nullable CommandExecution getCommandExecution() {
        return commandExecution;
    }

    public @Nullable PlayerExecution getPlayerExecution() {
        return playerExecution;
    }

    public CommandNode withCommandExecution(CommandExecution commandExecution) {
        this.commandExecution = commandExecution;
        return this;
    }

    public CommandNode withPlayerExecution(@Nullable PlayerExecution playerExecution) {
        this.playerExecution = playerExecution;
        return this;
    }

    public CommandNode withPlayerStateExecution(@NotNull PlayerStateExecution playerExecution) {
        return withPlayerExecution((player, context, args) -> {
            PlayerState playerState = plugin.getPlayerState(player);
            playerExecution.execute(playerState, context, args);
        });
    }

    public @NotNull CommandCompletion getCommandCompletion() {
        return commandCompletion;
    }

    public CommandNode withTabCompletion(@NotNull CommandCompletion tabCompleter) {
        this.commandCompletion = tabCompleter;
        return this;
    }

    public CommandNode withPlayerCompletion(@NotNull PlayerCompletion tabCompleter) {
        this.commandCompletion = tabCompleter;
        return this;
    }

    public final void execute(CommandSender sender, String[] args) {
        Context context = Context.fromCommand(plugin, sender, this, args);

        int argsCount = args.length;
        if (argsCount < minArgs) {
            plugin.sendMessage(sender, context, "not-enough-args");
            return;
        }

        if (argsCount > maxArgs) {
            plugin.sendMessage(sender, context, "too-many-args");
            return;
        }

        if (permission != null && !sender.hasPermission(permission)) {
            plugin.sendMessage(sender, context, "no-permission");
            return;
        }

        String[] fullArgs = new String[this.parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            CommandParameter parameter = parameters.get(i);
            String value = args.length > i ? args[i] : parameter.getDefaultValue();
            fullArgs[i] = value;
        }

        if (isPlayerOnly()) {
            if (sender instanceof Player player) {
                playerExecution.execute(player, context, fullArgs);
                return;
            }
            plugin.sendMessage(sender, context, "player-only");
            return;
        }

        if (commandExecution != null) {
            commandExecution.execute(sender, context, fullArgs);
            return;
        }

        plugin.sendMessage(sender, context, "usage");
    }

    public final List<String> tabComplete(CommandSender sender, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            return List.of();
        }

        final int argsLength = args.length;
        if (argsLength < 1) {
            return List.of();
        }

        final int parametersCount = parameters.size();
        if (parametersCount == 0) {
            return commandCompletion.tabComplete(sender, args);
        }
        else if (args.length <= parametersCount) {
            final CommandParameter parameter = parameters.get(argsLength - 1);
            return parameter.tabComplete(sender, args);
        }

        return List.of();
    }

    @Override
    public Iterator<SubCommand> iterator() {
        return new SubCommandIterator(this);
    }

    private static class SubCommandIterator implements Iterator<SubCommand> {
        private final Deque<SubCommand> stack = new ArrayDeque<>();

        public SubCommandIterator(CommandNode root) {
            pushSubCommands(root.getSubCommands());
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        private void pushSubCommands(List<SubCommand> subCommands) {
            for (int i = subCommands.size() - 1; i >= 0; i--) {
                stack.push(subCommands.get(i));
            }
        }

        @Override
        public SubCommand next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            final SubCommand current = stack.pop();
            pushSubCommands(current.getSubCommands());
            return current;
        }
    }

    @Override
    public void forEach(Consumer<? super SubCommand> action) {
        Objects.requireNonNull(action);
        for (SubCommand node : this) {
            action.accept(node);
        }
    }

    @Override
    public Spliterator<SubCommand> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED);
    }

    protected class SubCommandCompletion implements CommandCompletion {
        @Override
        public @NotNull List<@NotNull String> tabComplete(CommandSender sender, String[] args) {
            return getSubCommands().stream()
                    .map(SubCommand::getName)
                    .toList();
        }
    }

}
