package io.github.pigaut.rpg.plugin.manager;

import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public interface Toggleable {

    @NotNull
    Module getModule();

}
