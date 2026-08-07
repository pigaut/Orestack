package io.github.pigaut.rpg.bukkit.attribute;

import org.bukkit.attribute.*;
import org.jetbrains.annotations.*;

public enum AttributeOperation {

    ADD_NUMBER(AttributeModifier.Operation.ADD_NUMBER),
    ADD_SCALAR(AttributeModifier.Operation.ADD_SCALAR),
    MULTIPLY_SCALAR_1(AttributeModifier.Operation.MULTIPLY_SCALAR_1),

    ADD_VALUE(AttributeModifier.Operation.ADD_NUMBER),
    ADD_PERCENT(AttributeModifier.Operation.ADD_SCALAR),
    MULTIPLY_TOTAL(AttributeModifier.Operation.MULTIPLY_SCALAR_1),

    ADD(AttributeModifier.Operation.ADD_NUMBER),
    PERCENT(AttributeModifier.Operation.ADD_SCALAR),
    MULTIPLY(AttributeModifier.Operation.MULTIPLY_SCALAR_1);

    private final AttributeModifier.Operation operation;

    AttributeOperation(AttributeModifier.Operation operation) {
        this.operation = operation;
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    public static AttributeOperation of(@NotNull AttributeModifier.Operation operation) {
        switch (operation) {
            case ADD_NUMBER -> {
                return ADD_VALUE;
            }
            case ADD_SCALAR -> {
                return ADD_PERCENT;
            }
            case MULTIPLY_SCALAR_1 -> {
                return MULTIPLY_TOTAL;
            }
            default -> throw new IllegalArgumentException("Operation cannot be null");
        }
    }

}
