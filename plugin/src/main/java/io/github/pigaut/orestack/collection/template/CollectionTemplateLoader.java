package io.github.pigaut.orestack.collection.template;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.collection.tier.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionTemplateLoader implements ConfigLoader<CollectionTemplate> {

    private final OrestackPlugin plugin;

    public CollectionTemplateLoader(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid collection";
    }

    @Override
    public @NotNull CollectionTemplate loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String collectionName = scalar.toString();
        CollectionTemplate collection = plugin.getCollectionTemplate(collectionName);
        if (collection == null) {
            throw new InvalidConfigException(scalar, "Could not find collection with name: " + collectionName);
        }
        return collection;
    }

    @Override
    public @NotNull CollectionTemplate loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        if (!(sequence instanceof RootSequence root) || !root.hasFile()) {
            throw new InvalidConfigException(sequence, "Can only load collection from a root sequence");
        }

        String name = root.getName();
        String group = Group.byFile(root.getFile(), "collections", true);

        ConfigSection settingsSection = sequence.getRequiredSection(0);
        ItemStack item = settingsSection.getRequired("item", ItemStack.class);
        if (plugin.getCollectionTemplate(item) != null) {
            throw new InvalidConfigException(settingsSection, "item", "Another collection already uses this item");
        }

        List<CollectionTier> collectionTiers = new ArrayList<>();

        int previousAmount = 0;
        for (int i = 1; i < sequence.size(); i++) {
            CollectionTier collectionTier = sequence.getRequiredSection(i).getRequired(CollectionTier.class);
            if (collectionTier.getAmount() <= previousAmount) {
                throw new InvalidConfigException(sequence, i, "Collection tier amount must be greater than the previous");
            }
            previousAmount = collectionTier.getAmount();
            collectionTiers.add(collectionTier);
        }

        Function onUnlock = settingsSection.get("on-unlock", Function.class)
                .withDefault(null);

        Function onLock = settingsSection.get("on-lock", Function.class)
                .withDefault(null);

        return new CollectionTemplate(name, group, item, collectionTiers, onUnlock, onLock);
    }

}
