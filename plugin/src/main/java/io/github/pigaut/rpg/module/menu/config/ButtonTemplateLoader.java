package io.github.pigaut.rpg.module.menu.config;

import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.conditional.*;
import io.github.pigaut.rpg.module.menu.button.icon.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.conditional.*;
import io.github.pigaut.rpg.module.menu.button.icon.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ButtonTemplateLoader implements ConfigLoader<ButtonTemplate> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid button";
    }

    @Override
    public @NotNull ButtonTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (section.isScalar("condition|conditions")) {
            Condition condition = section.getRequired("condition|conditions", Condition.class);
            FixedButton metButton = section.getRequired("met", FixedButton.class);
            FixedButton unmetButton = section.getRequired("unmet", FixedButton.class);
            return new ConditionalButton(condition, metButton, unmetButton);
        }

        FixedButton defaultButton = section.getRequired(FixedButton.class);

        Map<Condition, Button> buttonsByCondition = new HashMap<>();
        if (section.isSection("modify|replace")) {
            ConfigSection conditionSection = section.getSectionOrCreate("modify|replace");

            Condition condition = conditionSection.getRequired(Condition.class);

            ItemStack icon = defaultButton.getIcon();
            boolean updateOnClick = defaultButton.isUpdateOnClick();

            boolean replace = conditionSection.getKey().equalsIgnoreCase("replace") ||
                    conditionSection.getBoolean("replace|clear").withDefault(false);

            if (replace) {
                icon.setAmount(1);
                icon.setItemMeta(Bukkit.getItemFactory().getItemMeta(icon.getType()));
            }

            IconOptions iconOptions = conditionSection.getRequired(IconOptions.class);
            iconOptions.applyTo(icon);

            Function onClick = conditionSection.get("on-click", Function.class).withDefault(null);
            Function onLeftClick = conditionSection.get("on-left-click", Function.class).withDefault(null);
            Function onRightClick = conditionSection.get("on-right-click", Function.class).withDefault(null);
            Function onShiftLeftClick = conditionSection.get("on-shift-left-click", Function.class).withDefault(null);
            Function onShiftRightClick = conditionSection.get("on-shift-right-click", Function.class).withDefault(null);

            FixedButton buttonOverride = new FixedButton(icon, updateOnClick,
                    onClick != null || replace ? onClick : defaultButton.getOnClick(),
                    onLeftClick != null || replace ? onLeftClick : defaultButton.getOnLeftClick(),
                    onRightClick != null || replace ? onRightClick : defaultButton.getOnRightClick(),
                    onShiftLeftClick != null || replace ? onShiftLeftClick : defaultButton.getOnShiftLeftClick(),
                    onShiftRightClick != null || replace ? onShiftRightClick : defaultButton.getOnShiftRightClick()
            );

            buttonsByCondition.put(condition, buttonOverride);
        }

        if (section.isSequence("modify|replace")) {
            ConfigSequence conditionSequence = section.getRequiredSequence("modify|replace");
            for (ConfigSection conditionSection : conditionSequence.getNestedSections()) {
                Condition condition = conditionSection.getRequired(Condition.class);

                ItemStack icon = defaultButton.getIcon();
                boolean updateOnClick = defaultButton.isUpdateOnClick();

                boolean replace = conditionSection.getKey().equalsIgnoreCase("replace") ||
                        conditionSection.getBoolean("replace|clear").withDefault(false);

                if (replace) {
                    icon.setAmount(1);
                    icon.setItemMeta(Bukkit.getItemFactory().getItemMeta(icon.getType()));
                }

                IconOptions iconOptions = conditionSection.getRequired(IconOptions.class);
                iconOptions.applyTo(icon);

                Function onClick = conditionSection.get("on-click", Function.class).withDefault(null);
                Function onLeftClick = conditionSection.get("on-left-click", Function.class).withDefault(null);
                Function onRightClick = conditionSection.get("on-right-click", Function.class).withDefault(null);
                Function onShiftLeftClick = conditionSection.get("on-shift-left-click", Function.class).withDefault(null);
                Function onShiftRightClick = conditionSection.get("on-shift-right-click", Function.class).withDefault(null);

                FixedButton buttonOverride = new FixedButton(icon, updateOnClick,
                        onClick != null || replace ? onClick : defaultButton.getOnClick(),
                        onLeftClick != null || replace ? onLeftClick : defaultButton.getOnLeftClick(),
                        onRightClick != null || replace ? onRightClick : defaultButton.getOnRightClick(),
                        onShiftLeftClick != null || replace ? onShiftLeftClick : defaultButton.getOnShiftLeftClick(),
                        onShiftRightClick != null || replace ? onShiftRightClick : defaultButton.getOnShiftRightClick()
                );

                buttonsByCondition.put(condition, buttonOverride);
            }
        }

        if (buttonsByCondition.isEmpty()) {
            return defaultButton;
        }

        return new MultiConditionButton(defaultButton, buttonsByCondition);
    }

}
