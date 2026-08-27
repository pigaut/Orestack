package io.github.pigaut.rpg.module.function.action.menu;

import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;
import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class MenuActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("OPEN_MENU", (Line<Action>) line -> {
            String menuName = line.getRequiredString(1);
            return new OpenMenu(plugin, menuName);
        });

        actions.addLoader("CLOSE_MENU", (Line<Action>) line ->
                new CloseMenu());

        actions.addLoader("OPEN_PREVIOUS_MENU", (Line<Action>) line ->
                new OpenPreviousMenu());

        actions.addLoader("OPEN_FIRST_MENU", (Line<Action>) line ->
                new OpenFirstMenu());

        actions.addLoader("NEXT_MENU_PAGE", (Line<Action>) line ->
                new NextMenuPage());

        actions.addLoader("PREVIOUS_MENU_PAGE", (Line<Action>) line ->
                new NextMenuPage());

        actions.addLoader("SCROLL_MENU", (Line<Action>) line ->
                new ScrollAtlasMenu(line.getRequired(1, ScrollDirection.class),
                        line.getInteger("amount").withDefault(1)));

        actions.addLoader("SCROLL_MENU_RIGHT", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT, line.getInteger(1).withDefault(1)));

        actions.addLoader("SCROLL_MENU_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.DOWN, line.getInteger(1).withDefault(1)));

        actions.addLoader("SCROLL_MENU_LEFT", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT, line.getInteger(1).withDefault(1)));

        actions.addLoader("SCROLL_MENU_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.UP, line.getInteger(1).withDefault(1)));

        actions.addLoader("SCROLL_MENU_RIGHT_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT_UP, line.getInteger(1).withDefault(1)));

        actions.addLoader("SCROLL_MENU_RIGHT_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT_DOWN, line.getInteger(1).withDefault(1)));

        actions.addLoader("SCROLL_MENU_LEFT_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT_DOWN, line.getInteger(1).withDefault(1)));

        actions.addLoader("SCROLL_MENU_LEFT_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT_UP, line.getInteger(1).withDefault(1)));

        actions.addAliases("SCROLL_MENU", "SCROLL");
        actions.addAliases("SCROLL_MENU_RIGHT", "SCROLL_RIGHT");
        actions.addAliases("SCROLL_MENU_DOWN", "SCROLL_DOWN");
        actions.addAliases("SCROLL_MENU_LEFT", "SCROLL_LEFT");
        actions.addAliases("SCROLL_MENU_UP", "SCROLL_UP");
        actions.addAliases("SCROLL_MENU_RIGHT_UP", "SCROLL_RIGHT_UP");
        actions.addAliases("SCROLL_MENU_RIGHT_DOWN", "SCROLL_RIGHT_DOWN");
        actions.addAliases("SCROLL_MENU_LEFT_DOWN", "SCROLL_LEFT_DOWN");
        actions.addAliases("SCROLL_MENU_LEFT_UP", "SCROLL_LEFT_UP");
    }

}