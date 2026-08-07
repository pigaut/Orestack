package io.github.pigaut.rpg.command.recipe;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class RecipeSubCommand extends SubCommand {

    public RecipeSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "recipe");
        this.withPermission(plugin.getPermission("recipe"));
        this.withDescription(plugin.getTranslation("recipe-command"));
        this.addSubCommand(new RecipeUnlockSubCommand(plugin));
        this.addSubCommand(new RecipeLockSubCommand(plugin));
        this.addSubCommand(new RecipeUnlockAllSubCommand(plugin));
        this.addSubCommand(new RecipeLockAllSubCommand(plugin));
    }

}
