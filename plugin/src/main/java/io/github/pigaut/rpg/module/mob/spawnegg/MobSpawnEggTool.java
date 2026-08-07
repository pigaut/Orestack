package io.github.pigaut.rpg.module.mob.spawnegg;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.item.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.core.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class MobSpawnEggTool extends Tool {

    private final EnhancedPlugin plugin;

    private final NamespacedKey MOB_TEMPLATE_KEY;

    public MobSpawnEggTool(EnhancedPlugin plugin) {
        super(plugin, "mob_spawn_egg");
        this.plugin = plugin;

        MOB_TEMPLATE_KEY = plugin.getNamespacedKey("mob_spawn_egg_template");

        setItemTemplate(new ItemStack(Material.PIG_SPAWN_EGG));
        setItemMetaTemplate(meta -> MetaEditor.of(meta)
                .withAllFlags()
                .withName("&b&l{mob_spawn_egg_template_tc} Spawn Egg")
                .addLine("&8Use this tool to spawn and")
                .addLine("&8remove mobs in your worlds.")
                .addEmptyLine()
                .addLine("&cLeft-Click: &fTo remove mob")
                .addLine("&eRight-Click: &fTo spawn mob")
        );

        onLeftClickEntity((player, entity) -> {
            Mob mob = plugin.getMob(entity);
            if (mob != null) {
                mob.remove();
                plugin.sendMessage(player, Context.fromMobAndPlayer(plugin, mob, player), "egg-removed-mob");
            }
        });

        onRightClickBlock(event -> {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            MobTemplate mobTemplate = getMobTemplate(item);
            if (mobTemplate == null) {
                plugin.sendMessage(player, Context.fromPlayer(plugin, player), "corrupt-tool-data");
                return;
            }

            Block block = event.getClickedBlock();
            Mob mob = mobTemplate.spawn(block.getRelative(event.getBlockFace()).getLocation());
            plugin.sendMessage(player, Context.fromMobAndPlayer(plugin, mob, player), "egg-spawned-mob");
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

    public @NotNull ItemStack createItem(@NotNull MobTemplate mobTemplate) {
        ItemStack item = new ItemStack(MaterialUtil.getEntitySpawnEgg(mobTemplate.getEntityType()));
        updateItem(item, mobTemplate.getName());
        return item;
    }

    public void setMobTemplate(@NotNull ItemStack item, @NotNull MobTemplate mobTemplate) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        updateItem(item, mobTemplate.getName());
    }

    private void updateItem(@NotNull ItemStack item, @NotNull String mobTemplate) {
        ItemMeta meta = getItemMetaTemplate(item);

        PersistentData.setString(meta, MOB_TEMPLATE_KEY, mobTemplate);

        Context context = Context.builder(plugin)
                .withPlaceholder("mob_spawn_egg_template", mobTemplate)
                .build();

        PlaceholderUtil.parseAll(context, meta);

        item.setItemMeta(meta);
    }

}
