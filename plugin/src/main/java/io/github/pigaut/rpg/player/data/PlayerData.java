package io.github.pigaut.rpg.player.data;

import io.github.pigaut.rpg.module.collection.ItemCollection;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.player.data.base.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PlayerData extends EnhancedPlayerData {

    @NotNull
    Set<NamespacedKey> getUnlockedRecipes();

    boolean hasUnlockedRecipe(@NotNull NamespacedKey recipe);

    void addUnlockedRecipe(@NotNull NamespacedKey recipe);

    void removeUnlockedRecipe(@NotNull NamespacedKey recipe);

    @NotNull
    Collection<Skill> getSkills();

    @Nullable
    Skill getSkill(@NotNull String name);

    int getSkillCount();

    int getSkillCount(@NotNull String group);

    int getSkillsMaxed();

    int getSkillsMaxed(@NotNull String group);

    @NotNull
    Collection<ItemCollection> getItemCollections();

    @Nullable
    ItemCollection getItemCollection(@NotNull String name);

    @Nullable
    ItemCollection getItemCollection(@NotNull ItemStack item);

    int getCollectionCount();

    int getCollectionCount(@NotNull String group);

    int getCollectionsUnlocked();

    int getCollectionsUnlocked(@NotNull String group);

}
