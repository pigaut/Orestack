package io.github.pigaut.rpg.module.collection.template;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class CollectionTemplateManager extends ConfigBackedManager<CollectionTemplate> {

    public CollectionTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.COLLECTIONS, CollectionTemplate.class);
    }

    public @Nullable CollectionTemplate get(@NotNull ItemStack item) {
        for (CollectionTemplate collection : this) {
            if (collection.getItem().isSimilar(item)) {
                return collection;
            }
        }
        return null;
    }

}
