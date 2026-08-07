package io.github.pigaut.rpg.config.location;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class LocationLoader implements ConfigLoader<Location> {

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid location";
    }

    @Override
    public @NotNull Location loadFromSection(@NotNull ConfigSection config) throws InvalidConfigException {
        World world = config.get("world", World.class).withDefault(null);
        double x = config.getRequiredDouble("x");
        double y = config.getRequiredDouble("y");
        double z = config.getRequiredDouble("z");
        float yaw = config.getFloat("yaw").withDefault(0f);
        float pitch = config.getFloat("pitch").withDefault(0f);
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull Location loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        switch (sequence.size()) {
            case 3 -> {
                double x = sequence.getRequiredDouble(0);
                double y = sequence.getRequiredDouble(1);
                double z = sequence.getRequiredDouble(2);
                return new Location(null, x, y, z);
            }
            case 4 -> {
                World world = sequence.getRequired(0, World.class);
                double x = sequence.getRequiredDouble(1);
                double y = sequence.getRequiredDouble(2);
                double z = sequence.getRequiredDouble(3);
                return new Location(world, x, y, z);
            }
            case 5 -> {
                double x = sequence.getRequiredDouble(0);
                double y = sequence.getRequiredDouble(1);
                double z = sequence.getRequiredDouble(2);
                float yaw = sequence.getRequiredFloat(3);
                float pitch = sequence.getRequiredFloat(4);
                return new Location(null, x, y, z, yaw, pitch);
            }
            case 6 -> {
                World world = sequence.getRequired(0, World.class);
                double x = sequence.getRequiredDouble(1);
                double y = sequence.getRequiredDouble(2);
                double z = sequence.getRequiredDouble(3);
                float yaw = sequence.getRequiredFloat(4);
                float pitch = sequence.getRequiredFloat(5);
                return new Location(world, x, y, z, yaw, pitch);
            }
            default -> throw new InvalidConfigException(sequence, "Wrong format: [(world), <x>, <y>, <z>, (yaw), (pitch)]");
        }
    }

}
