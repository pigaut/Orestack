package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.collection.*;
import io.github.pigaut.rpg.module.function.action.menu.*;
import io.github.pigaut.rpg.module.function.action.skill.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;
import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class MenuActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.register("OPEN_MENU", (Line<Action>) line -> {
            String menuName = line.getRequiredString(1);

            if (line.hasFlag("collection")) {
                String collectionName = line.getRequiredString("collection");
                return new OpenCollectionMenu(plugin, menuName, collectionName);
            }

            if (line.hasFlag("skill")) {
                String skillName = line.getRequiredString("skill");
                return new OpenSkillMenu(plugin, menuName, skillName);
            }

            return new OpenMenu(plugin, menuName);
        });

        actions.register("CLOSE_MENU", (Line<Action>) line ->
                new CloseMenu());

        actions.register("OPEN_PREVIOUS_MENU", (Line<Action>) line ->
                new OpenPreviousMenu());

        actions.register("OPEN_FIRST_MENU", (Line<Action>) line ->
                new OpenFirstMenu());

        actions.register("NEXT_MENU_PAGE", (Line<Action>) line ->
                new NextMenuPage());

        actions.register("PREVIOUS_MENU_PAGE", (Line<Action>) line ->
                new NextMenuPage());

        actions.register("SCROLL_MENU", (Line<Action>) line ->
                new ScrollAtlasMenu(line.getRequired(1, ScrollDirection.class),
                        line.getInteger("amount").withDefault(1)));

        actions.register("SCROLL_MENU_RIGHT", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT, line.getInteger(1).withDefault(1)));

        actions.register("SCROLL_MENU_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.DOWN, line.getInteger(1).withDefault(1)));

        actions.register("SCROLL_MENU_LEFT", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT, line.getInteger(1).withDefault(1)));

        actions.register("SCROLL_MENU_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.UP, line.getInteger(1).withDefault(1)));

        actions.register("SCROLL_MENU_RIGHT_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT_UP, line.getInteger(1).withDefault(1)));

        actions.register("SCROLL_MENU_RIGHT_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT_DOWN, line.getInteger(1).withDefault(1)));

        actions.register("SCROLL_MENU_LEFT_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT_DOWN, line.getInteger(1).withDefault(1)));

        actions.register("SCROLL_MENU_LEFT_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT_UP, line.getInteger(1).withDefault(1)));

        actions.registerAlias("SCROLL_MENU", "SCROLL");
        actions.registerAlias("SCROLL_MENU_RIGHT", "SCROLL_RIGHT");
        actions.registerAlias("SCROLL_MENU_DOWN", "SCROLL_DOWN");
        actions.registerAlias("SCROLL_MENU_LEFT", "SCROLL_LEFT");
        actions.registerAlias("SCROLL_MENU_UP", "SCROLL_UP");
        actions.registerAlias("SCROLL_MENU_RIGHT_UP", "SCROLL_RIGHT_UP");
        actions.registerAlias("SCROLL_MENU_RIGHT_DOWN", "SCROLL_RIGHT_DOWN");
        actions.registerAlias("SCROLL_MENU_LEFT_DOWN", "SCROLL_LEFT_DOWN");
        actions.registerAlias("SCROLL_MENU_LEFT_UP", "SCROLL_LEFT_UP");
    }

}