package io.github.pigaut.orestack.core.action.skill;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.menu.*;
import io.github.pigaut.voxel.data.function.action.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.voxel.player.state.*;
import io.github.pigaut.voxel.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class OpenSkillMenu implements Action {

    private final EnhancedPlugin plugin;
    private final String menuName;
    private final String skillName;

    public OpenSkillMenu(@NotNull EnhancedPlugin plugin, @NotNull String menuName, @NotNull String skillName) {
        this.plugin = plugin;
        this.menuName = menuName;
        this.skillName = skillName;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        PlayerState playerState = context.playerState();
        if (player == null || playerState == null) {
            return;
        }

        PlayerData playerData = context.playerData();
        if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
            return;
        }

        context.addPlaceholder("menu-name", menuName);
        context.addPlaceholder("skill-name", skillName);

        Menu menu = plugin.getMenu(menuName);
        if (menu == null) {
            plugin.sendMessage(player, context, "menu-not-found");
            return;
        }

        Skill skill = rpgPlayerData.getSkill(skillName);
        if (skill == null) {
            plugin.sendMessage(player, context, "skill-not-found");
            return;
        }

        playerState.openMenu(menu, context.with(Skill.class, skill));
    }

}