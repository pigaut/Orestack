package io.github.pigaut.rpg.hook.mmocore;

import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.yaml.amount.*;
import net.Indyuce.mmocore.api.player.*;
import net.Indyuce.mmocore.experience.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class GiveMMOCoreExp implements PlayerAction {

    private final Amount exp;
    private final boolean splitExp;

    public GiveMMOCoreExp(Amount exp, boolean splitExp) {
        this.exp = exp;
        this.splitExp = splitExp;
    }

    @Override
    public void execute(@NotNull Player player) {
        PlayerData mmoCorePlayer = PlayerData.get(player);
        mmoCorePlayer.giveExperience(exp.intValue(), EXPSource.OTHER, null, splitExp);
    }

}
