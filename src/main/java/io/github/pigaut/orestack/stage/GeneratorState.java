package io.github.pigaut.orestack.stage;

public enum GeneratorState {
    DEPLETED(false),
    GROWING(false),
    HARVESTABLE(true),
    REPLENISHED(true);

    private final boolean harvestable;

    GeneratorState(boolean harvestable) {
        this.harvestable = harvestable;
    }

    public boolean isHarvestable() {
        return harvestable;
    }

}
