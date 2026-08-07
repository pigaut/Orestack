package io.github.pigaut.rpg.module.mob.faction;

import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.template.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MobFaction {

    private final String name;
    private final Set<MobTemplate> members;
    private final Set<MobTemplate> enemies;
    private final Set<MobTemplate> allies;
    private final Set<MobTemplate> indifferent;

    public MobFaction(String name, Set<MobTemplate> members, Set<MobTemplate> enemies,
                      Set<MobTemplate> allies, Set<MobTemplate> indifferent) {
        this.name = name;
        this.members = members;
        this.enemies = enemies;
        this.allies = allies;
        this.indifferent = indifferent;
    }

    public @NotNull String getName() {
        return name;
    }

    public boolean isMember(@NotNull Mob mob) {
        return members.contains(mob.getTemplate());
    }

    public boolean isEnemy(@NotNull Mob mob) {
        return enemies.contains(mob.getTemplate());
    }

    public boolean isAlly(@NotNull Mob mob) {
        return allies.contains(mob.getTemplate());
    }

    public boolean isIndifferent(@NotNull Mob mob) {
        return indifferent.contains(mob.getTemplate());
    }

    public @NotNull Set<MobTemplate> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public @NotNull Set<MobTemplate> getEnemies() {
        return Collections.unmodifiableSet(enemies);
    }

    public @NotNull Set<MobTemplate> getAllies() {
        return Collections.unmodifiableSet(allies);
    }

    public @NotNull Set<MobTemplate> getIndifferent() {
        return Collections.unmodifiableSet(indifferent);
    }

}
