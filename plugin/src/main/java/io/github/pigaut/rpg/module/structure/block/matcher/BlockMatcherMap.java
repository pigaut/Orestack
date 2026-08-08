package io.github.pigaut.rpg.module.structure.block.matcher;

import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockMatcherMap<T> {

    private final List<Entry> entries = new ArrayList<>();

    public void put(@NotNull BlockMatcher matcher, T value) {
        entries.add(new Entry(matcher, value));
    }

    @Nullable
    public T get(@NotNull Block block) {
        for (Entry entry : entries) {
            if (entry.matcher.matchBlock(block)) {
                return entry.value;
            }
        }
        return null;
    }

    public T getOrDefault(@NotNull Block block, T defaultValue) {
        for (Entry entry : entries) {
            if (entry.matcher.matchBlock(block)) {
                return entry.value;
            }
        }
        return defaultValue;
    }

    private class Entry {
        private final BlockMatcher matcher;
        private final T value;

        public Entry(@NotNull BlockMatcher matcher, T value) {
            this.matcher = matcher;
            this.value = value;
        }
    }

}