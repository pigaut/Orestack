package io.github.pigaut.orestack.player.data;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.player.data.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimpleRpgPlayerData extends SimplePlayerData implements RpgPlayerData {

    private Map<String, Skill> skillsByName = new HashMap<>();
    private Set<ItemCollection> itemCollections = new HashSet<>();

    public SimpleRpgPlayerData(@NotNull UUID playerId) {
        super(playerId);
    }

    @Override
    public @NotNull Set<Skill> getSkills() {
        return new HashSet<>(skillsByName.values());
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

    public @NotNull Set<ItemCollection> getItemCollections() {
        return new HashSet<>(itemCollections);
    }

    public @Nullable ItemCollection getItemCollection(@NotNull String name) {
        for (ItemCollection collection : itemCollections) {
            if (collection.getName().equalsIgnoreCase(name)) {
                return collection;
            }
        }
        return null;
    }

    public @Nullable ItemCollection getItemCollection(@NotNull ItemStack item) {
        for (ItemCollection collection : itemCollections) {
            if (collection.matchItem(item)) {
                return collection;
            }
        }
        return null;
    }

    @Override
    public int getCollectionCount() {
        return itemCollections.size();
    }

    @Override
    public int getCollectionsUnlocked() {
        int collectionsUnlocked = 0;
        for (ItemCollection collection : itemCollections) {
            if (collection.isUnlocked()) {
                collectionsUnlocked++;
            }
        }
        return collectionsUnlocked;
    }

    public void setItemCollections(@NotNull Set<ItemCollection> itemCollections) {
        this.itemCollections = itemCollections;
    }

    public void setSkills(@NotNull Set<Skill> skills) {
        skillsByName = new HashMap<>();
        for (Skill skill : skills) {
            skillsByName.put(skill.getName(), skill);
        }
    }

}
