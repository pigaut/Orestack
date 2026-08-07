package io.github.pigaut.rpg.core.command.execution;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;

public interface PlayerStateExecution {

    void execute(PlayerState playerState, Context context, String[] args);

}
