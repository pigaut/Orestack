package io.github.pigaut.rpg.listener.skill;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.event.player.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.skill.exp.*;
import io.github.pigaut.rpg.module.skill.level.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.event.farm.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.event.farm.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.skill.exp.*;
import io.github.pigaut.rpg.module.skill.level.*;
import io.github.pigaut.rpg.player.data.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class SkillEventListener implements Listener {

    private final RpgMakerPlugin plugin;

    public SkillEventListener(RpgMakerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        RpgPlayerData playerData = plugin.getPlayerData(player);
        playerData.runWhenLoaded(() -> {
            for (Skill skill : playerData.getSkills()) {
                SkillLevel level = skill.getLevel();
                if (level != null) {
                    SkillStats stats = level.getStats();
                    stats.applyAll(plugin.getPlayerState(player), skill);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (plugin.isPlayerPlacedBlock(block)) {
            return;
        }

        Player player = event.getPlayer();
        Context context = Context.fromPlayerAndBlock(plugin, player, block);

        RpgPlayerData playerData = plugin.getPlayerData(player);
        for (Skill skill : playerData.getSkills()) {
            ExpYieldFunction blockBreakExp = skill.getBlockBreakExp();
            if (blockBreakExp == null) {
                continue;
            }

            ExpAmount expAmount = blockBreakExp.yield(context);
            if (expAmount == null) {
                continue;
            }

            int totalExp = expAmount.intValue();
            context.addPlaceholder("skill", skill.getName());
            context.addPlaceholder("exp", totalExp);
            skill.increaseExp(context, totalExp);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEggCollect(PlayerCollectEggEvent event) {
        Player player = event.getPlayer();
        Context context = Context.fromPlayer(plugin, player);

        int eggs = event.getEgg().getItemStack().getAmount();

        RpgPlayerData playerData = plugin.getPlayerData(player);
        for (Skill skill : playerData.getSkills()) {
            ExpYieldFunction eggCollectExp = skill.getEggCollectExp();
            if (eggCollectExp == null) {
                continue;
            }

            ExpAmount expAmount = eggCollectExp.yield(context);
            if (expAmount == null) {
                continue;
            }

            int totalExp = expAmount.intValue() * eggs;
            context.addPlaceholder("skill", skill.getName());
            context.addPlaceholder("exp", totalExp);
            skill.increaseExp(context, totalExp);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMilkCow(PlayerMilkCowEvent event) {
        Player player = event.getPlayer();
        Context context = Context.fromPlayer(plugin, player);

        RpgPlayerData playerData = plugin.getPlayerData(player);
        for (Skill skill : playerData.getSkills()) {
            ExpYieldFunction milkCowExp = skill.getMilkCowExp();
            if (milkCowExp == null) {
                continue;
            }

            ExpAmount expAmount = milkCowExp.yield(context);
            if (expAmount == null) {
                continue;
            }

            int totalExp = expAmount.intValue();
            context.addPlaceholder("skill", skill.getName());
            context.addPlaceholder("exp", totalExp);
            skill.increaseExp(context, totalExp);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onShearSheep(PlayerShearSheepEvent event) {
        Player player = event.getPlayer();
        Context context = Context.fromPlayer(plugin, player);

        RpgPlayerData playerData = plugin.getPlayerData(player);
        for (Skill skill : playerData.getSkills()) {
            ExpYieldFunction shearSheepExp = skill.getShearSheepExp();
            if (shearSheepExp == null) {
                continue;
            }

            ExpAmount expAmount = shearSheepExp.yield(context);
            if (expAmount == null) {
                continue;
            }

            int totalExp = expAmount.intValue();
            context.addPlaceholder("skill", skill.getName());
            context.addPlaceholder("exp", totalExp);
            skill.increaseExp(context, totalExp);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        Context context = Context.fromPlayer(plugin, player);

        RpgPlayerData playerData = plugin.getPlayerData(player);
        for (Skill skill : playerData.getSkills()) {
            ExpYieldFunction enchantItemExp = skill.getEnchantItemExp();
            if (enchantItemExp == null) {
                continue;
            }

            ExpAmount expAmount = enchantItemExp.yield(context);
            if (expAmount == null) {
                continue;
            }

            int totalExp = expAmount.intValue();
            context.addPlaceholder("skill", skill.getName());
            context.addPlaceholder("exp", totalExp);
            skill.increaseExp(context, totalExp);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBrewPotion(PlayerCollectBrewedPotionEvent event) {
        Player player = event.getPlayer();
        Context context = Context.fromPlayerAndItem(plugin, player, event.getPotion());

        int amount = event.getAmount();

        RpgPlayerData playerData = plugin.getPlayerData(player);
        for (Skill skill : playerData.getSkills()) {
            ExpYieldFunction brewPotionExp = skill.getBrewPotionExp();
            if (brewPotionExp == null) {
                continue;
            }

            ExpAmount expAmount = brewPotionExp.yield(context);
            if (expAmount == null) {
                continue;
            }

            int totalExp = expAmount.intValue() * amount;
            context.addPlaceholder("skill", skill.getName());
            context.addPlaceholder("exp", totalExp);
            skill.increaseExp(context, totalExp);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKillEntity(PlayerKillEntityEvent event) {
        Player player = event.getPlayer();
        Context context = Context.fromPlayerAndEntity(plugin, player, event.getEntity());

        RpgPlayerData playerData = plugin.getPlayerData(player);
        for (Skill skill : playerData.getSkills()) {
            ExpYieldFunction entityKillExp = skill.getEntityKillExp();
            if (entityKillExp == null) {
                continue;
            }

            ExpAmount expAmount = entityKillExp.yield(context);
            if (expAmount == null) {
                continue;
            }

            int totalExp = expAmount.intValue();
            context.addPlaceholder("skill", skill.getName());
            context.addPlaceholder("exp", totalExp);
            skill.increaseExp(context, totalExp);
        }
    }

}
