package io.github.pigaut.rpg.module.recipe;

import org.bukkit.block.Furnace;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public final class RecipeUtil {

    private RecipeUtil() {}

    public static @NotNull List<ItemStack> getCraftingIngredients(@NotNull CraftingInventory inventory) {
        return Arrays.asList(inventory.getMatrix());
    }

    public static void setCraftingIngredients(@NotNull CraftingInventory inventory, @NotNull List<ItemStack> items) {
        inventory.setMatrix(items.toArray(new ItemStack[0]));
    }

    public static @NotNull List<ItemStack> getSmithingIngredients(@NotNull SmithingInventory inventory) {
        List<ItemStack> items = new ArrayList<>(3);
        items.add(inventory.getInputTemplate());
        items.add(inventory.getInputEquipment());
        items.add(inventory.getInputMineral());
        return items;
    }

    public static void setSmithingIngredients(@NotNull SmithingInventory inventory, @NotNull List<ItemStack> items) {
        inventory.setInputTemplate(items.get(0));
        inventory.setInputEquipment(items.get(1));
        inventory.setInputMineral(items.get(2));
    }

    public static @NotNull List<ItemStack> getStonecutterIngredients(@NotNull StonecutterInventory inventory) {
        ItemStack inputItem = inventory.getInputItem();
        return inputItem != null ? List.of(inputItem) : List.of();
    }

    public static void setStonecutterItems(@NotNull StonecutterInventory inventory, @NotNull List<ItemStack> items) {
        inventory.setInputItem(items.get(0));
    }

    public static @NotNull List<ItemStack> getCookingItems(@NotNull FurnaceInventory inventory) {
        ItemStack smeltingItem = inventory.getSmelting();
        return smeltingItem != null ? List.of(smeltingItem) : List.of();
    }

    public static void setCookingItems(@NotNull FurnaceInventory inventory, @NotNull List<ItemStack> items) {
        inventory.setSmelting(items.get(0));
    }

    public static @Nullable Player getSmeltingPlayer(@NotNull Furnace furnace) {
        return getSmeltingPlayer(furnace.getInventory());
    }

    public static @Nullable Player getSmeltingPlayer(@NotNull FurnaceInventory furnaceInventory) {
        List<HumanEntity> viewers = furnaceInventory.getViewers();
        return !viewers.isEmpty() ? (Player) viewers.get(0) : null;
    }

}