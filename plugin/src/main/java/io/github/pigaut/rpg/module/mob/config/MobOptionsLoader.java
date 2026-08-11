package io.github.pigaut.rpg.module.mob.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.mob.goal.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.mob.goal.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MobOptionsLoader implements ConfigLoader<MobOptions> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid mob options";
    }

    @Override
    public @NotNull MobOptions loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        EntityType entity = section.get("type|entity", EntityType.class)
                .require(EntityType::isSpawnable, "This entity cannot be spawned")
                .require(e -> e.getEntityClass() != null && LivingEntity.class.isAssignableFrom(e.getEntityClass()),
                        "The entity must be a living entity")
                .orThrow();

        String displayName = section.getString("name")
                .withDefault(null);

        Amount health = section.get("health", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(20));

        Double maxHealth = section.getDouble("max-health")
                .require(Requirements.positive())
                .withDefault(health.maxValue());

        if (health.maxValue() > maxHealth) {
            throw new InvalidConfigException(section, "health", "Health must be lower than max health");
        }

        Double speed = section.getDouble("speed").withDefault(null);
        Double damage = section.getDouble("damage|attack-damage").withDefault(null);
        Double knockbackResistance = section.getDouble("knockback-resistance").withDefault(null);

        ItemStack helmet = section.get("equipment.head", ItemStack.class).withDefault(null);
        ItemStack chestplate = section.get("equipment.chest", ItemStack.class).withDefault(null);
        ItemStack leggings = section.get("equipment.legs", ItemStack.class).withDefault(null);
        ItemStack boots = section.get("equipment.feet", ItemStack.class).withDefault(null);
        ItemStack mainHand = section.get("equipment.hand", ItemStack.class).withDefault(null);
        ItemStack offHand = section.get("equipment.off-hand", ItemStack.class).withDefault(null);

        boolean alwaysShowName = section.getBoolean("always-show-name").withDefault(true);
        int arrowsInBody = section.getInteger("arrows-in-body").withDefault(0);
        boolean baby = section.getBoolean("baby").withDefault(false);
        DyeColor color = section.get("color", DyeColor.class).withDefault(null);
        boolean collidable = section.getBoolean("collidable").withDefault(true);
        boolean canPickupItems = section.getBoolean("can-pickup-items").withDefault(false);
        boolean glowing = section.getBoolean("glowing").withDefault(false);
        boolean gravity = section.getBoolean("gravity").withDefault(true);
        boolean hasAi = section.getBoolean("has-ai").withDefault(true);
        boolean invulnerable = section.getBoolean("invulnerable").withDefault(false);
        boolean invisible = section.getBoolean("invisible").withDefault(false);
        int noDamageTicks = section.get("invulnerability-cooldown", Delay.class).mapIfValid(Delay::toTicks).withDefault(10);
        boolean silent = section.getBoolean("silent").withDefault(false);

        Integer size = section.getInteger("size").withDefault(null);

        Class<? extends Entity> entityClass = entity.getEntityClass();
        List<String> mobGoals = section.getStringList("goals|mob-goals")
                .check(Server.isPaper(), "Goals requires a paper server")
                .withDefault(null);

        MobGoalsTemplate mobGoalsTemplate = null;
        if (mobGoals != null && entityClass != null && Mob.class.isAssignableFrom(entityClass)) {
            @SuppressWarnings("unchecked")
            Class<? extends Mob> mobClass = (Class<? extends Mob>) entityClass;
            mobGoalsTemplate = new MobGoalsTemplate(mobClass, mobGoals);
        }

        return new MobOptions(displayName, maxHealth, health, speed, damage, knockbackResistance,
                helmet, chestplate, leggings, boots, mainHand, offHand,
                alwaysShowName, arrowsInBody, baby, color, collidable, canPickupItems, glowing,
                gravity, hasAi, invulnerable, invisible, noDamageTicks, silent, size,
                mobGoalsTemplate);
    }

}
