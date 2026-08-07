package io.github.pigaut.rpg.bukkit.effect;

import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import org.bukkit.*;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionUtil {

    private static final Map<String, PotionEffectType> EFFECTS = new HashMap<>();

    static {
        registerPotionEffect("absorption", "absorb");
        registerPotionEffect("bad_omen", "omen_bad", "pillager");
        registerPotionEffect("blindness", "blind");
        registerPotionEffect("conduit_power", "conduit", "power_conduit");
        registerPotionEffect("darkness");
        registerPotionEffect("dolphins_grace", "dolphin", "grace");
        registerPotionEffect("fire_resistance", "fire_resist", "resist_fire", "fire_resistance");
        registerPotionEffect("glowing", "glow", "shine", "shiny");
        registerPotionEffect("haste", "fast_digging", "super_pick", "digfast", "dig_speed", "quick_mine", "sharp");
        registerPotionEffect("health_boost", "boost_health", "boost", "hp");
        registerPotionEffect("hero_of_the_village", "hero", "village_hero");
        registerPotionEffect("hunger", "starve", "hungry");
        registerPotionEffect("infested");
        registerPotionEffect("instant_damage", "injure", "damage", "harming", "inflict", "harm");
        registerPotionEffect("instant_health", "health", "insta_heal", "instant_heal", "insta_health", "heal", "healing");
        registerPotionEffect("invisibility", "invisible", "vanish", "invis", "disappear", "hide");
        registerPotionEffect("jump_boost", "leap", "leaping", "jump");
        registerPotionEffect("levitation", "levitate");
        registerPotionEffect("luck", "lucky");
        registerPotionEffect("mining_fatigue", "slow_digging", "fatigue", "dull", "digging", "slow_dig", "dig_slow");
        registerPotionEffect("nausea", "confusion", "sickness", "sick");
        registerPotionEffect("night_vision", "vision", "vision_night");
        registerPotionEffect("oozing");
        registerPotionEffect("poison", "venom");
        registerPotionEffect("raid_omen");
        registerPotionEffect("regeneration", "regen");
        registerPotionEffect("resistance", "damage_resistance", "armor", "dmg_resist", "dmg_resistance");
        registerPotionEffect("saturation", "food");
        registerPotionEffect("slowness", "slow", "sluggish");
        registerPotionEffect("slow_falling", "slow_fall", "fall_slow");
        registerPotionEffect("speed", "sprint", "runfast", "swift", "swiftness", "fast");
        registerPotionEffect("strength", "increase_damage", "bull", "strong", "attack");
        registerPotionEffect("trial_omen");
        registerPotionEffect("unluck", "unlucky");
        registerPotionEffect("water_breathing", "water_breath", "underwater_breathing", "underwater_breath", "air");
        registerPotionEffect("weakness", "weak");
        registerPotionEffect("weaving");
        registerPotionEffect("wind_charged");
        registerPotionEffect("wither", "decay");
    }

    public static @Nullable PotionEffectType getPotionEffect(@NotNull String name) {
        return EFFECTS.get(name.toLowerCase(Locale.ENGLISH));
    }

    public static @NotNull PotionEffectType getPotionEffectOrThrow(String name) {
        PotionEffectType potionEffect = getPotionEffect(name);
        if (potionEffect == null) {
            throw new IllegalStateException("Could not find potion effect with name: " + name);
        }
        return potionEffect;
    }

    private static void registerPotionEffect(String... names) {
        for (String name : names) {
            PotionEffectType foundPotionEffect;
            if (Server.getVersion() >= Version.V1_20_3) {
                foundPotionEffect = Registry.EFFECT.get(NamespacedKey.minecraft(name));
            }
            else if (Server.getVersion() >= Version.V1_18_2) {
                foundPotionEffect = Reflect.onClass(PotionEffectType.class)
                        .call("getByKey", NamespacedKey.minecraft(name))
                        .get();
            }
            else {
                foundPotionEffect = Reflect.onClass(PotionEffectType.class)
                        .call("getByName", name)
                        .get();
            }

            if (foundPotionEffect != null) {
                for (String id : names) {
                    EFFECTS.put(id, foundPotionEffect);
                }
                return;
            }
        }
    }

}
