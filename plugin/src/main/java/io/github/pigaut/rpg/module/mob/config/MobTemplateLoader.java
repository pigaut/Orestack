package io.github.pigaut.rpg.module.mob.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.module.mob.disguise.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.module.mob.disguise.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MobTemplateLoader implements ConfigLoader<MobTemplate> {

    private final EnhancedPlugin plugin;

    public MobTemplateLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid mob";
    }

    @Override
    public @NotNull MobTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (!(section instanceof ConfigRoot root) || !root.hasFile()) {
            throw new IllegalArgumentException("Can only create mob template from a root section");
        }

        String name = root.getName();
        String group = Group.byMobFile(root.getFile());

        EntityType entity = section.get("type|entity", EntityType.class)
                .require(EntityType::isSpawnable, "This entity cannot be spawned")
                .orThrow();

        MobOptions options = section.getRequired(MobOptions.class);

        MobBossBarTemplate bossBarTemplate = section.get("boss-bar|bossbar|mob-bossbar|mob-boss-bar", MobBossBarTemplate.class)
                .withDefault(null);

        MobDisguiseTemplate disguiseTemplate = section.get("disguise|mob-disguise", MobDisguiseTemplate.class)
                .withDefault(null);

        Boolean defaultDrops = section.getBoolean("default-drops").withDefault(null);

        boolean defaultItemDrops = defaultDrops != null ? defaultDrops :
                section.getBoolean("default-item-drops").withDefault(false);

        boolean defaultExpDrops = defaultDrops != null ? defaultDrops :
                section.getBoolean("default-exp-drops|default-xp-drops").withDefault(false);

        List<ItemDrop> itemDrops = section.getList("item-drops|drops", ItemDrop.class)
                .withDefault(null);

        Amount expDrops = section.get("exp-drops|xp-drops|exp|xp", Amount.class)
                .withDefault(null);

        Delay attackCooldown = section.get("attack-cooldown", Delay.class)
                .withDefault(null);

        boolean leashing = section.getBoolean("leashing")
                .withDefault(false);

        boolean renaming = section.getBoolean("renaming")
                .check(Server.isPaper(), "renaming is only available on paper servers")
                .withDefault(false);

        boolean sunburn = section.getBoolean("sunburn")
                .withDefault(false);

        boolean dealDamage = section.getBoolean("deal-damage")
                .withDefault(true);

        boolean randomEquipment = section.getBoolean("random-equipment")
                .withDefault(false);

        boolean slimeSplit = section.getBoolean("slime-split")
                .withDefault(false);

        String playerSlainMessage = section.getString("player-slain-message|slain-message")
                .withDefault(null);

        Function onSpawn = section.get("on-spawn", Function.class).withDefault(null);
        Function onDamaged = section.get("on-damaged", Function.class).withDefault(null);
        Function onDeath = section.get("on-death", Function.class).withDefault(null);
        Function onHealthChange = section.get("on-health-change", Function.class).withDefault(null);

        Function onTarget = section.get("on-target", Function.class).withDefault(null);
        Function onAttack = section.get("on-attack", Function.class).withDefault(null);
        Function onKill = section.get("on-kill", Function.class).withDefault(null);
        Function onPlayerKill = section.get("on-player-kill", Function.class).withDefault(null);
        Function onJump = section.get("on-jump", Function.class).withDefault(null);
        Function onLand = section.get("on-land", Function.class).withDefault(null);

        return new MobTemplate(plugin, name, group, entity, options,
                null, bossBarTemplate, disguiseTemplate,
                defaultItemDrops, defaultExpDrops, itemDrops, expDrops, attackCooldown,
                leashing, renaming, sunburn, dealDamage, randomEquipment, slimeSplit,
                playerSlainMessage,
                onSpawn, onDamaged, onHealthChange, onDeath, onTarget, onAttack, onKill, onPlayerKill,
                onJump, onLand);
    }

}
