package io.github.pigaut.orestack.options;

import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.checkerframework.checker.units.qual.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackOptionsManager extends Manager implements ConfigBacked {

    private ItemStack generatorTool;

    public OrestackOptionsManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull List<ConfigurationException> loadConfigurationData() {
        List<ConfigurationException> errorsFound = new ArrayList<>();

        ConfigSection config = plugin.getConfiguration();
        generatorTool = config.get("generator-tool", ItemStack.class)
                .withDefault(GeneratorTool.getDefaultItem(), errorsFound::add);

        return errorsFound;
    }

    public @NotNull ItemStack getGeneratorTool() {
        return generatorTool.clone();
    }

}
