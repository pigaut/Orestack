package io.github.pigaut.orestack.skill;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.orestack.skill.template.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.player.data.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerSkillRepository implements PlayerDataRepository<SimpleRpgPlayerData> {

    private final OrestackPlugin plugin;

    public PlayerSkillRepository(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    private void createSkillTable(Database database) {
        database.createTableIfNotExists("player_skills",
                "uuid VARCHAR(36) NOT NULL",
                "skill VARCHAR(255) NOT NULL",
                "exp LONG NOT NULL",
                "PRIMARY KEY (uuid, skill)"
        );
    }

    @Override
    public void loadData(@NotNull SimpleRpgPlayerData playerData) {
        Database database = plugin.getDatabase();
        if (database == null) {
            return;
        }

        createSkillTable(database);

        UUID playerId = playerData.getUniqueId();
        String uuidString = playerId.toString();

        Set<Skill> skills = new HashSet<>();
        database.createStatement("SELECT skill, exp FROM player_skills WHERE uuid = ?")
                .withParameter(uuidString)
                .fetchAllRows(row -> {
                    String skillName = row.getString("skill");

                    SkillTemplate skillTemplate = plugin.getSkillTemplate(skillName);
                    if (skillTemplate == null) {
                        plugin.getColoredLogger().warning("Could not load player skill. " +
                                "Reason: could not find skill with name: " + skillName);
                        return;
                    }

                    int totalExp = row.getInt("exp");
                    if (totalExp < 0) {
                        plugin.getColoredLogger().warning("Could not load player skill exp for: " +
                                skillName + ". Applying default exp of 0.");
                        totalExp = 0;
                    }

                    skills.add(new Skill(skillTemplate, totalExp));
                });

        // Add missing skills
        for (SkillTemplate skillTemplate : plugin.getSkillTemplates().getAll()) {
            boolean foundSkill = false;
            for (Skill skill : skills) {
                if (skill.getName().equals(skillTemplate.getName())) {
                    foundSkill = true;
                    break;
                }
            }
            if (!foundSkill) {
                skills.add(new Skill(skillTemplate, 0));
            }
        }

        playerData.setSkills(skills);
    }

    @Override
    public void saveData(@NotNull SimpleRpgPlayerData playerData) {
        Database database = plugin.getDatabase();
        if (database == null) {
            return;
        }

        createSkillTable(database);

        String uuidString = playerData.getUniqueId().toString();

        database.createStatement("DELETE FROM player_skills WHERE uuid = ?")
                .withParameter(uuidString)
                .executeUpdate();

        DatabaseStatement insertStatement = database.insert("player_skills", "uuid", "skill", "exp");
        for (Skill skill : playerData.getSkills()) {
            insertStatement.withParameter(uuidString);
            insertStatement.withParameter(skill.getName());
            insertStatement.withParameter(skill.getTotalExp());
            insertStatement.addBatch();
        }
        insertStatement.executeBatch();
    }
}