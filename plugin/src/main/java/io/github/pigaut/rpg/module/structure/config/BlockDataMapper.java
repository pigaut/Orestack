package io.github.pigaut.rpg.module.structure.config;

import com.nexomc.nexo.api.*;
import com.nexomc.nexo.mechanics.custom_block.*;
import dev.lone.itemsadder.api.CustomBlock;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.configurator.map.*;
import net.momirealms.craftengine.bukkit.api.*;
import net.momirealms.craftengine.core.block.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.*;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.Campfire;
import org.jetbrains.annotations.*;

public class BlockDataMapper implements ConfigMapper<Block> {

    @Override
    public @NotNull FieldType getDefaultMappingType() {
        return FieldType.SECTION;
    }

    @Override
    public void mapToSection(@NotNull ConfigSection section, @NotNull Block block) {
        if (Server.isPluginEnabled("ItemsAdder")) {
            dev.lone.itemsadder.api.CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
            if (customBlock != null) {
                section.set("itemsadder-block|ia-block", customBlock.getId());
                return;
            }
        }

        if (Server.isPluginEnabled("Nexo")) {
            CustomBlockMechanic customBlock = NexoBlocks.customBlockMechanic(block);
            if (customBlock != null) {
                section.set("nexo-block|nx-block", customBlock.getItemID());
                return;
            }
        }

        if (Server.isPluginEnabled("CraftEngine")) {
            ImmutableBlockState blockState = CraftEngineBlocks.getCustomBlockState(block);
            if (blockState != null) {
                BlockStateWrapper blockStateWrapper = blockState.customBlockState();
                section.set("craft-engine-block|craftengine-block|ce-block", blockStateWrapper.ownerId().toString());
                return;
            }
        }

        section.set("block", block.getType());

        BlockData blockData = block.getBlockData();

        // Specific block data
        if (blockData instanceof Stairs stairs) {
            section.set("stair-shape|stairs-shape|stairs", stairs.getShape());
        }

        if (blockData instanceof Slab slab) {
            section.set("slab-type|slab", slab.getType());
        }

        if (blockData instanceof Wall wall) {
            section.set("up", wall.isUp());
            for (BlockFace face : BlockUtil.WALL_FACES) {
                section.set("faces." + face, wall.getHeight(face));
            }
        }

        if (blockData instanceof Door door) {
            section.set("door-hinge|door", door.getHinge());
        }

        if (blockData instanceof Bed bed) {
            section.set("bed-part|bed", bed.getPart());
        }

        if (blockData instanceof Bamboo bamboo) {
            section.set("bamboo-leaves", bamboo.getLeaves());
        }

        if (Server.getVersion() >= Version.V1_17) {
            if (blockData instanceof CaveVinesPlant caveVines) {
                section.set("cave-vines-berries|vines-berries", caveVines.isBerries());
            }
        }

        if (blockData instanceof Beehive beehive) {
            section.set("honey-level|honey", beehive.getHoneyLevel());
        }

        if (blockData instanceof BigDripleaf bigDripleaf) {
            section.set("leaf-tilt", bigDripleaf.getTilt());
        }

        if (blockData instanceof BrewingStand brewingStand) {
            section.set("bottles", brewingStand.getBottles());
        }

        if (blockData instanceof Cake cake) {
            section.set("bites", cake.getBites());
        }

        if (blockData instanceof Campfire campfire) {
            section.set("signal-fire", campfire.isSignalFire());
        }

        // Generic block data
        if (blockData instanceof Ageable ageable) {
            section.set("age", ageable.getAge());
        }

        if (blockData instanceof Directional directional) {
            section.set("direction|face", directional.getFacing());
        } else if (blockData instanceof Rotatable rotatable) {
            section.set("direction|face", rotatable.getRotation());
        }

        if (blockData instanceof MultipleFacing multipleFacing) {
            section.set("directions|faces", multipleFacing.getFaces());
        }

        if (blockData instanceof Orientable orientable) {
            section.set("orientation", orientable.getAxis());
        }

        if (blockData instanceof Openable openable) {
            section.set("open", openable.isOpen());
        }

        if (blockData instanceof Bisected bisected) {
            section.set("half", bisected.getHalf());
        }

        if (blockData instanceof Lightable lightable) {
            section.set("lit", lightable.isLit());
        }

        if (blockData instanceof Brushable brushable) {
            section.set("dusted", brushable.getDusted());
        }

        if (blockData instanceof Waterlogged waterlogged) {
            section.set("waterlogged|in-water", waterlogged.isWaterlogged());
        }

    }

}
