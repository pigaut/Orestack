package io.github.pigaut.rpg.module.function.response;

import org.jetbrains.annotations.*;

public class YieldValueResponse implements FunctionResponse {

    private final Object value;

    public YieldValueResponse(@Nullable Object value) {
        this.value = value;
    }

    @Override
    public ResponseType getType() {
        return ResponseType.YIELD;
    }

    public @Nullable Object getValue() {
        return value;
    }

}
