package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExpUtil {

    private ExpUtil() {}

    public static void dropExp(Location location, int totalExp) {
        World world = location.getWorld();
        if (world == null) {
            world = Server.getDefaultWorld();
        }

        for (Integer orbExp : getOrbs(totalExp)) {
            ExperienceOrb expOrb = (ExperienceOrb) world.spawnEntity(location, EntityType.EXPERIENCE_ORB);
            expOrb.setExperience(orbExp);
        }
    }

    public static void dropExp(Location location, int exp, @Nullable Integer count) {
        if (count == null) {
            dropExp(location, exp);
            return;
        }

        World world = location.getWorld();
        if (world == null) {
            world = Server.getDefaultWorld();
        }

        for (int i = 0; i < count; i++) {
            final ExperienceOrb expOrb = (ExperienceOrb) world.spawnEntity(location, EntityType.EXPERIENCE_ORB);
            expOrb.setExperience(exp);
        }
    }

    public static void dropExp(Location location, Amount expAmount, @Nullable Integer count) {
        if (count == null) {
            dropExp(location, expAmount.intValue());
            return;
        }

        World world = location.getWorld();
        if (world == null) {
            world = Server.getDefaultWorld();
        }

        for (int i = 0; i < count; i++) {
            final ExperienceOrb expOrb = (ExperienceOrb) world.spawnEntity(location, EntityType.EXPERIENCE_ORB);
            expOrb.setExperience(expAmount.intValue());
        }
    }

    public static List<Integer> getOrbs(int totalXP) {
        List<Integer> orbs = new ArrayList<>();

        while (totalXP > 0) {
            int orb = getOrbValue(totalXP);
            orbs.add(orb);
            totalXP -= orb;
        }

        return orbs;
    }

    private static int getOrbValue(int xp) {
        if (xp >= 2477) return 2477;
        if (xp >= 1237) return 1237;
        if (xp >= 617) return 617;
        if (xp >= 307) return 307;
        if (xp >= 149) return 149;
        if (xp >= 73) return 73;
        if (xp >= 37) return 37;
        if (xp >= 17) return 17;
        if (xp >= 7) return 7;
        if (xp >= 3) return 3;
        if (xp >= 1) return 1;
        return 0;
    }

}
