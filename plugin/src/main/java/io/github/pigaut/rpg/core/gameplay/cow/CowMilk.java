package io.github.pigaut.rpg.core.gameplay.cow;

import org.jetbrains.annotations.*;

public class CowMilk {

    private int milk;
    private @Nullable Long lastMilked;

    public CowMilk(int milk) {
        this.milk = milk;
    }

    public int getMilk() {
        return milk;
    }

    public void setMilk(int milk) {
        this.milk = milk;
    }

    public @Nullable Long getLastMilkChange() {
        return lastMilked;
    }

    public void setLastMilkChange(@Nullable Long lastMilked) {
        this.lastMilked = lastMilked;
    }
}
