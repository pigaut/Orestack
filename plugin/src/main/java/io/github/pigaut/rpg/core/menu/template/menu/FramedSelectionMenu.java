package io.github.pigaut.rpg.core.menu.template.menu;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.paged.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.paged.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import org.jetbrains.annotations.*;

public class FramedSelectionMenu extends PagedMenu {

    private final Button[] buttons;

    public FramedSelectionMenu(String title, int size) {
        super(title, size);
        this.buttons = new Button[size];
        final Button frameButton = getFrameButton();

        int toolbarStart;
        switch (size) {
            case MenuSize.SMALL -> {
                addEntrySlots(ButtonLayout.SMALL_BOX);
                ButtonLayout.apply(buttons, frameButton, ButtonLayout.SMALL_FRAME);
                toolbarStart = 18;
            }

            case MenuSize.MEDIUM -> {
                addEntrySlots(ButtonLayout.MEDIUM_BOX);
                ButtonLayout.apply(buttons, frameButton, ButtonLayout.MEDIUM_FRAME);
                toolbarStart = 27;
            }

            case MenuSize.BIG -> {
                addEntrySlots(ButtonLayout.BIG_BOX);
                ButtonLayout.apply(buttons, frameButton, ButtonLayout.BIG_FRAME);
                toolbarStart = 36;
            }

            case MenuSize.LARGE -> {
                addEntrySlots(ButtonLayout.LARGE_BOX);
                ButtonLayout.apply(buttons, frameButton, ButtonLayout.LARGE_FRAME);
                toolbarStart = 45;
            }

            default -> throw new IllegalArgumentException("Invalid size for framed selection menu.");
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
