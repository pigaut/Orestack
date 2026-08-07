package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class CandleBlockTemplate extends AbstractBlockTemplate<Candle> {

    private final int candles;
    private final boolean lit;

    public CandleBlockTemplate(@NotNull Material type, int candles, boolean lit) {
        super(type, Candle.class);
        this.candles = candles;
        this.lit = lit;
    }

    @Override
    public boolean matchBlockData(@NotNull Candle blockData, @NotNull Rotation rotation) {
        return blockData.getCandles() == candles &&
                blockData.isLit() == lit;
    }

    @Override
    public void updateBlockData(@NotNull Candle blockData, @NotNull Rotation rotation) {
        blockData.setCandles(candles);
        blockData.setLit(lit);
    }

}
