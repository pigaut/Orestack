package io.github.pigaut.rpg.module.structure.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StructureTemplateLoader implements ConfigLoader.Line<StructureTemplate> {

    private final EnhancedPlugin plugin;

    public StructureTemplateLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid block/structure";
    }

    @Override
    public @NotNull StructureTemplate loadFromLine(ConfigLine line) throws InvalidConfigException {
        String structureName = line.toString(CaseStyle.SNAKE);
        StructureTemplate blockStructure = plugin.getStructure(structureName);
        if (blockStructure != null) {
            return blockStructure;
        }

        Offset offset = line.getRequired(Offset.class);
        BlockTemplate template = line.getRequired(BlockTemplate.class);
        Map<Offset, BlockTemplate> blockTemplateByOffset = Map.of(offset, template);

        return new StructureTemplate(plugin, blockTemplateByOffset);
    }

    @Override
    public @NotNull StructureTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Offset offset = section.getRequired(Offset.class);
        BlockTemplate template = section.getRequired(BlockTemplate.class);
        Map<Offset, BlockTemplate> blockTemplateByOffset = Map.of(offset, template);

        if (section instanceof ConfigRoot root) {
            String name = root.getName();
            String group = Group.byStructureFile(root.getFile());

            return new StructureTemplate(plugin, name, group, blockTemplateByOffset);
        }

        return new StructureTemplate(plugin, blockTemplateByOffset);
    }

    @Override
    public @NotNull StructureTemplate loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        Map<Offset, BlockTemplate> blockTemplatesByOffset = new HashMap<>();

        for (ConfigField field : sequence.getNestedFields()) {
            Offset offset = field.getRequired(Offset.class);
            BlockTemplate template = field.getRequired(BlockTemplate.class);
            blockTemplatesByOffset.put(offset, template);
        }

        if (sequence instanceof ConfigRoot root && root.hasFile()) {
            String name = root.getName();
            String group = Group.byStructureFile(root.getFile());
            return new StructureTemplate(plugin, name, group, blockTemplatesByOffset);
        }

        return new StructureTemplate(plugin, blockTemplatesByOffset);
    }

}
