package io.github.pigaut.rpg.module.gate.settings;

import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

public interface GateSettings {

    @NotNull
    Delay getGateClickCooldown();

    boolean isPlayerConstruction(@NotNull GateTemplate construction);

}
