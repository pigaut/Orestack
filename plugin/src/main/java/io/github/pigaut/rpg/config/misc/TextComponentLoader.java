package io.github.pigaut.rpg.config.misc;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

@SuppressWarnings("deprecation") // Paper deprecated TextComponent API
public class TextComponentLoader implements ConfigLoader<TextComponent> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid text component";
    }

    @Override
    public @NotNull TextComponent loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String text = scalar.toString();

        TextComponent component = new TextComponent();
        for (BaseComponent extra : TextComponent.fromLegacyText(text)) {
            component.addExtra(extra);
        }

        return component;
    }

    @Override
    public @NotNull TextComponent loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String text = section.getRequiredString("text");

        TextComponent component = new TextComponent();
        for (BaseComponent extra : TextComponent.fromLegacyText(text)) {
            component.addExtra(extra);
        }

        ConfigLine clickLine = section.getLine("on-click|click", LineStyle.LABELED, "<click_action> <value>").withDefault(null);
        if (clickLine != null) {
            ClickEvent.Action action = clickLine.getRequired(0, ClickEvent.Action.class);
            String value = clickLine.getRequiredString(1);
            component.setClickEvent(new ClickEvent(action, value));
        }

        ConfigLine hoverLine = section.getLine("on-hover|hover", LineStyle.LABELED, "<hover_action> <value>").withDefault(null);
        if (hoverLine != null) {
            HoverEvent.Action action = hoverLine.getRequired(0, HoverEvent.Action.class);
            Content content = switch (action) {
                case SHOW_TEXT -> {
                    String hoverText = hoverLine.getRequiredString(1);
                    yield new Text(TextComponent.fromLegacyText(hoverText));
                }
                case SHOW_ITEM -> {
                    Material material = hoverLine.getRequired(1, Material.class);
                    int amount = hoverLine.getInteger(2).withDefault(1);
                    yield new Item(material.getKey().toString(), amount, null);
                }
                case SHOW_ENTITY -> {
                    String entityType = hoverLine.getRequiredString(1);
                    UUID entityId = hoverLine.get("id", UUID.class).withDefault(UUID.randomUUID());
                    String displayName = hoverLine.getString("name").withDefault(null);
                    yield new Entity(entityType, entityId.toString(), displayName != null ? new TextComponent(displayName) : null);
                }
                default -> throw new InvalidConfigException(section, "on-hover", "Unsupported hover action: " + action);
            };
            component.setHoverEvent(new HoverEvent(action, content));
        }

        return component;
    }

}
