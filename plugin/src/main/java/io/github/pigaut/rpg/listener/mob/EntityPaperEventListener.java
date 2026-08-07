package io.github.pigaut.rpg.listener.mob;

import com.destroystokyo.paper.event.entity.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.papermc.paper.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class EntityPaperEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public EntityPaperEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityRename(PlayerNameEntityEvent event) {
        Mob mob = plugin.getMob(event.getEntity());
        if (mob == null) {
            return;
        }

        if (!mob.isRenaming()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityJump(EntityJumpEvent event) {
        LivingEntity entity = event.getEntity();
        Mob mob = plugin.getMob(entity);
        if (mob == null) {
            return;
        }

        Function onJump = mob.getOnJump();
        if (onJump != null) {
            onJump.run(Context.fromMob(plugin, mob, event));
        }

        Function onLand = mob.getOnLand();
        if (onLand != null) {
            new PluginRunnable(plugin) {
                private int ticks = 0;

                @Override
                public void run() {
                    if (++ticks >= 100 || !entity.isValid()) {
                        cancel();
                        return;
                    }
                    if (entity.isOnGround()) {
                        cancel();
                        onLand.run(Context.fromMob(plugin, mob));
                    }
                }
            }.runTaskTimer(1);
        }
    }

}
