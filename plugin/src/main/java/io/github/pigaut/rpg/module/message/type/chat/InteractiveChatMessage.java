package io.github.pigaut.rpg.module.message.type.chat;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.type.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

@SuppressWarnings("deprecation") // Paper deprecated TextComponent API
public class InteractiveChatMessage extends ChatMessage {

    private final List<TextComponent> templates;

    public InteractiveChatMessage(@NotNull String name, @Nullable String group, @NotNull List<TextComponent> templates) {
        super(name, group);
        this.templates = templates;
    }

    public InteractiveChatMessage(@NotNull String name, @Nullable String group, @NotNull TextComponent template) {
        this(name, group, List.of(template));
    }

    public void send(@NotNull Player player, @NotNull Context context) {
        for (TextComponent template : templates) {
            BaseComponent parsed = template.duplicate();
            parseComponent(parsed, context);
            player.spigot().sendMessage(parsed);
        }
    }

    private static void parseComponent(@NotNull BaseComponent component, @NotNull Context context) {
        if (component instanceof TextComponent textComponent) {
            textComponent.setText(PlaceholderUtil.parseAll(context, textComponent.getText()));
        }

        if (component.getClickEvent() != null) {
            ClickEvent click = component.getClickEvent();
            component.setClickEvent(new ClickEvent(click.getAction(), PlaceholderUtil.parseAll(context, click.getValue())));
        }

        if (component.getHoverEvent() != null) {
            component.setHoverEvent(parseHover(component.getHoverEvent(), context));
        }

        if (component.getExtra() != null) {
            for (BaseComponent extra : component.getExtra()) {
                parseComponent(extra, context);
            }
        }
    }

    private static HoverEvent parseHover(@NotNull HoverEvent hoverEvent, @NotNull Context context) {
        List<Content> parsedContents = new ArrayList<>();
        for (Content content : hoverEvent.getContents()) {
            if (content instanceof Text text && text.getValue() instanceof BaseComponent[] hoverComponents) {
                BaseComponent[] parsed = new BaseComponent[hoverComponents.length];
                for (int i = 0; i < hoverComponents.length; i++) {
                    BaseComponent duplicated = hoverComponents[i].duplicate();
                    parseComponent(duplicated, context);
                    parsed[i] = duplicated;
                }
                parsedContents.add(new Text(parsed));
            } else {
                parsedContents.add(content);
            }
        }
        return new HoverEvent(hoverEvent.getAction(), parsedContents.toArray(new Content[0]));
    }

}