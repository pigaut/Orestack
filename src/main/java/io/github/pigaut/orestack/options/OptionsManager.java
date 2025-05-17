package io.github.pigaut.orestack.options;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class OptionsManager extends Manager {

    private ItemStack generatorTool;

    public OptionsManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void loadData() {
        super.loadData();
        final ConfigSection config = plugin.getConfiguration();
        generatorTool = config.get("generator-tool", ItemStack.class);
    }

    public @NotNull ItemStack getGeneratorTool() {
        return generatorTool.clone();
    }

    public void setGeneratorTool(@NotNull ItemStack generatorTool) {
        this.generatorTool = generatorTool;
    }

}
