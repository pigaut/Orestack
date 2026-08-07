package io.github.pigaut.rpg.config.persistence;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.configurator.map.*;
import org.bukkit.persistence.*;
import org.jetbrains.annotations.*;

import static org.bukkit.persistence.PersistentDataType.*;

public class PersistentDataTypeMapper implements ConfigMapper<PersistentDataType> {

    @Override
    public @NotNull FieldType getDefaultMappingType() {
        return FieldType.SCALAR;
    }

    @Override
    public void mapToScalar(@NotNull ConfigScalar scalar, @NotNull PersistentDataType dataType) {
        if (dataType == BYTE) {
            scalar.setValue("byte");
            return;
        }

        if (dataType == SHORT) {
            scalar.setValue("short");
            return;
        }

        if (dataType == INTEGER) {
            scalar.setValue("integer");
            return;
        }

        if (dataType == LONG) {
            scalar.setValue("long");
            return;
        }

        if (dataType == FLOAT) {
            scalar.setValue("float");
            return;
        }

        if (dataType == DOUBLE) {
            scalar.setValue("double");
            return;
        }

        if (dataType == BYTE_ARRAY) {
            scalar.setValue("byte_array");
            return;
        }

        if (dataType == INTEGER_ARRAY) {
            scalar.setValue("integer_array");
            return;
        }

        if (dataType == LONG_ARRAY) {
            scalar.setValue("long_array");
            return;
        }

        if (dataType == TAG_CONTAINER_ARRAY) {
            scalar.setValue("tag_container_array");
            return;
        }

        if (dataType == TAG_CONTAINER) {
            scalar.setValue("tag_container");
            return;
        }

        scalar.setValue("string");
    }

}
