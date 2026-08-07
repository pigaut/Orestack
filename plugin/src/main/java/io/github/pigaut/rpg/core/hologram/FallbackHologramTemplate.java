package io.github.pigaut.rpg.core.hologram;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FallbackHologramTemplate implements HologramTemplate {

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        Entity entity = EntityUtil.createHologram("&c&lMissing Hologram Provider", location, true);
        return new FallbackHologram(entity.getUniqueId());
    }

    private static class FallbackHologram implements Hologram {

        private final UUID entityId;

        public FallbackHologram(UUID entityId) {
            this.entityId = entityId;
        }

        @Override
        public boolean exists() {
            return Bukkit.getEntity(entityId) != null;
        }

        @Override
        public void update() {

        }

        @Override
        public void remove() {
            Entity entity = Bukkit.getEntity(entityId);
            if (entity != null) {
                entity.remove();
            }
        }
    }

}
