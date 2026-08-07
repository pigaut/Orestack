package io.github.pigaut.rpg.player.input;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public abstract class GenericInputCollector<T> implements InputCollector {

    private final PlayerState playerState;
    private final @NotNull Parser<T> parser;
    private @NotNull Consumer<T> inputCollector = input -> {};
    private @NotNull Runnable cancelAction = () -> {};
    private boolean collecting = false;

    public GenericInputCollector(@NotNull PlayerState player, @NotNull Parser<T> parser) {
        this.playerState = player;
        this.parser = parser;
    }

    @Override
    public @NotNull PlayerState getPlayerState() {
        return playerState;
    }

    public @NotNull Parser<T> getInputParser() {
        return parser;
    }

    @Override
    public boolean isCollecting() {
        return collecting;
    }

    @Override
    public void start() {
        onStart();
        collecting = true;
    }

    @Override
    public void cancel() {
        onCancel();
        cancelAction.run();
        collecting = false;
    }

    public void onSuccess(@NotNull T input) {

    }

    public void onStart() {

    }

    public void onCancel() {

    }

    public void onInputRejected(@NotNull String errorMessage) {

    }

    @Override
    public void accept(@NotNull String input) {
        if (input.equalsIgnoreCase("ESC")) {
            cancel();
            return;
        }

        T parsedInput;
        try {
            parsedInput = parser.parse(input);
        } catch (StringParseException e) {
            onInputRejected(e.getMessage());
            return;
        }

        inputCollector.accept(parsedInput);
        onSuccess(parsedInput);
        collecting = false;
    }

    public GenericInputCollector<T> onInput(@NotNull Consumer<T> inputCollector) {
        this.inputCollector = inputCollector;
        return this;
    }

    public GenericInputCollector<T> onCancel(@NotNull Runnable cancelAction) {
        this.cancelAction = cancelAction;
        return this;
    }

}
