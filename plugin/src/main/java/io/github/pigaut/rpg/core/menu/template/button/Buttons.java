package io.github.pigaut.rpg.core.menu.template.button;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import org.bukkit.*;

public class Buttons {

    public static final Button WHITE_PANEL = Button.builder()
            .type(Material.WHITE_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button ORANGE_PANEL = Button.builder()
            .type(Material.ORANGE_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button MAGENTA_PANEL = Button.builder()
            .type(Material.MAGENTA_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button LIGHT_BLUE_PANEL = Button.builder()
            .type(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button YELLOW_PANEL = Button.builder()
            .type(Material.YELLOW_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button LIME_PANEL = Button.builder()
            .type(Material.LIME_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button PINK_PANEL = Button.builder()
            .type(Material.PINK_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button GRAY_PANEL = Button.builder()
            .type(Material.GRAY_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button LIGHT_GRAY_PANEL = Button.builder()
            .type(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button CYAN_PANEL = Button.builder()
            .type(Material.CYAN_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button PURPLE_PANEL = Button.builder()
            .type(Material.PURPLE_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button BLUE_PANEL = Button.builder()
            .type(Material.BLUE_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button BROWN_PANEL = Button.builder()
            .type(Material.BROWN_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button GREEN_PANEL = Button.builder()
            .type(Material.GREEN_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button RED_PANEL = Button.builder()
            .type(Material.RED_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button BLACK_PANEL = Button.builder()
            .type(Material.BLACK_STAINED_GLASS_PANE)
            .name(" ")
            .buildButton();

    public static final Button CLOSE = Button.builder()
            .type(Material.RED_CONCRETE)
            .name("&4Close")
            .enchanted(true)
            .onLeftClick((view, playerState) -> view.close())
            .buildButton();

    public static final Button APPLY_CHANGES_AND_CLOSE = Button.builder()
            .type(Material.GREEN_CONCRETE)
            .name("&aApply Changes and Close")
            .enchanted(true)
            .onLeftClick((view, playerState) -> view.close())
            .buildButton();

    public static final Button BACK = Button.builder()
            .type(Material.SPRUCE_DOOR)
            .name("&cBack")
            .enchanted(true)
            .onLeftClick((view, playerState) -> {
                final MenuView previousView = view.getPreviousView();
                if (previousView != null) {
                    previousView.open();
                }
            })
            .buildButton();

    public static final Button MAIN_MENU = Button.builder()
            .type(Material.MINECART)
            .name("&5Main Menu")
            .enchanted(true)
            .onLeftClick((view, playerState) -> {
                final MenuView firstView = view.getFirstView();
                if (view == firstView) {
                    return;
                }
                firstView.open();
            })
            .buildButton();

    public static final Button INVALID_CONDITION = Button.builder()
            .type(Material.RED_WOOL)
            .enchanted(true)
            .name("&4Invalid Condition")
            .addEmptyLine()
            .addLine("&cCould not evaluate condition")
            .buildButton();


}
