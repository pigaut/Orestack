package io.github.pigaut.orestack.player.data;

import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.player.data.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import io.github.pigaut.orestack.collection.ItemCollection;

import java.util.*;

public class SimpleRpgPlayerData extends SimplePlayerData implements RpgPlayerData {

    private Map<String, Skill> skillsByName = new HashMap<>();
    private Set<ItemCollection> itemCollections = new HashSet<>();

    public SimpleRpgPlayerData(@NotNull UUID playerId) {
        super(playerId);
    }

    public @Nullable ItemCollection getItemCollection(@NotNull ItemStack item) {
        for (ItemCollection collection : itemCollections) {
            if (collection.matchItem(item)) {
                return collection;
            }
        }
        return null;
    }

    public @Nullable ItemCollection getItemCollection(@NotNull String name) {
        for (ItemCollection collection : itemCollections) {
            if (collection.getName().equalsIgnoreCase(name)) {
                return collection;
            }
        }
        return null;
    }

    @Override
    public @NotNull Set<Skill> getSkills() {
        return new HashSet<>(skillsByName.values());
    }

    @Override
    public @Nullable Skill getSkill(@NotNull String name) {
        return skillsByName.get(name);
    }

    public void setSkills(@NotNull Set<Skill> skills) {
        skillsByName = new HashMap<>();
        for (Skill skill : skills) {
            skillsByName.put(skill.getName(), skill);
        }
    }

    public @NotNull Set<ItemCollection> getItemCollections() {
        return new HashSet<>(itemCollections);
    }

    public void setItemCollections(@NotNull Set<ItemCollection> itemCollections) {
        this.itemCollections = itemCollections;
    }

}
