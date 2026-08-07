package io.github.pigaut.rpg.module.mob;

import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MobManager extends Manager {

    private final Map<UUID, Mob> mobsById = new HashMap<>();
    private final Set<Mob> mobsToRestore = new HashSet<>();

    public MobManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    public @Nullable Mob get(@NotNull UUID entityId) {
        return mobsById.get(entityId);
    }

    public @Nullable Mob get(@NotNull Entity entity) {
        return get(entity.getUniqueId());
    }

    public @NotNull Collection<Mob> getAll() {
        return new ArrayList<>(mobsById.values());
    }

    public void register(@NotNull Mob mob) {
        mobsById.put(mob.getEntityId(), mob);
    }

    public void unregister(@NotNull UUID entityId) {
        mobsById.remove(entityId);
    }

    @Override
    public void enable() {
        for (Mob mob : mobsToRestore) {
            mob.remove();
            Location location = mob.getLocation();
            MobTemplate mobTemplate = plugin.getMobTemplate(mob.getTemplate().getName());
            if (mob.getSpawnPad() != null || location == null || mobTemplate == null) {
                continue;
            }
            register(mobTemplate.spawn(location));
        }
        mobsToRestore.clear();
    }

    @Override
    public void disable() {
        for (Mob mob : getAll()) {
            mob.remove();
            if (mob.getSpawnPad() == null) {
                mobsToRestore.add(mob);
            }
        }
    }

}
