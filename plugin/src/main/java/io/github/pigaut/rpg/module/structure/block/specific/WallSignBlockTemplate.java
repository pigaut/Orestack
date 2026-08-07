package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.yaml.util.Preconditions;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.Sign;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class WallSignBlockTemplate extends AbstractBlockTemplate<WallSign> {

    private final BlockFace facing;
    private final List<String> lines;

    public WallSignBlockTemplate(@NotNull Material type, BlockFace facing, List<String> lines) {
        super(type, WallSign.class);
        this.facing = facing;
        this.lines = lines;
    }

    @Override
    public boolean matchBlockData(@NotNull WallSign blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() != rotation.translateBlockFace(facing);
    }

    @Override
    public void updateBlockData(@NotNull WallSign blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
    }

    @Override
    public boolean hasBlockState() {
        return true;
    }

    @Override
    public boolean matchBlockState(@NotNull BlockState blockState) {
        if (!(blockState instanceof Sign sign)) {
            return false;
        }

        for (int i = 0; i < 4; i++) {
            String expected = (lines != null && lines.size() > i) ? lines.get(i) : "";
            String actual = sign.getLine(i);

            if (!expected.equals(actual)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void updateBlockState(@NotNull BlockState blockState) {
        if (blockState instanceof Sign sign) {
            for (int i = 0; i < 4; i++) {
                String line = (lines != null && lines.size() > i) ? lines.get(i) : "";
                sign.setLine(i, line);
            }

            sign.update(true);
        }
    }

    public static @NotNull Builder builder() {
        return new Builder(Material.OAK_WALL_SIGN);
    }

    public static class Builder {

        private Material type;
        private WallSign blockData;
        private BlockFace facing;
        private final List<String> lines = new ArrayList<>();

        public Builder(@NotNull Material type) {
            BlockData blockData = type.createBlockData();
            if (!(blockData instanceof WallSign wallSign)) {
                throw new IllegalArgumentException("Type must be a wall sign");
            }
            this.type = type;
            this.blockData = wallSign;
            this.facing = wallSign.getFacing();
        }

        public Builder type(@NotNull Material type) {
            BlockData blockData = type.createBlockData();
            if (!(blockData instanceof WallSign wallSign)) {
                throw new IllegalArgumentException("Type must be a wall sign");
            }
            this.type = type;
            this.blockData = wallSign;
            return this;
        }

        public Builder facing(@NotNull BlockFace facing) {
            Preconditions.checkArgument(blockData.getFaces().contains(facing), "Unsupported facing direction");
            this.facing = facing;
            return this;
        }

        public Builder addLine(@NotNull String line) {
            Preconditions.checkArgument(lines.size() < 4, "Wall sign supports 4 lines max");
            lines.add(ColorUtil.parseAll(line));
            return this;
        }

        public Builder lines(@NotNull String... lines) {
            this.lines.clear();
            for (int i = 0; i < 4; i++) {
                addLine(lines.length > i ? lines[i] : "");
            }
            return this;
        }

        public WallSignBlockTemplate build() {
            return new WallSignBlockTemplate(type, facing, lines);
        }

    }

}
