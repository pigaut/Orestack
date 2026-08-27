package io.github.pigaut.rpg.module.message.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.message.type.chat.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import net.md_5.bungee.api.chat.*;
import org.jetbrains.annotations.*;

import javax.swing.text.*;
import java.util.*;
import java.util.regex.*;

public class ChatMessageLoader implements ConfigLoader<ChatMessage> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid chat message";
    }

    @Override
    public @NotNull ChatMessage loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String group = Group.byMessageFile(scalar.getRoot().getFile());
        return new SimpleChatMessage(scalar.getKey(), group, scalar.toString());
    }

    @Override
    public @NotNull ChatMessage loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byMessageFile(section.getRoot().getFile());

        List<String> chatLines;
        if (section.isScalar("message|messages|chat")) {
            chatLines = List.of(section.getRequiredString("message|messages|chat"));
        }
        else if (section.isSequence("message|messages|chat")) {
            chatLines = section.getStringList("message|messages|chat")
                    .require(Requirements.minSize(1), "Chat message must have at least one line")
                    .orThrow();
        } else {
            throw new InvalidConfigException(section, "Could not find valid chat message");
        }

        if (section.size() == 1) {
            if (chatLines.size() == 1) {
                return new SimpleChatMessage(name, group, chatLines.get(0));
            }
            return new SimpleChatMessage(name, group, chatLines);
        }

        Map<String, TextComponent> componentsByName = new HashMap<>();
        for (String key : section.getKeys()) {
            if (StringUtil.isAnyEqualIgnoreCase(key, "message", "messages", "chat")) {
                continue;
            }

            if (StringUtil.isParenthesized(key, "<", ">")) {
                throw new InvalidConfigException(section, key, "Button name must start/end with angle brackets [<, >]");
            }

            TextComponent component = section.getRequired(key, TextComponent.class);
            componentsByName.put(key.toLowerCase(), component);
        }

        List<TextComponent> parsedMessages = new ArrayList<>();
        for (String chatLine : chatLines) {
            parsedMessages.add(parseMessageComponents(chatLine, componentsByName));
        }

        if (parsedMessages.size() == 1) {
            return new InteractiveChatMessage(name, group, parsedMessages.get(0));
        }

        return new InteractiveChatMessage(name, group, parsedMessages);
    }

    // To be moved to separate class?
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
