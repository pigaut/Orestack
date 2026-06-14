package io.github.pigaut.orestack.skill;

import io.github.pigaut.voxel.data.function.Function;

import java.util.*;

public class SkillLevel {

    private final int expAmount;
    private final List<String> rewards;
    private final Function onUnlock;

    public SkillLevel(int expAmount, List<String> rewards, Function onUnlock) {
        this.expAmount = expAmount;
        this.rewards = List.copyOf(rewards);
        this.onUnlock = onUnlock;
    }

    public int getExpAmount() {
        return expAmount;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public Function getOnUnlock() {
        return onUnlock;
    }

}
