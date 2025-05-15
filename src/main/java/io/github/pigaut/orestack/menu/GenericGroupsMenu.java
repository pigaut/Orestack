package io.github.pigaut.orestack.menu;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.paged.*;

public class GenericGroupsMenu extends PagedMenu {

    public GenericGroupsMenu(String title) {
        super(title, ChestSize.FIVE_ROWS);

        this.addEntrySlots(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 31, 33, 34);

        buttons[40] = new BacktrackButton();
    }

}
