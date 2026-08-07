package io.github.pigaut.rpg.module.function.response;

public interface FunctionResponse {

    FunctionResponse NONE = () -> ResponseType.NONE;
    FunctionResponse RETURN = () -> ResponseType.RETURN;
    FunctionResponse CONTINUE = () -> ResponseType.CONTINUE;
    FunctionResponse STOP = () -> ResponseType.STOP;

    ResponseType getType();

}
