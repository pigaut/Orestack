package io.github.pigaut.rpg.module.structure.config;

import com.nexomc.nexo.api.*;
import dev.lone.itemsadder.api.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.generic.*;
import io.github.pigaut.rpg.module.structure.block.specific.*;
import io.github.pigaut.rpg.hook.craftengine.*;
import io.github.pigaut.rpg.hook.itemsadder.*;
import io.github.pigaut.rpg.hook.nexo.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import net.momirealms.craftengine.core.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.*;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.Campfire;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockTemplateLoader implements ConfigLoader.Line<BlockTemplate> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid block";
    }

    @Override
    public @NotNull BlockTemplate loadFromLine(ConfigLine line) throws InvalidConfigException {
        if (line.hasFlag("itemsAdderBlock|iaBlock")) {
            if (!Server.isPluginEnabled("ItemsAdder")) {
                line.collectWarning(line, "ItemsAdder plugin is not installed");
                return BlockTemplate.INVALID;
            }
            CustomBlock customBlock = line.getRequired("itemsAdderBlock|iaBlock", CustomBlock.class);
            return new ItemsAdderBlockTemplate(customBlock);
        }

        if (line.hasFlag("nexoBlock|nxBlock")) {
            if (!Server.isPluginEnabled("Nexo")) {
                line.collectWarning(line, "Nexo plugin is not installed");
                return BlockTemplate.INVALID;
            }
            String blockId = line.getRequiredString("nexoBlock|nxBlock");
            if (!NexoBlocks.isCustomBlock(blockId)) {
                throw new InvalidConfigException(line, "nexoBlock", "Could not find nexo block with name: '" + blockId + "'");
            }
            return new NexoBlockTemplate(blockId);
        }

        if (line.hasFlag("craftEngineBlock|ceBlock")) {
            if (!Server.isPluginEnabled("CraftEngine")) {
                line.collectWarning(line, "CraftEngine plugin is not installed");
                return BlockTemplate.INVALID;
            }
            BlockDefinition customBlock = line.getRequired("craftEngineBlock|ceBlock", BlockDefinition.class);
            return new CraftEngineBlockTemplate(customBlock);
        }

        Material type = line.getRequired(0, Material.class);
        BlockData blockData = type.createBlockData();

        BlockFace facing = null;
        if (blockData instanceof Directional directional) {
            facing = line.get("direction|facing", BlockFace.class)
                    .require(directional.getFaces()::contains)
                    .withDefault(directional.getFaces().iterator().next());
        }

        Bisected.Half half = null;
        if (blockData instanceof Bisected) {
            half = line.get("half", Bisected.Half.class).withDefault(Bisected.Half.TOP);
        }

        boolean open = false;
        if (blockData instanceof Openable) {
            open = line.getBoolean("open").withDefault(false);
        }

        if (blockData instanceof Stairs) {
            Stairs.Shape stairShape = line.get("stairShape|stairsShape|stairs", Stairs.Shape.class).withDefault(null);
            return new StairsBlockTemplate(type, facing, half, stairShape);
        }

        if (blockData instanceof Slab) {
            Slab.Type slabType = line.get("slabType|slab", Slab.Type.class).withDefault(Slab.Type.BOTTOM);
            return new SlabBlockTemplate(type, slabType);
        }

        if (blockData instanceof Wall) {
            boolean up = line.getBoolean("up").withDefault(false);
            Map<BlockFace, Wall.Height> heightByFace = new HashMap<>();
            for (BlockFace face : BlockUtil.WALL_FACES) {
                String faceName = CaseFormatter.toCamelCase(face.toString());
                heightByFace.put(face, line.getRequired(faceName, Wall.Height.class));
            }
            return new WallBlockTemplate(type, up, heightByFace);
        }

        if (blockData instanceof Door) {
            Door.Hinge doorHinge = line.get("doorHinge|door", Door.Hinge.class).withDefault(Door.Hinge.LEFT);
            return new DoorBlockTemplate(type, half, facing, doorHinge, open);
        }

        if (blockData instanceof Bed) {
            Bed.Part bedPart = line.get("bedPart|bed", Bed.Part.class).withDefault(null);
            return new BedBlockTemplate(type, facing, bedPart);
        }

        if (blockData instanceof Bamboo bamboo) {
            int maxAge = bamboo.getMaximumAge();
            int age = line.getInteger("age")
                    .require(Requirements.between(0, maxAge))
                    .withDefault(maxAge);
            Bamboo.Leaves bambooLeaves = line.get("bambooLeaves", Bamboo.Leaves.class).withDefault(Bamboo.Leaves.NONE);
            return new BambooBlockTemplate(type, age, bambooLeaves);
        }

        if (blockData instanceof Beehive beehive) {
            int honeyLevel = line.getInteger("honeyLevel|honey")
                    .require(Requirements.between(0, beehive.getMaximumHoneyLevel()))
                    .withDefault(beehive.getMaximumHoneyLevel());

            return new BeehiveBlockTemplate(type, honeyLevel);
        }

        if (blockData instanceof Cake cake) {
            int bites = line.getInteger("bites")
                    .require(Requirements.between(0, cake.getMaximumBites()))
                    .withDefault(0);

            return new CakeBlockTemplate(type, bites);
        }

        if (blockData instanceof Campfire) {
            boolean lit = line.getBoolean("lit").withDefault(true);
            boolean signalFire = line.getBoolean("signalFire").withDefault(false);
            return new CampfireBlockTemplate(type, facing, lit, signalFire);
        }

        if (Server.getVersion() >= Version.V1_17) {
            if (blockData instanceof CaveVines) {
                boolean caveVinesBerries = line.getBoolean("berries|caveVinesBerries|vinesBerries").withDefault(false);
                return new CaveVinesBlockTemplate(type, caveVinesBerries);
            }

            if (blockData instanceof CaveVinesPlant) {
                boolean caveVinesBerries = line.getBoolean("berries|caveVinesBerries|vinesBerries").withDefault(false);
                return new CaveVinesPlantBlockTemplate(type, caveVinesBerries);
            }

            if (blockData instanceof BigDripleaf) {
                BigDripleaf.Tilt tilt = line.get("leafTilt", BigDripleaf.Tilt.class).withDefault(BigDripleaf.Tilt.NONE);
                return new BigDripLeafBlockTemplate(type, facing, tilt);
            }
        }

        if (Server.getVersion() >= Version.V1_20) {
            if (blockData instanceof Brushable brushable) {
                int dusted = line.getInteger("dusted")
                        .require(Requirements.between(0, brushable.getMaximumDusted()))
                        .withDefault(brushable.getDusted());

                return new BrushableBlockTemplate(type, dusted);
            }
        }

        // Generic block data fallback
        if (blockData instanceof Ageable ageable) {
            int age = line.getInteger("age").withDefault(ageable.getMaximumAge());
            return new AgeableBlockTemplate(type, age);
        }

        if (blockData instanceof Directional) {
            return new DirectionalBlockTemplate(type, facing);
        }

        if (blockData instanceof Orientable orientable) {
            Axis axis = line.get("orientation|axis", Axis.class)
                    .require(orientable.getAxes()::contains)
                    .withDefault(orientable.getAxes().iterator().next());

            return new OrientableBlockTemplate(type, axis);
        }

        if (blockData instanceof Openable) {
            return new OpenableBlockTemplate(type, open);
        }

        if (blockData instanceof Bisected) {
            return new BisectedBlockTemplate(type, half);
        }

        if (blockData instanceof Lightable) {
            boolean lit = line.getBoolean("lit").withDefault(true);
            return new LightableBlockTemplate(type, lit);
        }

        if (blockData instanceof Waterlogged) {
            boolean waterlogged = line.getBoolean("waterlogged|inWater").withDefault(false);
            return new WaterloggedBlockTemplate(type, waterlogged);
        }

        return new BasicBlockTemplate(type);
    }

    @Override
    public @NotNull BlockTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (section.isSet("items-adder-block|itemsadder-block|ia-block")) {
            if (!Server.isPluginEnabled("ItemsAdder")) {
                throw new InvalidConfigException(section, "items-adder-block", "ItemsAdder is not loaded/enabled");
            }
            CustomBlock customBlock =
                    section.getRequired("items-adder-block|itemsadder-block|ia-block", CustomBlock.class);
            return new ItemsAdderBlockTemplate(customBlock);
        }

        if (section.isSet("nexo-block|nx-block")) {
            if (!Server.isPluginEnabled("Nexo")) {
                throw new InvalidConfigException(section, "nexo-block", "Nexo is not loaded/enabled");
            }
            String blockId = section.getRequiredString("nexo-block|nx-block");
            if (!NexoBlocks.isCustomBlock(blockId)) {
                throw new InvalidConfigException(section, "nexo-block", "Could not find nexo block with name: '" + blockId + "'");
            }
            return new NexoBlockTemplate(blockId);
        }

        if (section.isSet("craft-engine-block|craftengine-block|ce-block")) {
            if (!Server.isPluginEnabled("CraftEngine")) {
                throw new InvalidConfigException(section, "craft-engine-block", "CraftEngine is not loaded/enabled");
            }
            BlockDefinition customBlock =
                    section.getRequired("craft-engine-block|craftengine-block|ce-block", BlockDefinition.class);
            return new CraftEngineBlockTemplate(customBlock);
        }

        Material type = section.getRequired("block", Material.class);
        BlockData blockData = type.createBlockData();

        BlockFace facing = null;
        if (blockData instanceof Directional directional) {
            facing = section.get("direction|facing", BlockFace.class)
                    .require(directional.getFaces()::contains)
                    .withDefault(directional.getFacing());
        }

        Bisected.Half half = null;
        if (blockData instanceof Bisected) {
            half = section.get("half", Bisected.Half.class).withDefault(Bisected.Half.TOP);
        }

        boolean open = false;
        if (blockData instanceof Openable) {
            open = section.getBoolean("open").withDefault(false);
        }

        if (blockData instanceof Stairs) {
            Stairs.Shape stairShape = section.get("stair-shape|stairs-shape|stairs", Stairs.Shape.class).withDefault(null);
            return new StairsBlockTemplate(type, facing, half, stairShape);
        }

        if (blockData instanceof Slab) {
            Slab.Type slabType = section.get("slab-type|slab", Slab.Type.class).withDefault(Slab.Type.BOTTOM);
            return new SlabBlockTemplate(type, slabType);
        }

        if (blockData instanceof Wall) {
            boolean up = section.getBoolean("up").withDefault(false);
            Map<BlockFace, Wall.Height> heightByFace = new HashMap<>();
            for (BlockFace face : BlockUtil.WALL_FACES) {
                Wall.Height height = section.get("faces." + face, Wall.Height.class).withDefault(Wall.Height.NONE);
                heightByFace.put(face, height);
            }
            return new WallBlockTemplate(type, up, heightByFace);
        }

        if (blockData instanceof Door) {
            Door.Hinge doorHinge = section.get("door-hinge|door", Door.Hinge.class).withDefault(Door.Hinge.LEFT);
            return new DoorBlockTemplate(type, half, facing, doorHinge, open);
        }

        if (blockData instanceof Bed) {
            Bed.Part bedPart = section.get("bed-part|bed", Bed.Part.class).withDefault(null);
            return new BedBlockTemplate(type, facing, bedPart);
        }

        if (blockData instanceof Bamboo bamboo) {
            int age = section.getInteger("age")
                    .require(Requirements.between(0, bamboo.getMaximumAge()))
                    .withDefault(bamboo.getMaximumAge());
            Bamboo.Leaves bambooLeaves = section.get("bamboo-leaves", Bamboo.Leaves.class).withDefault(Bamboo.Leaves.NONE);
            return new BambooBlockTemplate(type, age, bambooLeaves);
        }

        if (blockData instanceof Beehive beehive) {
            int honeyLevel = section.getInteger("honey-level|honey")
                    .require(Requirements.between(0, beehive.getMaximumHoneyLevel()))
                    .withDefault(beehive.getMaximumHoneyLevel());

            return new BeehiveBlockTemplate(type, honeyLevel);
        }

        if (blockData instanceof BrewingStand brewingStand) {
            List<Integer> bottles = section.getIntegerList("bottles")
                    .requireEach(Requirements.between(0, brewingStand.getMaximumBottles()))
                    .orEmpty();

            return new BrewingStandBlockTemplate(type, new HashSet<>(bottles));
        }

        if (blockData instanceof Cake cake) {
            int bites = section.getInteger("bites")
                    .require(Requirements.between(0, cake.getMaximumBites()))
                    .withDefault(cake.getBites());

            return new CakeBlockTemplate(type, bites);
        }

        if (blockData instanceof Campfire) {
            boolean lit = section.getBoolean("lit").withDefault(true);
            boolean signalFire = section.getBoolean("signal-fire").withDefault(false);
            return new CampfireBlockTemplate(type, facing, lit, signalFire);
        }

        if (Server.getVersion() >= Version.V1_17) {
            if (blockData instanceof CaveVines) {
                boolean caveVinesBerries = section.getBoolean("berries|cav-vines-berries|vines-berries").withDefault(false);
                return new CaveVinesBlockTemplate(type, caveVinesBerries);
            }

            if (blockData instanceof CaveVinesPlant) {
                boolean caveVinesBerries = section.getBoolean("berries|cave-vines-berries|vines-berries").withDefault(false);
                return new CaveVinesPlantBlockTemplate(type, caveVinesBerries);
            }

            if (blockData instanceof BigDripleaf) {
                BigDripleaf.Tilt tilt = section.get("leaf-tilt", BigDripleaf.Tilt.class).withDefault(BigDripleaf.Tilt.NONE);
                return new BigDripLeafBlockTemplate(type, facing, tilt);
            }
        }

        if (Server.getVersion() >= Version.V1_20) {
            if (blockData instanceof Brushable brushable) {
                int dusted = section.getInteger("dusted")
                        .require(Requirements.between(0, brushable.getMaximumDusted()))
                        .withDefault(brushable.getDusted());

                return new BrushableBlockTemplate(type, dusted);
            }
        }

        // Generic block data fallback
        if (blockData instanceof Ageable ageable) {
            int age = section.getInteger("age").withDefault(ageable.getMaximumAge());
            return new AgeableBlockTemplate(type, age);
        }

        if (blockData instanceof Directional) {
            return new DirectionalBlockTemplate(type, facing);
        }

        if (blockData instanceof Rotatable rotatable) {
            BlockFace rotation = section.get("direction|facing", BlockFace.class)
                    .withDefault(rotatable.getRotation());
            return new RotatableBlockTemplate(type, rotation);
        }

        if (blockData instanceof MultipleFacing multipleFacing) {
            List<BlockFace> facingDirections = section.getList("directions|faces", BlockFace.class)
                    .requireEach(multipleFacing.getAllowedFaces()::contains)
                    .orEmpty();

            return new MultipleFacingBlockTemplate(type, new HashSet<>(facingDirections));
        }

        if (blockData instanceof Orientable orientable) {
            Axis axis = section.get("orientation|axis", Axis.class)
                    .require(orientable.getAxes()::contains)
                    .withDefault(orientable.getAxis());

            return new OrientableBlockTemplate(type, axis);
        }

        if (blockData instanceof Openable) {
            return new OpenableBlockTemplate(type, open);
        }

        if (blockData instanceof Bisected) {
            return new BisectedBlockTemplate(type, half);
        }

        if (blockData instanceof Lightable) {
            boolean lit = section.getBoolean("lit").withDefault(true);
            return new LightableBlockTemplate(type, lit);
        }

        if (blockData instanceof Waterlogged) {
            boolean waterlogged = section.getBoolean("waterlogged|in-water").withDefault(false);
            return new WaterloggedBlockTemplate(type, waterlogged);
        }

        return new BasicBlockTemplate(type);
    }

}
