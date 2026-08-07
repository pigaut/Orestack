package io.github.pigaut.rpg.module.message;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;

public class MessageManager extends ConfigBackedManager<Message> {

    public MessageManager(EnhancedJavaPlugin plugin) {
        super(plugin, Module.MESSAGES, Message.class);
    }

}
