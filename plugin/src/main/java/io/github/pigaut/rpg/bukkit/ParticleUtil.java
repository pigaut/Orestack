package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ParticleUtil {

    public static @NotNull Particle getParticleOrThrow(@NotNull String particleName) {
        Particle particle = getParticle(particleName);
        if (particle == null) {
            throw new IllegalStateException("Could not find particle with name: " + particleName);
        }
        return particle;
    }

    public static @Nullable Particle getParticle(@NotNull String name) {
        name = CaseFormatter.toConstantCase(name);
        Particle particle = ParseUtil.parseEnumOrNull(Particle.class, name);

        if (particle != null) {
            return particle;
        }

        return CHANGED_PARTICLE_NAMES.get(name);
    }

    private static final Map<String, Particle> CHANGED_PARTICLE_NAMES = new HashMap<>();

    private static void registerChangedName(int version, String oldName, String newName) {
        if (Server.getVersion() >= version) {
            Particle newParticle = ParseUtil.parseEnumOrNull(Particle.class, newName);
            if (newParticle == null) {
                throw new IllegalStateException("Could not register new particle: " + newName + " under old name: " + oldName);
            }
            CHANGED_PARTICLE_NAMES.put(oldName, newParticle);
        } else {
            Particle oldParticle = ParseUtil.parseEnumOrNull(Particle.class, oldName);
            if (oldParticle == null) {
                throw new IllegalStateException("Could not register old particle: " + oldName + " under new name: " + newName);
            }
            CHANGED_PARTICLE_NAMES.put(newName, oldParticle);
        }
    }

    static {
        var v1_18 = Version.V1_18;
        var v1_20 = Version.V1_20;
        var v1_20_5 = Version.V1_20_5;

        registerChangedName(v1_20_5, "VILLAGER_ANGRY", "ANGRY_VILLAGER");
        registerChangedName(v1_20_5, "WATER_BUBBLE", "BUBBLE");
        registerChangedName(v1_20_5, "DRIP_LAVA", "DRIPPING_LAVA");
        registerChangedName(v1_20_5, "DRIP_WATER", "DRIPPING_WATER");

        registerChangedName(v1_20_5, "REDSTONE", "DUST");

        registerChangedName(v1_20_5, "SPELL", "EFFECT");
        registerChangedName(v1_20_5, "SPELL_INSTANT", "INSTANT_EFFECT");
        registerChangedName(v1_20_5, "SPELL_MOB", "ENTITY_EFFECT");
        registerChangedName(v1_20_5, "SPELL_MOB_AMBIENT", "ENTITY_EFFECT");
        registerChangedName(v1_20_5, "SPELL_WITCH", "WITCH");

        registerChangedName(v1_20_5, "MOB_APPEARANCE", "ELDER_GUARDIAN");
        registerChangedName(v1_20_5, "ENCHANTMENT_TABLE", "ENCHANT");
        registerChangedName(v1_20_5, "CRIT_MAGIC", "ENCHANTED_HIT");
        registerChangedName(v1_20_5, "EXPLOSION_LARGE", "EXPLOSION");
        registerChangedName(v1_20_5, "EXPLOSION_HUGE", "EXPLOSION_EMITTER");
        registerChangedName(v1_20_5, "FIREWORKS_SPARK", "FIREWORK");
        registerChangedName(v1_20_5, "WATER_WAKE", "FISHING");
        registerChangedName(v1_20_5, "VILLAGER_HAPPY", "HAPPY_VILLAGER");
        registerChangedName(v1_20_5, "SLIME", "ITEM_SLIME");
        registerChangedName(v1_20_5, "SMOKE_LARGE", "LARGE_SMOKE");
        registerChangedName(v1_20_5, "TOWN_AURA", "MYCELIUM");
        registerChangedName(v1_20_5, "EXPLOSION_NORMAL", "POOF");
        registerChangedName(v1_20_5, "WATER_DROP", "RAIN");
        registerChangedName(v1_20_5, "SMOKE_NORMAL", "SMOKE");
        registerChangedName(v1_20_5, "WATER_SPLASH", "SPLASH");
        registerChangedName(v1_20_5, "TOTEM", "TOTEM_OF_UNDYING");

        registerChangedName(v1_20_5, "ITEM_CRACK", "ITEM");
        registerChangedName(v1_20_5, "BLOCK_CRACK", "BLOCK");
        registerChangedName(v1_20_5, "BLOCK_DUST", "BLOCK");

        registerChangedName(v1_20_5, "SNOWBALL", "ITEM_SNOWBALL");
        registerChangedName(v1_20_5, "SNOW_SHOVEL", "ITEM_SNOWBALL");
        registerChangedName(v1_20_5, "SUSPENDED", "UNDERWATER");
        registerChangedName(v1_20_5, "SUSPENDED_DEPTH", "UNDERWATER");
        registerChangedName(v1_18, "BARRIER", "BLOCK_MARKER");

        var serverVersion = Server.getVersion();
        if (serverVersion >= Version.V1_17) {
            registerChangedName(v1_18, "LIGHT", "BLOCK_MARKER");
        }
        if (serverVersion >= Version.V1_19_4) {
            registerChangedName(v1_20, "FALLING_CHERRY_LEAVES", "CHERRY_LEAVES");
            registerChangedName(v1_20, "LANDING_CHERRY_LEAVES", "CHERRY_LEAVES");
        }
    }

    private static final Set<Particle> DIRECTIONAL = new HashSet<>();
    private static final Set<Particle> EFFECTS = new HashSet<>();
    private static final Set<Particle> BLOCKS = new HashSet<>();

    public static final Particle CRIT = getParticle("CRIT");
    public static final Particle FLAME = getParticle("FLAME");
    public static final Particle DUST = getParticle("DUST");
    public static final Particle EFFECT = getParticle("EFFECT");
    public static final Particle NOTE = getParticle("NOTE");
    public static final Particle ITEM = getParticle("ITEM");
    public static final Particle BLOCK = getParticle("BLOCK");

    public static boolean isDirectional(@NotNull Particle particle) {
        return DIRECTIONAL.contains(particle);
    }

    public static boolean isEffect(@NotNull Particle particle) {
        return EFFECTS.contains(particle);
    }

    public static boolean isDust(@NotNull Particle particle) {
        return particle == DUST;
    }

    public static boolean isItemBreak(@NotNull Particle particle) {
        return particle == ITEM;
    }

    public static boolean isNote(@NotNull Particle particle) {
        return particle == NOTE;
    }

    public static boolean isBlock(@NotNull Particle particle) {
        return BLOCKS.contains(particle);
    }

    static {
        int serverVersion = Server.getVersion();

        DIRECTIONAL.add(ParticleUtil.getParticle("PORTAL"));
        DIRECTIONAL.add(ParticleUtil.getParticle("TOTEM_OF_UNDYING"));
        DIRECTIONAL.add(ParticleUtil.getParticle("SPIT"));
        DIRECTIONAL.add(ParticleUtil.getParticle("SQUID_INK"));
        DIRECTIONAL.add(ParticleUtil.getParticle("LARGE_SMOKE"));
        DIRECTIONAL.add(ParticleUtil.getParticle("SMOKE"));
        DIRECTIONAL.add(ParticleUtil.getParticle("FISHING"));
        DIRECTIONAL.add(ParticleUtil.getParticle("NAUTILUS"));
        DIRECTIONAL.add(ParticleUtil.getParticle("BUBBLE"));
        DIRECTIONAL.add(ParticleUtil.getParticle("BUBBLE_COLUMN_UP"));
        DIRECTIONAL.add(ParticleUtil.getParticle("BUBBLE_POP"));
        DIRECTIONAL.add(ParticleUtil.getParticle("CAMPFIRE_COSY_SMOKE"));
        DIRECTIONAL.add(ParticleUtil.getParticle("CAMPFIRE_SIGNAL_SMOKE"));
        DIRECTIONAL.add(ParticleUtil.getParticle("CLOUD"));
        DIRECTIONAL.add(ParticleUtil.getParticle("CRIT"));
        DIRECTIONAL.add(ParticleUtil.getParticle("ENCHANTED_HIT"));
        DIRECTIONAL.add(ParticleUtil.getParticle("DAMAGE_INDICATOR"));
        DIRECTIONAL.add(ParticleUtil.getParticle("DRAGON_BREATH"));
        DIRECTIONAL.add(ParticleUtil.getParticle("ENCHANT"));
        DIRECTIONAL.add(ParticleUtil.getParticle("END_ROD"));
        DIRECTIONAL.add(ParticleUtil.getParticle("POOF"));
        DIRECTIONAL.add(ParticleUtil.getParticle("FIREWORK"));
        DIRECTIONAL.add(ParticleUtil.getParticle("FLAME"));

        if (serverVersion >= Version.V1_16) {
            DIRECTIONAL.add(ParticleUtil.getParticle("SOUL"));
            DIRECTIONAL.add(ParticleUtil.getParticle("SOUL_FIRE_FLAME"));
            DIRECTIONAL.add(ParticleUtil.getParticle("REVERSE_PORTAL"));
        }

        if (serverVersion >= Version.V1_17) {
            DIRECTIONAL.add(ParticleUtil.getParticle("SCRAPE"));
            DIRECTIONAL.add(ParticleUtil.getParticle("SMALL_FLAME"));
            DIRECTIONAL.add(ParticleUtil.getParticle("ELECTRIC_SPARK"));
        }

        if (serverVersion >= Version.V1_19) {
            DIRECTIONAL.add(ParticleUtil.getParticle("SCULK_CHARGE"));
            DIRECTIONAL.add(ParticleUtil.getParticle("SCULK_CHARGE_POP"));
            DIRECTIONAL.add(ParticleUtil.getParticle("SCULK_SOUL"));
        }

        if (serverVersion >= Version.V1_20) {
            DIRECTIONAL.add(ParticleUtil.getParticle("WAX_OFF"));
            DIRECTIONAL.add(ParticleUtil.getParticle("WAX_ON"));
        }

        EFFECTS.add(ParticleUtil.getParticle("EFFECT"));
        EFFECTS.add(ParticleUtil.getParticle("INSTANT_EFFECT"));
        EFFECTS.add(ParticleUtil.getParticle("SPELL_MOB")); // ENTITY_EFFECT
        EFFECTS.add(ParticleUtil.getParticle("SPELL_MOB_AMBIENT")); // ENTITY_EFFECT
        EFFECTS.add(ParticleUtil.getParticle("WITCH"));

        BLOCKS.add(ParticleUtil.getParticle("BLOCK_CRACK")); // BLOCK
        BLOCKS.add(ParticleUtil.getParticle("BLOCK_DUST")); // BLOCK
        BLOCKS.add(ParticleUtil.getParticle("FALLING_DUST"));

    }

}
