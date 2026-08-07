package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ChatMessageLoader implements ConfigLoader<Message> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid chat message";
    }

    @Override
    public @NotNull Message loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return ConfigLoader.super.loadFromSequence(sequence);
    }

    @Override
    public @NotNull Message loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        return ConfigLoader.super.loadFromScalar(scalar);
    }

    @Override
    public @NotNull Message loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        return ConfigLoader.super.loadFromSection(section);
    }
}
