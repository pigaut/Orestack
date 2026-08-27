package io.github.pigaut.rpg.config;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.config.attribute.*;
import io.github.pigaut.rpg.config.attribute.legacy.*;
import io.github.pigaut.rpg.config.color.*;
import io.github.pigaut.rpg.config.deserializer.*;
import io.github.pigaut.rpg.config.itemstack.*;
import io.github.pigaut.rpg.config.location.*;
import io.github.pigaut.rpg.config.misc.*;
import io.github.pigaut.rpg.config.persistence.*;
import io.github.pigaut.rpg.core.item.options.*;
import io.github.pigaut.rpg.module.structure.config.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.config.attribute.*;
import io.github.pigaut.rpg.config.attribute.legacy.*;
import io.github.pigaut.rpg.config.color.*;
import io.github.pigaut.rpg.config.deserializer.*;
import io.github.pigaut.rpg.config.itemstack.*;
import io.github.pigaut.rpg.config.location.*;
import io.github.pigaut.rpg.config.misc.*;
import io.github.pigaut.rpg.config.persistence.*;
import io.github.pigaut.rpg.core.item.options.*;
import io.github.pigaut.rpg.module.structure.config.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.configurator.convert.serialize.*;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.block.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.trim.*;
import org.bukkit.persistence.*;
import org.bukkit.potion.*;

public class SpigotConfigurator extends StandardConfigurator {

    @SuppressWarnings("UnstableApiUsage")
    public SpigotConfigurator() {
        int serverVersion = Server.getVersion();

        addLoader(NamespacedKey.class, new NamespacedKeyLoader());

        addLoader(ItemStack.class, new ItemStackLoader());
        addLoader(ItemOptions.class, new ItemOptionsLoader());
        addMapper(ItemStack.class, new ItemStackMapper());

        addLoader(Location.class, new LocationLoader());
        addMapper(Location.class, new LocationMapper());

        addLoader(CustomAttribute.class, new AttributeLoader());
        addMapper(CustomAttribute.class, new AttributeMapper());

        addLoader(Color.class, new ColorLoader());

        addMapper(Block.class, new BlockDataMapper());

        addMapper(PersistentDataContainer.class, new PersistentDataContainerMapper());
        addMapper(PersistentDataType.class, new PersistentDataTypeMapper());

        addDeserializer(World.class, new WorldDeserializer());
        if (serverVersion > Version.V1_17_1) {
            addSerializer(World.class, World::getName);
        }
        else {
            addSerializer(World.class, world -> Reflect.on(world).call("getName").get());
        }

        addDeserializer(Enchantment.class, new EnchantmentDeserializer());
        addSerializer(Enchantment.class, enchant -> enchant.getKey().getKey());

        addDeserializer(Material.class, new MaterialDeserializer());
        addDeserializer(PotionType.class, new PotionTypeDeserializer());
        addDeserializer(Particle.class, new ParticleDeserializer());
        addDeserializer(Sound.class, new SoundDeserializer());
        addDeserializer(Attribute.class, new AttributeDeserializer());

        addConverter(EquipmentSlot.class, new EquipmentSlotConverter());

        if (serverVersion >= Version.V1_19_4) {
            addLoader(TrimPattern.class, new TrimPatternLoader());
        }

        if (serverVersion >= Version.V1_21_3) {
            addDeserializer(EquipmentSlotGroup.class, new SlotGroupDeserializer());
            addSerializer(EquipmentSlotGroup.class, Serializers.defaultSerializer());
            addLoader(CustomAttribute.class, new AttributeLoader());
            addMapper(CustomAttribute.class, new AttributeMapper());
        }
        else {
            addLoader(CustomAttribute.class, new AttributeLoaderLegacy());
            addMapper(CustomAttribute.class, new AttributeMapperLegacy());
        }

    }

}
