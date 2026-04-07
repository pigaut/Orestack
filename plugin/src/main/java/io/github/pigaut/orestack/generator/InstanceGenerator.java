package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import org.bukkit.*;

import java.util.*;

public class InstanceGenerator extends Generator {

    private final Map<UUID, GeneratorState> stateByPlayerId = new HashMap<>();

    public InstanceGenerator(GeneratorTemplate template, Location origin, Rotation rotation) {
        super(template, origin, rotation);
    }

}
