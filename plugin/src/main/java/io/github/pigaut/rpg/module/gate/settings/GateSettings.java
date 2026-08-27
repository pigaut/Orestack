package io.github.pigaut.rpg.module.gate.settings;

import io.github.pigaut.rpg.module.gate.template.*;
import org.jetbrains.annotations.*;

public interface GateSettings {

    int getGateClickCooldown();

    boolean isPlayerConstruction(@NotNull GateTemplate construction);

}
