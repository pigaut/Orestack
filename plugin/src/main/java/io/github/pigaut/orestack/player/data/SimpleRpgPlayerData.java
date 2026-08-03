package io.github.pigaut.orestack.player.data;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.player.data.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimpleRpgPlayerData extends SimplePlayerData implements RpgPlayerData {

    private Map<String, Skill> skillsByName = new HashMap<>();
    private Map<String, ItemCollection> itemCollectionsByName = new HashMap<>();

    public SimpleRpgPlayerData(@NotNull UUID playerId) {
        super(playerId);
    }

    @Override
    public @NotNull Collection<Skill> getSkills() {
        if (!isLoaded()) {
            return List.of();
        }
        return new ArrayList<>(skillsByName.values());
    }

    @Override
    public @Nullable Skill getSkill(@NotNull String name) {
        return skillsByName.get(name);
    }

    @Override
    public int getSkillCount() {
        return skillsByName.size();
    }

    @Override
    public int getSkillCount(@NotNull String groupName) {
        int skillCount = 0;
        for (Skill skill : skillsByName.values()) {
            String group = skill.getGroup();
            if (group != null && group.equals(groupName) && skill.isMaxLevel()) {
                skillCount++;
            }
        }
        return skillCount;
    }

    @Override
    public int getSkillsMaxed() {
        int skillsMaxed = 0;
        for (Skill skill : skillsByName.values()) {
            if (skill.isMaxLevel()) {
                skillsMaxed++;
            }
        }
        return skillsMaxed;
    }

    @Override
    public int getSkillsMaxed(@NotNull String groupName) {
        int skillsMaxed = 0;
        for (Skill skill : skillsByName.values()) {
            String group = skill.getGroup();
            if (group != null && group.equals(groupName) && skill.isMaxLevel()) {
                skillsMaxed++;
            }
        }
        return skillsMaxed;
    }

    public @NotNull Collection<ItemCollection> getItemCollections() {
        if (!isLoaded()) {
            return List.of();
        }
        return new ArrayList<>(itemCollectionsByName.values());
    }

    public @Nullable ItemCollection getItemCollection(@NotNull String name) {
        for (ItemCollection collection : itemCollectionsByName.values()) {
            if (collection.getName().equalsIgnoreCase(name)) {
                return collection;
            }
        }
        return null;
    }

    public @Nullable ItemCollection getItemCollection(@NotNull ItemStack item) {
        for (ItemCollection collection : itemCollectionsByName.values()) {
            if (collection.matchItem(item)) {
                return collection;
            }
        }
        return null;
    }

    @Override
    public int getCollectionCount() {
        return itemCollectionsByName.size();
    }

    @Override
    public int getCollectionCount(@NotNull String groupName) {
        int collectionCount = 0;
        for (ItemCollection collection : itemCollectionsByName.values()) {
            String group = collection.getGroup();
            if (group != null && group.equals(groupName)) {
                collectionCount++;
            }
        }
        return collectionCount;
    }

    @Override
    public int getCollectionsUnlocked() {
        int collectionsUnlocked = 0;
        for (ItemCollection collection : itemCollectionsByName.values()) {
            if (collection.isUnlocked()) {
                collectionsUnlocked++;
            }
        }
        return collectionsUnlocked;
    }

    @Override
    public int getCollectionsUnlocked(@NotNull String groupName) {
        int collectionsUnlocked = 0;
        for (ItemCollection collection : itemCollectionsByName.values()) {
            String group = collection.getGroup();
            if (group != null && group.equals(groupName) && collection.isUnlocked()) {
                collectionsUnlocked++;
            }
        }
        return collectionsUnlocked;
    }

    public void setItemCollections(@NotNull Set<ItemCollection> itemCollections) {
        Map<String, ItemCollection> newItemCollectionsByName = new HashMap<>();
        for (ItemCollection itemCollection : itemCollections) {
            newItemCollectionsByName.put(itemCollection.getName(), itemCollection);
        }
        itemCollectionsByName = newItemCollectionsByName;
    }

    public void setSkills(@NotNull Set<Skill> skills) {
        Map<String, Skill> newSkillsByName = new HashMap<>();
        for (Skill skill : skills) {
            newSkillsByName.put(skill.getName(), skill);
        }
        skillsByName = newSkillsByName;
    }

}
