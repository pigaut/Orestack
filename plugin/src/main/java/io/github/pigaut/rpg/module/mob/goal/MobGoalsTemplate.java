package io.github.pigaut.rpg.module.mob.goal;

import com.destroystokyo.paper.entity.ai.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MobGoalsTemplate {

    private final Set<GoalKey<?>> goals = new HashSet<>();

    public MobGoalsTemplate(@NotNull Class<? extends Mob> mobClass, List<String> goals) {
        for (String goal : goals) {
            this.goals.add(GoalKey.of(mobClass, NamespacedKey.minecraft(goal)));
        }
    }

    public void apply(@NotNull LivingEntity entity) {
        if (!(entity instanceof Mob mob)) {
            return;
        }

        // match by name only, ignore class
        Set<String> goalNames = new HashSet<>();
        for (GoalKey<?> key : goals) {
            goalNames.add(key.getNamespacedKey().getKey());
        }

        MobGoals mobGoals = Bukkit.getMobGoals();

        List<Goal<Mob>> allGoals = new ArrayList<>();
        allGoals.addAll(mobGoals.getAllGoals(mob, GoalType.MOVE));
        allGoals.addAll(mobGoals.getAllGoals(mob, GoalType.LOOK));
        allGoals.addAll(mobGoals.getAllGoals(mob, GoalType.JUMP));
        allGoals.addAll(mobGoals.getAllGoals(mob, GoalType.TARGET));
        allGoals.addAll(mobGoals.getAllGoals(mob, GoalType.UNKNOWN_BEHAVIOR));

        mobGoals.removeAllGoals(mob);

        int priority = 0;
        for (Goal<Mob> goal : allGoals) {
            if (goalNames.contains(goal.getKey().getNamespacedKey().getKey())) {
                mobGoals.addGoal(mob, priority, goal);
                priority++;
            }
        }

    }

}
