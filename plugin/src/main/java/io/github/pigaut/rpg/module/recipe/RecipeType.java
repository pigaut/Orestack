package io.github.pigaut.rpg.module.recipe;

public enum RecipeType {

    SHAPED(true, false),
    SHAPELESS(true, false),
    STONECUTTER(false, false),
    SMITHING(false, false),
    SMELT(false, true),
    FURNACE(false, true),
    BLAST_FURNACE(false, true),
    SMOKER(false, true),
    CAMPFIRE(false, true);

    private final boolean crafting;
    private final boolean smelting;

    RecipeType(boolean crafting, boolean smelting) {
        this.crafting = crafting;
        this.smelting = smelting;
    }

    public boolean isCrafting() {
        return crafting;
    }

    public boolean isSmelting() {
        return smelting;
    }

}
