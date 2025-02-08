package io.github.pigaut.orestack.util;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import org.bukkit.*;

public class OrestackAPI {

    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public static GeneratorTemplate getGeneratorTemplate(String name) {
        return plugin.getGeneratorTemplate(name);
    }

    public static boolean isGenerator(Location location) {
        return plugin.getGenerators().isGenerator(location);
    }

}
