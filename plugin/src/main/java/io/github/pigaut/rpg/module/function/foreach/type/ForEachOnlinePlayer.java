package io.github.pigaut.rpg.module.function.foreach.type;

import com.google.common.collect.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ForEachOnlinePlayer implements ForEachSource<Player> {

    @Override
    public @NotNull List<Player> getElements(@NotNull Context context) {
        return ImmutableList.copyOf(Bukkit.getOnlinePlayers());
    }

}
