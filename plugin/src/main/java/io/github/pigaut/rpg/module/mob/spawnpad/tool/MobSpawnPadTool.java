package io.github.pigaut.rpg.module.mob.spawnpad.tool;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.core.item.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.item.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class MobSpawnPadTool extends Tool {

    private static final double DEFAULT_SPAWN_RANGE = 2.5;
    private static final double DEFAULT_ACTIVATION_RANGE = 30;

    private final EnhancedPlugin plugin;

    private final NamespacedKey MOB_TEMPLATE_KEY;
    private final NamespacedKey SPAWN_DELAY_KEY;
    private final NamespacedKey SPAWN_RANGE_KEY;
    private final NamespacedKey ACTIVATION_RANGE_KEY;

    public MobSpawnPadTool(EnhancedPlugin plugin) {
        super(plugin, "mob_spawn_pad");
        this.plugin = plugin;

        MOB_TEMPLATE_KEY = plugin.getNamespacedKey("mob_spawn_pad_template");
        SPAWN_DELAY_KEY = plugin.getNamespacedKey("mob_spawn_pad_delay");
        SPAWN_RANGE_KEY = plugin.getNamespacedKey("mob_spawn_pad_range");
        ACTIVATION_RANGE_KEY = plugin.getNamespacedKey("mob_spawn_pad_activation_range");

        setItemTemplate(new ItemStack(Material.SPAWNER));
        setItemMetaTemplate(meta -> MetaEditor.of(meta)
                .withAllFlags()
                .withName("&b&l{mob_spawn_pad_template_tc} Spawn Pad")
                .addLine("&8Use this tool to create mob")
                .addLine("&8spawn points in your worlds.")
                .addEmptyLine()
                .addLine("&8&m                                   ")
                .addLine("&aSpawn delay: &f{mob_spawn_pad_delay}")
                .addLine("&aSpawn range: &f{mob_spawn_pad_range} blocks")
                .addLine("&aActivation range: &f{mob_spawn_pad_activation_range} blocks")
                .addLine("&8&m                                   ")
                .addEmptyLine()
                .addLine("&cLeft-Click: &fTo remove spawn pad")
                .addLine("&eRight-Click: &fTo place the spawn pad")
                .addEmptyLine()
                .addLine("&6Press F: &fTo change settings")
                .addEmptyLine()
                .addLine("&c&l&o[!] &c&oMake mob spawn pads visible by")
                .addLine("&c&ousing: &f/{plugin_lc} mob spawn-pad make-visible")
        );

        onLeftClickBlock((event) -> {
            Location location = event.getClickedBlock().getLocation();
            MobSpawnPad mobSpawnPad = plugin.getMobSpawnPad(location);
            if (mobSpawnPad == null) {
                mobSpawnPad = plugin.getMobSpawnPad(location.add(0, 1, 0));
                if (mobSpawnPad == null) {
                    return;
                }
            }

            Player player = event.getPlayer();
            MobSpawnPad finalMobSpawnPad = mobSpawnPad;
            plugin.getScheduler().runTaskLater(1, () -> {
                player.sendBlockChange(location, Material.REDSTONE_BLOCK.createBlockData());
                finalMobSpawnPad.setAllowVisible(false);
            });
            plugin.getScheduler().runTaskLater(9, () -> player.sendBlockChange(location, location.getBlock().getBlockData()));

            mobSpawnPad.remove();
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "removed-mob-spawn-pad");
        });

        onRightClickBlock(event -> {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            MobTemplate mobTemplate = getMobTemplate(item);
            Delay spawnDelay = getSpawnDelay(item);
            double spawnRange = getSpawnRange(item);
            double activationRange = getActivationRange(item);

            if (mobTemplate == null) {
                plugin.sendMessage(player, Context.fromPlayer(plugin, player), "mob-not-exists");
                return;
            }

            Location location = event.getClickedBlock().getLocation();
            if (plugin.getMobSpawnPad(location) != null) {
                plugin.sendMessage(player, Context.fromPlayer(plugin, player), "mob-spawn-pad-occupied-block");
                return;
            }

            MobSpawnPad mobSpawnPad = new MobSpawnPad(plugin, mobTemplate, location, spawnDelay, spawnRange, activationRange);
            mobSpawnPad.setAllowVisible(false);
            plugin.getMobSpawnPads().register(mobSpawnPad);

            plugin.getScheduler().runTaskLater(1, () -> player.sendBlockChange(location, Material.EMERALD_BLOCK.createBlockData()));
            plugin.getScheduler().runTaskLater(9, () -> {
                player.sendBlockChange(location, location.getBlock().getBlockData());
                mobSpawnPad.setAllowVisible(true);
            });

            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "placed-mob-spawn-pad");
        });

        onSwapHand(player -> {
            PlayerState playerState = plugin.getPlayerState(player);
            PlayerInventory inventory = player.getInventory();
            playerState.openMenu(new MobSpawnPadToolEditor(this, inventory.getItemInMainHand(), inventory.getHeldItemSlot()));
        });

    }

    public @Nullable String getMobTemplateName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        return PersistentData.getString(meta, MOB_TEMPLATE_KEY);
    }

    public @Nullable MobTemplate getMobTemplate(@NotNull ItemStack item) {
        String mobTemplateName = getMobTemplateName(item);
        return mobTemplateName != null ? plugin.getMobTemplate(mobTemplateName) : null;
    }

    public @Nullable String getSpawnDelayData(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        return PersistentData.getString(meta, SPAWN_DELAY_KEY);
    }

    public @Nullable Delay getSpawnDelay(@NotNull ItemStack item) {
        String spawnDelayData = getSpawnDelayData(item);
        return spawnDelayData != null ? ParseUtil.parseDelayOrNull(spawnDelayData) : null;
    }

    public double getSpawnRange(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return DEFAULT_SPAWN_RANGE;
        }
        return PersistentData.getDoubleOrDefault(meta, SPAWN_RANGE_KEY, DEFAULT_SPAWN_RANGE);
    }

    public double getActivationRange(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return DEFAULT_ACTIVATION_RANGE;
        }
        return PersistentData.getDoubleOrDefault(meta, ACTIVATION_RANGE_KEY, DEFAULT_ACTIVATION_RANGE);
    }


    public @NotNull ItemStack createItem(@NotNull MobTemplate mobTemplate) {
        ItemStack item = getItemTemplate();
        updateItem(item, mobTemplate.getName(), null, 2.5, 30);
        return item;
    }

    public void setMobTemplate(@NotNull ItemStack item, @NotNull MobTemplate mobTemplate) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        String spawnDelayData = PersistentData.getString(meta, SPAWN_DELAY_KEY);
        double spawnRange = PersistentData.getDoubleOrDefault(meta, SPAWN_RANGE_KEY, 2.5);
        double activationRange = PersistentData.getDoubleOrDefault(meta, ACTIVATION_RANGE_KEY, 30);
        updateItem(item, mobTemplate.getName(), spawnDelayData, spawnRange, activationRange);
    }

    public void setSpawnDelay(@NotNull ItemStack item, @NotNull Delay spawnDelay) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        String mobTemplateName = PersistentData.getString(meta, MOB_TEMPLATE_KEY);
        double spawnRange = PersistentData.getDoubleOrDefault(meta, SPAWN_RANGE_KEY, 2.5);
        double activationRange = PersistentData.getDoubleOrDefault(meta, ACTIVATION_RANGE_KEY, 30);
        updateItem(item, mobTemplateName, spawnDelay.toString(), spawnRange, activationRange);
    }

    public void setSpawnRange(@NotNull ItemStack item, double spawnRange) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        String mobTemplateName = PersistentData.getString(meta, MOB_TEMPLATE_KEY);
        String spawnDelayData = PersistentData.getString(meta, SPAWN_DELAY_KEY);
        double activationRange = PersistentData.getDoubleOrDefault(meta, ACTIVATION_RANGE_KEY, 30);
        updateItem(item, mobTemplateName, spawnDelayData, spawnRange, activationRange);
    }

    public void setActivationRange(@NotNull ItemStack item, double activationRange) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            meta = getItemMetaTemplate(item);
        }

        String mobTemplateName = PersistentData.getString(meta, MOB_TEMPLATE_KEY);
        String spawnDelayData = PersistentData.getString(meta, SPAWN_DELAY_KEY);
        double spawnRange = PersistentData.getDoubleOrDefault(meta, SPAWN_RANGE_KEY, 2.5);

        updateItem(item, mobTemplateName, spawnDelayData, spawnRange, activationRange);
    }

    private void updateItem(@NotNull ItemStack item, @NotNull String mobTemplate, @Nullable String spawnDelay, double spawnRange, double activationRange) {
        ItemMeta meta = getItemMetaTemplate(item);

        PersistentData.setString(meta, MOB_TEMPLATE_KEY, mobTemplate);
        if (spawnDelay != null) {
            PersistentData.setString(meta, SPAWN_DELAY_KEY, spawnDelay);
        }
        PersistentData.setDouble(meta, SPAWN_RANGE_KEY, spawnRange);
        PersistentData.setDouble(meta, ACTIVATION_RANGE_KEY, activationRange);

        Context context = Context.builder(plugin)
                .withPlaceholder("mob_spawn_pad_template", mobTemplate)
                .withPlaceholder("mob_spawn_pad_delay", spawnDelay != null ? spawnDelay : "none")
                .withPlaceholder("mob_spawn_pad_range", spawnRange)
                .withPlaceholder("mob_spawn_pad_activation_range", activationRange)
                .build();

        PlaceholderUtil.parseAll(context, meta);

        item.setItemMeta(meta);
    }

}
