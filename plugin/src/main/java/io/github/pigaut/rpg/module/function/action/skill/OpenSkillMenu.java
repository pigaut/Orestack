package io.github.pigaut.rpg.module.function.action.skill;

import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.player.data.base.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
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

        EnhancedPlayerData playerData = context.playerData();
        if (!(playerData instanceof PlayerData rpgPlayerData)) {
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