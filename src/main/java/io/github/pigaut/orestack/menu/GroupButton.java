package io.github.pigaut.orestack.menu;

import io.github.pigaut.voxel.menu.button.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GroupButton extends Button {

    protected final String group;

    public GroupButton(@NotNull ItemStack icon, String group) {
        super(icon);
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

}
