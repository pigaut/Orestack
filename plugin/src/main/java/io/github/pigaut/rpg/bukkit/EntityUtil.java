package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.effect.*;
import io.github.pigaut.rpg.core.enchant.*;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.projectiles.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityUtil {

    public static @Nullable LivingEntity getDamagerEntity(@NotNull Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        }

        if (entity instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }

        return null;
    }

    public static @NotNull Vector getForwardVector(@NotNull LivingEntity entity) {
        Vector forward = entity.getEyeLocation().getDirection().setY(0);
        if (forward.lengthSquared() < 1.0E-4) {
            forward = new Vector(1, 0, 0);
        } else {
            forward.normalize();
        }
        return forward;
    }

    public static @NotNull Vector getRightVector(@NotNull LivingEntity entity) {
        Vector forward = entity.getEyeLocation().getDirection().setY(0);
        if (forward.lengthSquared() < 1.0E-4) {
            forward = new Vector(1, 0, 0);
        } else {
            forward.normalize();
        }
        return new Vector(-forward.getZ(), 0, forward.getX());
    }

    public static boolean isInvulnerable(@NotNull LivingEntity entity) {
        if (entity instanceof Player player) {
            GameMode gameMode = player.getGameMode();
            if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) {
                return true;
            }
        }
        return entity.isInvulnerable();
    }

    public static boolean attack(@NotNull LivingEntity attacker, @NotNull LivingEntity target, double damage) {
        if (damage(target, damage)) {
            knockback(attacker, target, damage);
            return true;
        }
        return false;
    }

    public static boolean damage(@NotNull LivingEntity target, double damage) {
        if (isInvulnerable(target)) {
            return false;
        }

        // Armor reduction
        AttributeInstance armorAttribute = target.getAttribute(Attributes.ARMOR);
        AttributeInstance toughnessAttribute = target.getAttribute(Attributes.ARMOR_TOUGHNESS);

        if (armorAttribute != null && toughnessAttribute != null) {
            double armor = armorAttribute.getValue();
            double toughness = toughnessAttribute.getValue();
            double armorReduction = Math.max(armor / 25.0, armor - damage / (2.0 + toughness / 4.0));
            damage *= 1.0 - Math.min(armorReduction, 20.0) / 25.0;
        }

        // Protection enchantment reduction
        EntityEquipment equipment = target.getEquipment();
        if (equipment != null) {
            int protection = 0;
            for (ItemStack piece : equipment.getArmorContents()) {
                if (piece != null) {
                    protection += piece.getEnchantmentLevel(Enchants.PROTECTION);
                }
            }
            damage *= 1.0 - Math.min(protection * 0.04, 0.64);
        }

        // Resistance potion effect
        PotionEffect resistance = target.getPotionEffect(PotionEffects.RESISTANCE);
        if (resistance != null) {
            int amplifier = resistance.getAmplifier() + 1;
            damage *= 1.0 - Math.min(amplifier * 0.2, 1.0);
        }

        // Absorption hearts
        double absorption = target.getAbsorptionAmount();
        if (absorption > 0) {
            double absorbed = Math.min(absorption, damage);
            target.setAbsorptionAmount(absorption - absorbed);
            damage -= absorbed;
        }

        if (damage <= 0) {
            return false;
        }

        double newHealth = Math.max(0, target.getHealth() - damage);
        target.setHealth(newHealth);
        return true;
    }

    public static void knockback(@NotNull LivingEntity source, @NotNull LivingEntity target) {
        knockback(source, target, 0.4);
    }

    public static void knockback(@NotNull LivingEntity source, @NotNull LivingEntity target, double strength) {
        if (isInvulnerable(target)) {
            return;
        }

        Vector knockback = target.getLocation().toVector()
                .subtract(source.getLocation().toVector())
                .setY(0)
                .normalize()
                .setY(0.4)
                .multiply(strength);

        target.setVelocity(knockback);
    }

    public static void removeAllEquipment(@NotNull LivingEntity entity) {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment != null) {
            equipment.setHelmet(null);
            equipment.setChestplate(null);
            equipment.setLeggings(null);
            equipment.setBoots(null);
            equipment.setItemInMainHand(null);
            equipment.setItemInOffHand(null);
        }
    }

    public static @NotNull List<LivingEntity> getEntitiesInRange(@NotNull LivingEntity source, double range, int limit) {
        List<LivingEntity> targets = new ArrayList<>();

        Collection<Entity> nearby = source.getNearbyEntities(range, range, range);
        for (Entity entity : nearby) {
            if (!(entity instanceof LivingEntity living)) {
                continue;
            }

            targets.add(living);
            if (targets.size() >= limit) {
                break;
            }
        }

        return targets;
    }

    public static @NotNull List<LivingEntity> getEntitiesInRadius(@NotNull LivingEntity source, double radius, int limit) {
        List<LivingEntity> targets = new ArrayList<>();

        Collection<Entity> nearby = source.getNearbyEntities(radius, radius, radius);
        for (Entity entity : nearby) {
            if (!(entity instanceof LivingEntity living) || living.isDead()) {
                continue;
            }

            if (source.getLocation().distanceSquared(living.getLocation()) > (radius * radius)) {
                continue;
            }

            targets.add(living);
            if (targets.size() >= limit) {
                break;
            }
        }

        return targets;
    }

    public static @NotNull List<LivingEntity> getEntitiesInRing(@NotNull LivingEntity source, double diameter, double thickness, int limit) {
        List<LivingEntity> targets = new ArrayList<>();
        double outerRadius = diameter / 2.0;
        double innerRadius = Math.max(0, outerRadius - thickness);

        double outerRadiusSq = outerRadius * outerRadius;
        double innerRadiusSq = innerRadius * innerRadius;

        Collection<Entity> nearby = source.getNearbyEntities(outerRadius, outerRadius, outerRadius);
        Location center = source.getLocation();

        for (Entity entity : nearby) {
            if (entity instanceof LivingEntity living && !living.isDead()) {
                double distSq = center.distanceSquared(living.getLocation());

                if (distSq <= outerRadiusSq && distSq >= innerRadiusSq) {
                    targets.add(living);
                    if (targets.size() >= limit) {
                        break;
                    }
                }
            }
        }
        return targets;
    }

    public static @NotNull List<LivingEntity> getEntitiesInFront(@NotNull LivingEntity source, double length, double width, int limit) {
        List<LivingEntity> targets = new ArrayList<>();
        Location origin = source.getLocation();
        Vector direction = origin.getDirection().setY(0).normalize();
        Vector right = new Vector(-direction.getZ(), 0, direction.getX()).normalize();

        double searchRadius = Math.max(length, width);
        Collection<Entity> nearby = source.getNearbyEntities(searchRadius, searchRadius, searchRadius);

        for (Entity entity : nearby) {
            if (entity instanceof LivingEntity living && !living.isDead()) {
                Vector toTarget = living.getLocation().toVector().subtract(origin.toVector());
                toTarget.setY(0);

                double forwardDist = toTarget.dot(direction);
                double sidewaysDist = Math.abs(toTarget.dot(right));

                if (forwardDist > 0 && forwardDist <= length && sidewaysDist <= (width / 2.0)) {
                    targets.add(living);
                    if (targets.size() >= limit) {
                        break;
                    }
                }
            }
        }

        return targets;
    }

    public static @NotNull ArmorStand createHologram(@NotNull String displayName, @NotNull Location location, boolean persistent) {
        World world = LocationUtil.getWorldOrDefault(location);
        ArmorStand hologram = (ArmorStand) world.spawnEntity(
                new Location(world, location.getBlockX(), 3, location.getBlockZ()),
                EntityType.ARMOR_STAND
        );
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setMarker(true);
        hologram.setArms(false);
        hologram.setBasePlate(false);
        hologram.setCanPickupItems(false);
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            hologram.addEquipmentLock(equipmentSlot, ArmorStand.LockType.REMOVING_OR_CHANGING);
        }
        hologram.teleport(location.clone().subtract(0, 0.5, 0));
        hologram.setPersistent(persistent);
        hologram.setCustomNameVisible(true);
        hologram.setCustomName(ColorUtil.parseAll(displayName));
        return hologram;
    }

}
