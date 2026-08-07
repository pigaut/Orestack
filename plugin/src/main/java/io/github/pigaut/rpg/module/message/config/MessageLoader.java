package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.impl.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.impl.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import net.md_5.bungee.api.chat.*;
import org.bukkit.boss.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.regex.*;

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
        String group = Group.byMessageFile(scalar.getRoot().getFile());
        return new ChatMessage(scalar.getKey(), group, List.of(scalar.toString(ColorUtil.FORMATTER)));
    }

    @Override
    public @NotNull Message loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byMessageFile(section.getRoot().getFile());

        Message message;
        if (section.isScalar("message|messages|chat")) {
            String chatLine = section.getRequiredString("message|messages|chat", ColorUtil.FORMATTER);
            if (section.size() == 1) {
                message = new ChatMessage(name, group, chatLine);
            }
            else {
                Map<String, TextComponent> componentsByName = new HashMap<>();
                for (String key : section.getKeys()) {
                    if (StringUtil.isAnyEqualIgnoreCase(key, "message", "messages", "chat")) {
                        continue;
                    }
                    TextComponent component = section.getRequired(key, TextComponent.class);
                    componentsByName.put(key.toLowerCase(), component);
                }

                TextComponent parsedMessage = parseMessageComponents(chatLine, componentsByName);
                message = new InteractiveChatMessage(name, group, parsedMessage);
            }
        }
        else if (section.isSequence("message|messages|chat")) {
            List<String> chatLines = section.getStringList("message|messages|chat", ColorUtil.FORMATTER)
                    .require(Requirements.minSize(1), "Chat message must have at least one line")
                    .orThrow();

            if (section.size() == 1) {
                message = new ChatMessage(name, group, chatLines);
            }
            else {
                Map<String, TextComponent> componentsByName = new HashMap<>();
                for (String key : section.getKeys()) {
                    if (StringUtil.isAnyEqualIgnoreCase(key, "message", "messages", "chat")) {
                        continue;
                    }
                    TextComponent component = section.getRequired(key, TextComponent.class);
                    componentsByName.put(key.toLowerCase(), component);
                }

                List<TextComponent> parsedMessages = new ArrayList<>();
                for (String chatLine : chatLines) {
                    parsedMessages.add(parseMessageComponents(chatLine, componentsByName));
                }

                message = new InteractiveChatMessage(name, group, parsedMessages);
            }
        }
        else if (section.isSet("actionbar|action-bar")) {
            String actionbar = section.getRequiredString("actionbar|action-bar", ColorUtil.FORMATTER);
            BarAlignment statusBarAlign = section.get("align", BarAlignment.class).withDefault(null);
            message = new ActionBarMessage(plugin, name, group, actionbar, statusBarAlign);
        }
        else if (section.isSet("title")) {
            message = new TitleMessage(name, group,
                    section.getRequiredString("title", ColorUtil.FORMATTER),
                    section.getString("subtitle", ColorUtil.FORMATTER).withDefault(""),
                    section.getInteger("fade-in").withDefault(10),
                    section.getInteger("stay").withDefault(70),
                    section.getInteger("fade-out").withDefault(20)
            );
        }
        else if (section.isSet("bossbar|boss-bar")) {
            message = new BossBarMessage(plugin, name, group,
                    section.getRequiredString("bossbar|boss-bar", ColorUtil.FORMATTER),
                    section.get("style", BarStyle.class).withDefault(BarStyle.SOLID),
                    section.get("color", BarColor.class).withDefault(BarColor.RED),
                    section.get("duration", Delay.class).map(Delay::toTicks).withDefault(100),
                    section.getDoubleList("progress").orEmpty()
            );
        }
        else if (section.isSet("hologram")) {
            message = new HologramMessage(plugin, name, group,
                    section.getRequired("hologram", HologramTemplate.class),
                    section.getInteger("duration").withDefault(40),
                    section.getDouble("range|radius.x").withDefault(null),
                    section.getDouble("range|radius.y").withDefault(null),
                    section.getDouble("range|radius.z").withDefault(null)
            );
        }
        else {
            throw new InvalidConfigException(section, "Could not determine message type");
        }

        Integer repetitions = section.getInteger("repeat|repetitions")
                .require(Requirements.positive())
                .withDefault(null);

        Integer interval = section.get("interval|period", Delay.class)
                .check(repetitions != null, "repetitions must be set to use interval delay")
                .map(Delay::toTicks)
                .withDefault(null);

        if (interval != null) {
            message = new PeriodicMessage(plugin, message, interval, repetitions);
        }
        else if (repetitions != null) {
            message = new RepeatedMessage(message, repetitions);
        }

        Integer delay = section.get("delay", Delay.class)
                .map(Delay::toTicks)
                .withDefault(null);

        if (delay != null) {
            message = new DelayedMessage(plugin, message, delay);
        }

        return message;
    }

    @Override
    public @NotNull Message loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        String messageName = sequence.getKey();
        String messageGroup = Group.byMessageFile(sequence.getRoot().getFile());
        return new MultiMessage(messageName, messageGroup, sequence, sequence.getAllRequired(Message.class));
    }

    // To be moved to separate loader
    private static final Pattern COMPONENT_TAG = Pattern.compile("<(\\w+)>");

    private static @NotNull TextComponent parseMessageComponents(@NotNull String message, @NotNull Map<String, TextComponent> componentsByName) {
        TextComponent root = new TextComponent();
        Matcher matcher = COMPONENT_TAG.matcher(message);

        StringBuilder literal = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            String tagName = matcher.group(1).toLowerCase();
            TextComponent replacement = componentsByName.get(tagName);

            if (replacement == null) {
                continue;
            }

            literal.append(message, lastEnd, matcher.start());
            flushLiteral(root, literal);

            root.addExtra(replacement.duplicate());
            lastEnd = matcher.end();
        }

        literal.append(message.substring(lastEnd));
        flushLiteral(root, literal);

        return root;
    }

    private static void flushLiteral(@NotNull TextComponent root, @NotNull StringBuilder literal) {
        if (literal.length() > 0) {
            for (BaseComponent part : TextComponent.fromLegacyText(literal.toString())) {
                root.addExtra(part);
            }
            literal.setLength(0);
        }
    }

}
