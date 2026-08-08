package io.github.pigaut.rpg.module.structure.block.matcher;

import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public interface BlockMatcher {

    BlockMatcher INVALID = block -> false;

    boolean matchBlock(@NotNull Block block);

}
