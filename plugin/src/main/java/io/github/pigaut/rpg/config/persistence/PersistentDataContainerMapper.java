package io.github.pigaut.rpg.config.persistence;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.configurator.map.*;
import org.bukkit.*;
import org.bukkit.persistence.*;
import org.jetbrains.annotations.*;

public class PersistentDataContainerMapper implements ConfigMapper<PersistentDataContainer> {

    @Override
    public @NotNull FieldType getDefaultMappingType() {
        return FieldType.SEQUENCE;
    }

    @Override
    public void mapToSequence(@NotNull ConfigSequence sequence, @NotNull PersistentDataContainer container) {
        for (NamespacedKey persistentKey : container.getKeys()) {
            final PersistentDataType<?, ?> dataType = PersistentData.getDataType(container, persistentKey);
            if (dataType == null)
                continue;

            final Object value = container.get(persistentKey, dataType);
            if (value == null)
                continue;

            final ConfigSection dataSection = sequence.addEmptySection();
            dataSection.set("key", persistentKey.toString());
            dataSection.set("type", dataType);
            dataSection.set("value", value);
        }
    }

}
