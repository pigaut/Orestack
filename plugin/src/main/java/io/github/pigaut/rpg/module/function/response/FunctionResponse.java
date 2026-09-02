package io.github.pigaut.rpg.module.function.response;

public interface FunctionResponse {

    FunctionResponse NONE = () -> ResponseType.NONE;
    FunctionResponse MET = () -> ResponseType.MET;
    FunctionResponse UNMET = () -> ResponseType.UNMET;
    FunctionResponse RETURN = () -> ResponseType.RETURN;
    FunctionResponse CONTINUE = () -> ResponseType.CONTINUE;
    FunctionResponse STOP = () -> ResponseType.STOP;
    FunctionResponse ERROR = () -> ResponseType.ERROR;

    static FunctionResponse met(boolean met) {
        return met ? MET : UNMET;
    }

    ResponseType getType();

}
