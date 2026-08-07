package io.github.pigaut.rpg.player.input;

import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public interface InputCollector {

    @NotNull InputSource getInputSource();

    @NotNull PlayerState getPlayerState();

    void start();

    void cancel();

    boolean isCollecting();

    void accept(@NotNull String input);

}
