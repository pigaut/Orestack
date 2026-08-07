package io.github.pigaut.rpg.module.structure;

import com.google.common.collect.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.structure.global.*;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.global.*;
import io.github.pigaut.rpg.module.structure.virtual.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.module.structure.global.Structure;
import io.github.pigaut.rpg.module.structure.virtual.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class StructureTemplate implements Identifiable, Iterable<BlockTemplate> {

    private final EnhancedPlugin plugin;
    private final String name;
    private final String group;
    private final Map<Offset, BlockTemplate> blockTemplatesByOffset;
    private final Material mostCommonMaterial;

    public StructureTemplate(EnhancedPlugin plugin, Map<Offset, BlockTemplate> blockTemplatesByOffset) {
        this(plugin, UUID.randomUUID().toString(), null, blockTemplatesByOffset);
    }

    public StructureTemplate(EnhancedPlugin plugin, String name, @Nullable String group, Map<Offset, BlockTemplate> blockTemplatesByOffset) {
        this.plugin = plugin;
        this.name = name;
        this.group = group;
        this.blockTemplatesByOffset = blockTemplatesByOffset;
        this.mostCommonMaterial = this.getMostCommonMaterial();
    }

    public static @NotNull StructureTemplate createEmpty(@NotNull EnhancedPlugin plugin) {
        return new StructureTemplate(plugin, Map.of());
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return new IconBuilder()
                .type(mostCommonMaterial)
                .name(CaseFormatter.toTitleCase(this.getName()))
                .buildIcon();
    }

    public int size() {
        return blockTemplatesByOffset.size();
    }

    public boolean hasMultipleBlocks() {
        return blockTemplatesByOffset.size() > 1;
    }

    public @Nullable BlockTemplate getBlockTemplate(int x, int y, int z) {
        return blockTemplatesByOffset.get(new Offset(x, y, z));
    }

    public List<GlobalBlock> createBlocks(@NotNull Location origin, @NotNull Rotation rotation) {
        List<GlobalBlock> realBlocks = new ArrayList<>();
        for (Offset offset : blockTemplatesByOffset.keySet()) {
            BlockTemplate template = blockTemplatesByOffset.get(offset);
            Location offsetLocation = offset.getOffsetLocation(origin, rotation);
            realBlocks.add(new GlobalBlock(template, offsetLocation, rotation));
        }
        return realBlocks;
    }

    public Multimap<ChunkPosition, VirtualBlock> createVirtualBlocks(@NotNull Location origin, @NotNull Rotation rotation) {
        Multimap<ChunkPosition, VirtualBlock> fakeBlocksByChunk = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
        for (Offset offset : blockTemplatesByOffset.keySet()) {
            BlockTemplate template = blockTemplatesByOffset.get(offset);
            if (template instanceof BasicBlockTemplate basicBlockTemplate) {
                Location offsetLocation = offset.getOffsetLocation(origin, rotation);
                ChunkPosition chunkCoords = ChunkPosition.fromLocation(offsetLocation);

                fakeBlocksByChunk.put(chunkCoords, new VirtualBlock(offsetLocation, basicBlockTemplate.createBlockData(rotation)));
            }
        }

        return fakeBlocksByChunk;
    }

    public boolean isPlaced(@NotNull Location origin, @NotNull Rotation rotation) {
        for (BlockTemplate block : blockTemplatesByOffset.values()) {
            if (!block.isPlaced(origin, rotation)) {
                return false;
            }
        }
        return true;
    }

    public @NotNull Structure place(@NotNull Location origin) {
        return place(origin, Rotation.NONE);
    }

    public @NotNull Structure place(@NotNull Location origin, @NotNull Rotation rotation) {
        Structure structure = Server.isFolia() ? new FoliaStructure(plugin, this, origin, rotation)
                : new Structure(this, origin, rotation);
        structure.place();
        return structure;
    }

    public @NotNull VirtualStructure project(@NotNull Location origin, @NotNull Rotation rotation) {
        VirtualStructure virtualStructure = new VirtualStructure(this, origin, rotation);
        plugin.getVirtualStructures().register(virtualStructure);
        return virtualStructure;
    }

    public void remove(@NotNull Location origin, @NotNull Rotation rotation) {
        Structure structure = Server.isFolia() ? new FoliaStructure(plugin, this, origin, rotation)
                : new Structure(this, origin, rotation);
        structure.remove();
    }

    public Set<Location> getOccupiedLocations(@NotNull Location origin, @NotNull Rotation rotation) {
        return blockTemplatesByOffset.keySet().stream()
                .map(offset -> offset.getOffsetLocation(origin, rotation))
                .collect(Collectors.toSet());
    }

    public Set<Block> getOccupiedSolidBlocks(@NotNull Location origin, @NotNull Rotation rotation) {
        Set<Block> solidBlocks = new HashSet<>();
        blockTemplatesByOffset.forEach((offset, blockTemplate) -> {
            if (!(blockTemplate instanceof BasicBlockTemplate basicTemplate) || basicTemplate.getType().isSolid()) {
                solidBlocks.add(offset.getOffsetLocation(origin, rotation).getBlock());
            }
        });
        return solidBlocks;
    }

    public Set<Block> getOccupiedBlocks(@NotNull Location origin) {
        return getOccupiedBlocks(origin, Rotation.NONE);
    }

    public Set<Block> getOccupiedBlocks(@NotNull Location origin, @NotNull Rotation rotation) {
        return blockTemplatesByOffset.keySet().stream()
                .map(offset -> offset.getOffsetLocation(origin, rotation).getBlock())
                .collect(Collectors.toSet());
    }

    public synchronized Material getMostCommonMaterial() {
        if (mostCommonMaterial != null) {
            return mostCommonMaterial;
        }

        final Map<Material, Integer> materialFrequency = new HashMap<>();
        for (BlockTemplate blockChange : blockTemplatesByOffset.values()) {
            if (blockChange instanceof BasicBlockTemplate simpleBlock) {
                Material material = simpleBlock.getType();
                materialFrequency.put(material, materialFrequency.getOrDefault(material, 0) + 1);
            }
        }

        Material mostCommonMaterial = null;
        int maxCount = 0;
        for (Map.Entry<Material, Integer> entry : materialFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommonMaterial = entry.getKey();
            }
        }

        return mostCommonMaterial != null ? mostCommonMaterial : Material.TERRACOTTA;
    }

    @Override
    public @NotNull Iterator<BlockTemplate> iterator() {
        return blockTemplatesByOffset.values().iterator();
    }

}
