package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class GenericMessageLoader extends AbstractLoader<Message> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid message";
    }

    public GenericMessageLoader() {

    }

    @Override
    public @NotNull Message loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byMessageFile(section.getRoot().getFile());

        String type;
        if (section.isScalar("message|messages|chat")) {
            type = "CHAT";
        }
        else if (section.isSet("actionbar|action-bar")) {

        }


        return super.loadFromSection(section);
    }

    @Override
    public @NotNull Message loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return super.loadFromSequence(sequence);
    }

    @Override
    public @NotNull Message loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        return super.loadFromScalar(scalar);
    }
}
