package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class AgeableBlockTemplate extends AbstractBlockTemplate<Ageable> {

    private final int age;

    public AgeableBlockTemplate(Material type, int age) {
        super(type, Ageable.class);
        this.age = age;
    }

    @Override
    public boolean matchBlockData(@NotNull Ageable blockData, @NotNull Rotation rotation) {
        return blockData.getAge() == age;
    }

    @Override
    public void updateBlockData(@NotNull Ageable blockData, @NotNull Rotation rotation) {
        blockData.setAge(age);
    }

}
