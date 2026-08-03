package io.github.pigaut.orestack.player.data;

import io.github.pigaut.orestack.collection.ItemCollection;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.player.data.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface RpgPlayerData extends PlayerData {

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
