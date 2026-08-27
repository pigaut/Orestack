package io.github.pigaut.rpg.module.function.response;

import org.jetbrains.annotations.*;

public class FunctionError implements FunctionResponse {

    private final String details;

    public FunctionError(@NotNull String details) {
        this.details = details;
    }

    @Override
    public ResponseType getType() {
        return ResponseType.STOP;
    }

    public @NotNull String getDetails() {
        return details;
    }

}
