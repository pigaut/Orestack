package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.option.*;
import io.github.pigaut.rpg.module.message.type.*;
import io.github.pigaut.rpg.module.message.type.chat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

public class MessageLoader implements ConfigLoader<Message> {

    private final EnhancedPlugin plugin;

    public MessageLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid message";
    }

    @Override
    public @NotNull Message loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String messageName = scalar.toString(CaseStyle.SNAKE);
        Message message = plugin.getMessage(messageName);
        if (message != null) {
            return message;
        }
        return scalar.getRequired(ChatMessage.class);
    }

    @Override
    public @NotNull Message loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        String messageName = sequence.getKey();
        String messageGroup = Group.byMessageFile(sequence.getRoot().getFile());
        return new MultiMessage(messageName, messageGroup, sequence.getAllRequired(Message.class));
    }

    @Override
    public @NotNull Message loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Message message;
        if (section.isSet("message|messages|chat")) {
            message = section.getRequired(ChatMessage.class);
        }
        else if (section.isSet("actionbar|action-bar")) {
            message = section.getRequired(ActionBarMessage.class);
        }
        else if (section.isSet("title")) {
            message = section.getRequired(TitleMessage.class);
        }
        else if (section.isSet("bossbar|boss-bar")) {
            message = section.getRequired(BossBarMessage.class);
        }
        else if (section.isSet("hologram")) {
            message = section.getRequired(HologramMessage.class);
        }
        else {
            throw new InvalidConfigException(section, "Could not determine message type");
        }

        Integer repetitions = section.getInteger("repeat|repetitions")
                .require(Requirements.positive())
                .withDefault(null);

        Integer interval = section.get("interval|period", Delay.class)
                .check(repetitions != null, "repetitions must be set to use interval delay")
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (interval != null) {
            message = new PeriodicMessage(plugin, message, interval, repetitions);
        }
        else if (repetitions != null) {
            message = new RepeatedMessage(message, repetitions);
        }

        Integer delay = section.get("delay", Delay.class)
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (delay != null) {
            message = new DelayedMessage(plugin, message, delay);
        }

        return message;
    }

}
