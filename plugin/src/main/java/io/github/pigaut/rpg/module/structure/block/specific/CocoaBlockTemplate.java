package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class CocoaBlockTemplate extends AbstractBlockTemplate<Cocoa> {

    private final int age;
    private final BlockFace direction;

    protected CocoaBlockTemplate(int age, BlockFace direction) {
        super(Material.COCOA, Cocoa.class);
        this.age = age;
        this.direction = direction;
    }

    @Override
    public boolean matchBlockData(@NotNull Cocoa blockData, @NotNull Rotation rotation) {
        return blockData.getAge() == age &&
                blockData.getFacing() == rotation.translateBlockFace(direction);
    }

    @Override
    public void updateBlockData(@NotNull Cocoa blockData, @NotNull Rotation rotation) {
        blockData.setAge(age);
        blockData.setFacing(rotation.translateBlockFace(direction));
    }

}
