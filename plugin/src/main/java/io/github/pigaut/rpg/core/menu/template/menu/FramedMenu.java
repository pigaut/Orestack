package io.github.pigaut.rpg.core.menu.template.menu;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import org.jetbrains.annotations.*;

public class FramedMenu extends FixedMenu {

    private final Button[] buttons;

    public FramedMenu(String title, int size) {
        super(title, size);
        this.buttons = new Button[size];
        final Button frameButton = getFrameButton();

        int toolbarStart;
        switch (size) {
            case MenuSize.SMALL -> {
                ButtonLayout.apply(buttons, frameButton, ButtonLayout.SMALL_FRAME);
                toolbarStart = 18;
            }

            case MenuSize.MEDIUM -> {
                ButtonLayout.apply(buttons, frameButton, ButtonLayout.MEDIUM_FRAME);
                toolbarStart = 27;
            }

            case MenuSize.BIG -> {
                ButtonLayout.apply(buttons, frameButton, ButtonLayout.BIG_FRAME);
                toolbarStart = 36;
            }

            case MenuSize.LARGE -> {
                ButtonLayout.apply(buttons, frameButton, ButtonLayout.LARGE_FRAME);
                toolbarStart = 45;
            }
            default -> throw new IllegalArgumentException("Invalid size for framed menu.");
        }

        buttons[toolbarStart++] = getToolbarButton1();
        buttons[toolbarStart++] = getToolbarButton2();
        buttons[toolbarStart++] = getToolbarButton3();
        buttons[toolbarStart++] = getToolbarButton4();
        buttons[toolbarStart++] = getToolbarButton5();
        buttons[toolbarStart++] = getToolbarButton6();
        buttons[toolbarStart++] = getToolbarButton7();
        buttons[toolbarStart++] = getToolbarButton8();
        buttons[toolbarStart] = getToolbarButton9();
    }

    public Button getFrameButton() {
        return Buttons.GRAY_PANEL;
    }

    public Button getToolbarButton1() {
        return this.getFrameButton();
    }

    public Button getToolbarButton2() {
        return this.getFrameButton();
    }

    public Button getToolbarButton3() {
        return this.getFrameButton();
    }

    public Button getToolbarButton4() {
        return this.getFrameButton();
    }

    public Button getToolbarButton5() {
        return Buttons.BACK;
    }

    public Button getToolbarButton6() {
        return this.getFrameButton();
    }

    public Button getToolbarButton7() {
        return this.getFrameButton();
    }

    public Button getToolbarButton8() {
        return this.getFrameButton();
    }

    public Button getToolbarButton9() {
        return this.getFrameButton();
    }

    @Override
    public Button[] createButtons(@NotNull Context context) {
        return buttons.clone();
    }

}
