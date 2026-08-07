package io.github.pigaut.rpg.module.menu.config;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.dynamic.*;
import io.github.pigaut.rpg.module.menu.button.icon.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.dynamic.*;
import io.github.pigaut.rpg.module.menu.button.icon.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class FixedButtonLoader implements ConfigLoader<FixedButton> {

    private final EnhancedPlugin plugin;

    public FixedButtonLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid button";
    }

    @Override
    public @NotNull FixedButton loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        int amount = section.getInteger("amount").withDefault(1);
        ItemStack icon = new ItemStack(Material.BEDROCK, amount);

        IconOptions iconOptions = section.getRequired(IconOptions.class);
        iconOptions.applyTo(icon);

        // Button options
        boolean updateOnClick = section.getBoolean("update-on-click").withDefault(true);
        Function onClick = section.get("on-click", Function.class).withDefault(null);
        Function onLeftClick = section.get("on-left-click", Function.class).withDefault(null);
        Function onRightClick = section.get("on-right-click", Function.class).withDefault(null);
        Function onShiftLeftClick = section.get("on-shift-left-click", Function.class).withDefault(null);
        Function onShiftRightClick = section.get("on-shift-right-click", Function.class).withDefault(null);

        // Dynamic material button
        String materialName = section.getRequiredString("type|material");
        if (StringUtil.isParenthesized(materialName, "{", "}")) {
            String dynamicIconName = StringUtil.removeParentheses(materialName);
            DynamicIcon dynamicIcon = plugin.getDynamicIcon(dynamicIconName);
            if (dynamicIcon == null) {
                throw new InvalidConfigException(section, "material", "Could not find dynamic icon with name: " + materialName);
            }
            return new DynamicIconButton(icon, updateOnClick, onClick, onLeftClick, onRightClick,
                    onShiftLeftClick, onShiftRightClick, dynamicIcon);
        }

        return new FixedButton(icon, updateOnClick, onClick, onLeftClick, onRightClick,
                onShiftLeftClick, onShiftRightClick);
    }

}
