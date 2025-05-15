package io.github.pigaut.orestack.options;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.plugin.manager.*;
import org.bukkit.*;

import java.util.*;

public class OrestackOptionsManager extends Manager {

    private final Set<Material> structureBlacklist = new HashSet<>();

    public OrestackOptionsManager(OrestackPlugin plugin) {
        super(plugin);
    }

    public Set<Material> getStructureBlacklist() {
        return new HashSet<>(structureBlacklist);
    }

    @Override
    public void disable() {
        structureBlacklist.clear();
    }

    @Override
    public void loadData() {
        structureBlacklist.addAll(plugin.getConfiguration().getList("structure-save-blacklist", Material.class));
    }

}
