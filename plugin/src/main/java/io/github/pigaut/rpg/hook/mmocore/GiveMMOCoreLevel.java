package io.github.pigaut.rpg.hook.mmocore;

import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.yaml.amount.*;
import net.Indyuce.mmocore.api.player.*;
import net.Indyuce.mmocore.experience.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class GiveMMOCoreLevel implements PlayerAction {

    private final Amount levels;

    public GiveMMOCoreLevel(Amount levels) {
        this.levels = levels;
    }

    @Override
    public void execute(@NotNull Player player) {
        PlayerData mmoCorePlayer = PlayerData.get(player);
        mmoCorePlayer.giveLevels(levels.intValue(), EXPSource.OTHER);
    }

}
