package io.github.pigaut.orestack.generator;

public enum GeneratorState {
    DEPLETED     (false, false),
    GROWING      (false, true),
    UNRIPE       (true, true),
    RIPE         (true, false),
    REGROWN      (true, false);

    private final boolean harvestable;
    private final boolean transitional;

    GeneratorState(boolean harvestable, boolean transitional) {
        this.harvestable = harvestable;
        this.transitional = transitional;
    }

    public boolean isHarvestable() {
        return harvestable;
    }

    public boolean isTransitional() {
        return transitional;
    }

}
