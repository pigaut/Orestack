package io.github.pigaut.rpg.core.hologram;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiHologramTemplate implements HologramTemplate {

    private final List<HologramTemplate> holograms;

    public MultiHologramTemplate(@NotNull List<@NotNull HologramTemplate> holograms) {
        this.holograms = holograms;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        Hologram hologram = new MultiLineHologramDisplay(location, rotation, context);
        return hologram;
    }

    private class MultiLineHologramDisplay implements Hologram {

        private final List<Hologram> displays;

        protected MultiLineHologramDisplay(Location location, Rotation rotation, Context context) {
            displays = holograms.stream()
                    .map(hologram -> hologram.spawn(location, rotation, context))
                    .filter(Objects::nonNull)
                    .toList();
        }

        @Override
        public boolean exists() {
            for (Hologram display: displays) {
                if (display.exists()) return true;
            }
            return false;
        }

        @Override
        public void update() {
            for (Hologram display : displays) {
                display.update();
            }
        }

        @Override
        public void remove() {
            for (Hologram display: displays) {
                display.remove();
            }
        }

    }

}
