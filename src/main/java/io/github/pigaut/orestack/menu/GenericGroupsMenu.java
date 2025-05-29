package io.github.pigaut.orestack.menu;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.paged.*;

public class GenericGroupsMenu extends PagedMenu {

    public GenericGroupsMenu(String title) {
        super(title, ChestSize.FIVE_ROWS);

        this.addEntrySlots(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34);

        this.setButtons(Buttons.GRAY_PANEL, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44);

        buttons[40] = new BacktrackButton();

    }

}
