package io.github.pigaut.rpg.core.enchant;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AutoSmelt {

    public static int getSmeltedDropCount(int totalDrops, double smeltChance) {
        int count = 0;
        for (int i = 0; i < totalDrops; i++) {
            if (Probability.test(smeltChance)) {
                count++;
            }
        }
        return count;
    }

    public static ItemStack[] getDrops(@NotNull ItemStack drop, int amount, @NotNull Material smeltResult, double smeltChance) {
        int smeltedCount = getSmeltedDropCount(amount, smeltChance);
        int baseCount = amount - smeltedCount;

        List<ItemStack> drops = new ArrayList<>(2);

        if (baseCount > 0) {
            ItemStack baseDrop = drop.clone();
            baseDrop.setAmount(baseCount);
            drops.add(baseDrop);
        }

        if (smeltedCount > 0) {
            ItemStack smeltedDrop = drop.clone();
            smeltedDrop.setType(smeltResult);
            smeltedDrop.setAmount(smeltedCount);
            drops.add(smeltedDrop);
        }

        return drops.toArray(new ItemStack[0]);
    }

}
