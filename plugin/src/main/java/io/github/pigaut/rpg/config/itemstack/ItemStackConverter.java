package io.github.pigaut.rpg.config.itemstack;

import com.ssomar.score.api.executableitems.*;
import com.ssomar.score.api.executableitems.config.*;
import com.willfp.eco.core.items.*;
import com.willfp.ecoitems.items.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.convert.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.lumine.mythic.bukkit.*;
import io.lumine.mythic.core.items.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemStackConverter implements Converter<ItemStack> {

    private final EnhancedPlugin plugin;

    public ItemStackConverter(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull ItemStack loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String itemName = scalar.toString();
        try {
            return deserialize(itemName);
        } catch (StringParseException e) {
            LatestMaterial latestMaterial = ParseUtil.parseEnumOrNull(LatestMaterial.class, itemName);
            if (latestMaterial != null) {
                ConfigRoot root = scalar.getRoot();
                root.collectWarning(new InvalidConfigException(scalar, "Material not available on this server version"));
                return new ItemStack(Material.STONE);
            }
            throw new InvalidConfigException(scalar, e.getMessage());
        }
    }

    @Override
    public ItemStack deserialize(String itemValue) throws StringParseException {
        String[] itemData = itemValue.split(":");
        if (itemData.length > 2) {
            throw new StringParseException("Could not extract item data from: " + itemValue);
        }

        String pluginName = itemData.length > 1 ? itemData[0] : null;
        String itemName = itemData[itemData.length - 1];

        if (pluginName != null) {
            if (pluginName.equalsIgnoreCase("mythicmobs") || pluginName.equalsIgnoreCase("mm")) {
                if (!Server.isPluginEnabled("MythicMobs")) {
                    throw new StringParseException("MythicMobs is not loaded/enabled");
                }

                ItemStack item = MythicBukkit.inst().getItemManager().getItemStack(itemName);
                if (item == null) {
                    throw new StringParseException("Could not find MythicMobs item with name: " + itemName);
                }

                return item;
            }

            else if (pluginName.equalsIgnoreCase("executableitems") || pluginName.equalsIgnoreCase("ei")) {
                if (!Server.isPluginEnabled("ExecutableItems")) {
                    throw new StringParseException("ExecutableItems is not loaded/enabled");
                }

                ExecutableItemInterface executableItem = ExecutableItemsAPI.getExecutableItemsManager()
                        .getExecutableItem(itemName).orElse(null);

                if (executableItem == null) {
                    throw new StringParseException("Could not find ExecutableItem with name: " + itemName);
                }

                return executableItem.buildItem(1, Optional.empty()); // facepalm...
            }

            else if (pluginName.equalsIgnoreCase("ecoitems") || pluginName.equalsIgnoreCase("ec")) {
                if (!Server.isPluginEnabled("EcoItems")) {
                    throw new StringParseException("EcoItems is not loaded/enabled");
                }

                EcoItem ecoItem = EcoItems.INSTANCE.getByID(itemName);
                if (ecoItem == null) {
                    throw new StringParseException("Could not find EcoItem with name: " + itemName);
                }

                return ecoItem.getItemStack();
            }

            else {
                throw new StringParseException("Custom items are not supported for plugin: " + pluginName);
            }
        }

        ItemTemplate itemTemplate = plugin.getItemTemplate(itemName);
        if (itemTemplate != null) {
            return itemTemplate.createItemStack();
        }

        Material material = MaterialUtil.getMaterial(itemName);
        if (material != null) {
            return new ItemStack(material);
        }

        throw new StringParseException("Could not find item/material with name: " + itemName);
    }

    @Override
    public @NotNull String serialize(@NotNull ItemStack itemStack) {
        if (Server.isPluginLoaded("MythicMobs")) {
            ItemExecutor itemManager = MythicBukkit.inst().getItemManager();
            String mythicItemId = itemManager.getMythicTypeFromItem(itemStack);
            if (mythicItemId != null) {
                MythicItem mythicItem = itemManager.getItem(mythicItemId).orElse(null);
                if (mythicItem != null) {
                    return "MythicMobs:" + mythicItemId;
                }
            }
        }

        if (Server.isPluginLoaded("ExecutableItems")) {
            ExecutableItemInterface executableItem = ExecutableItemsAPI.getExecutableItemsManager()
                    .getExecutableItem(itemStack).orElse(null);

            if (executableItem != null) {
                return "ExecutableItems:" + executableItem.getId();
            }
        }

        if (Server.isPluginLoaded("EcoItems")) {
            CustomItem ecoItem = Items.getCustomItem(itemStack);
            if (ecoItem != null) {
                String ecoItemId = ecoItem.getKey().getKey();
                return "EcoItems:" + ecoItemId;
            }
        }

        ItemTemplate itemTemplate = plugin.getItemTemplate(itemStack);
        if (itemTemplate != null) {
            return itemTemplate.getName();
        }

        return itemStack.getType().toString();
    }

}
