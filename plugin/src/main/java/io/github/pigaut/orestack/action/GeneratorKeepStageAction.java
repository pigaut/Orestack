package io.github.pigaut.orestack.action;

import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.voxel.core.function.action.*;
import io.github.pigaut.voxel.player.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class GeneratorKeepStageAction implements Action {

    @Override
    public void execute(@Nullable PlayerState player, @Nullable Event event, @Nullable Block block, @Nullable Entity target) {
        if (event instanceof GeneratorMineEvent mineEvent) {
            mineEvent.setIdle(true);
        }
    }

}
