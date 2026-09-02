package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.menu.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;
import org.jetbrains.annotations.*;

public class MenuConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("MENU_CAN_SCROLL", (Line<Condition>) line ->
                new MenuCanScroll(line.getRequired(ScrollDirection.class),
                        line.getInteger("amount").withDefault(1)));

        conditions.addLoader("MENU_CAN_SCROLL_RIGHT", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.RIGHT,
                        line.getInteger("amount").withDefault(1)));

        conditions.addLoader("MENU_CAN_SCROLL_DOWN", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.DOWN,
                        line.getInteger("amount").withDefault(1)));

        conditions.addLoader("MENU_CAN_SCROLL_LEFT", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.LEFT,
                        line.getInteger("amount").withDefault(1)));

        conditions.addLoader("MENU_CAN_SCROLL_UP", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.UP,
                        line.getInteger("amount").withDefault(1)));

        conditions.addLoader("MENU_CAN_SCROLL_RIGHT_UP", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.RIGHT_UP,
                        line.getInteger("amount").withDefault(1)));

        conditions.addLoader("MENU_CAN_SCROLL_RIGHT_DOWN", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.RIGHT_DOWN,
                        line.getInteger("amount").withDefault(1)));

        conditions.addLoader("MENU_CAN_SCROLL_LEFT_DOWN", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.LEFT_DOWN,
                        line.getInteger("amount").withDefault(1)));

        conditions.addLoader("MENU_CAN_SCROLL_LEFT_UP", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.LEFT_UP,
                        line.getInteger("amount").withDefault(1)));

        conditions.addAliases("MENU_CAN_SCROLL", "CAN_SCROLL");
        conditions.addAliases("MENU_CAN_SCROLL_RIGHT", "CAN_SCROLL_RIGHT");
        conditions.addAliases("MENU_CAN_SCROLL_DOWN", "CAN_SCROLL_DOWN");
        conditions.addAliases("MENU_CAN_SCROLL_LEFT", "CAN_SCROLL_LEFT");
        conditions.addAliases("MENU_CAN_SCROLL_UP", "CAN_SCROLL_UP");
        conditions.addAliases("MENU_CAN_SCROLL_RIGHT_UP", "CAN_SCROLL_RIGHT_UP");
        conditions.addAliases("MENU_CAN_SCROLL_RIGHT_DOWN", "CAN_SCROLL_RIGHT_DOWN");
        conditions.addAliases("MENU_CAN_SCROLL_LEFT_DOWN", "CAN_SCROLL_LEFT_DOWN");
        conditions.addAliases("MENU_CAN_SCROLL_LEFT_UP", "CAN_SCROLL_LEFT_UP");
    }

}