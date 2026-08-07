package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class BambooBlockTemplate extends AbstractBlockTemplate<Bamboo> {

    private final int age;
    private final Bamboo.Leaves bambooLeaves;

    public BambooBlockTemplate(@NotNull Material type, int age, Bamboo.Leaves bambooLeaves) {
        super(type, Bamboo.class);
        this.age = age;
        this.bambooLeaves = bambooLeaves;
    }

    @Override
    public boolean matchBlockData(@NotNull Bamboo blockData, @NotNull Rotation rotation) {
        return blockData.getAge() == age &&
                blockData.getLeaves() == bambooLeaves;
    }

    @Override
    public void updateBlockData(@NotNull Bamboo blockData, @NotNull Rotation rotation) {
        blockData.setAge(age);
        blockData.setLeaves(bambooLeaves);
    }
}
