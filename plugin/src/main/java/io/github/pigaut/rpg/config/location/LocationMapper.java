package io.github.pigaut.rpg.config.location;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.configurator.map.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;
import org.snakeyaml.engine.v2.common.*;

public class LocationMapper implements ConfigMapper<Location> {

    @Override
    public @NotNull FieldType getDefaultMappingType() {
        return FieldType.SEQUENCE;
    }

    @Override
    public void mapToSequence(@NotNull ConfigSequence sequence, @NotNull Location location) {
        World world = location.getWorld();
        if (world != null) {
            sequence.add(world.getName());
        }

        sequence.add(location.getX());
        sequence.add(location.getY());
        sequence.add(location.getZ());

        float yaw = location.getYaw();
        double pitch = location.getPitch();

        if (yaw != 0 || pitch != 0) {
            sequence.add(yaw);
            sequence.add(pitch);
        }

        sequence.setFlowStyle(FlowStyle.FLOW);
    }

    @Override
    public void mapToSection(@NotNull ConfigSection section, Location location) {
        World world = location.getWorld();

        if (world != null) {
            section.set("world", world.getName());
        }

        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());

        float yaw = location.getYaw();
        if (yaw != 0) {
            section.set("yaw", yaw);
        }

        double pitch = location.getPitch();
        if (pitch != 0) {
            section.set("pitch", pitch);
        }

        section.setFlowStyle(FlowStyle.FLOW);
    }

}
