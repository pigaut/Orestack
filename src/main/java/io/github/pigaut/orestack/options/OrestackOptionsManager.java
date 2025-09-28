package io.github.pigaut.orestack.options;

import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.checkerframework.checker.units.qual.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackOptionsManager extends Manager implements ConfigBacked {

    private boolean keepBlocksOnRemove;
    private ItemStack generatorTool;

    public OrestackOptionsManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull List<ConfigurationException> loadConfigurationData() {
        List<ConfigurationException> errorsFound = new ArrayList<>();

        ConfigSection config = plugin.getConfiguration();

        keepBlocksOnRemove = config.getBoolean("keep-blocks-on-remove")
                .withDefault(false, errorsFound::add);

        generatorTool = config.get("generator-tool", ItemStack.class)
                .withDefault(GeneratorTool.getDefaultItem(), errorsFound::add);

        return errorsFound;
    }

    public boolean isKeepBlocksOnRemove() {
        return keepBlocksOnRemove;
    }

    public @NotNull ItemStack getGeneratorTool() {
        return generatorTool.clone();
    }

}
