package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class HangingMossBlockTemplate extends AbstractBlockTemplate<HangingMoss> {

    private final boolean tip;

    public HangingMossBlockTemplate(@NotNull Material type, boolean tip) {
        super(type, HangingMoss.class);
        this.tip = tip;
    }

    @Override
    public boolean matchBlockData(@NotNull HangingMoss blockData, @NotNull Rotation rotation) {
        return blockData.isTip() == tip;
    }

    @Override
    public void updateBlockData(@NotNull HangingMoss blockData, @NotNull Rotation rotation) {
        blockData.setTip(tip);
    }
}
