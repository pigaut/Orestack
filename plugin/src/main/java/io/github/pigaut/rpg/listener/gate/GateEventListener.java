package io.github.pigaut.rpg.listener.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.gate.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.exception.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.module.gate.tool.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.exception.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.server.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class GateEventListener implements Listener {

    private final RpgMakerPlugin plugin;

    public GateEventListener(RpgMakerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleGateBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Gate gate = plugin.getGate(block.getLocation());
        if (gate == null) {
            return;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();
        GateUtil.mine(gate, player, block);
    }

    @EventHandler
    public void handleGateBlockInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.hasItem() && plugin.isTool(event.getItem())) {
            return;
        }

        Gate gate = plugin.getGate(event.getClickedBlock().getLocation());
        if (gate == null) {
            return;
        }

        if (!gate.isValid()) {
            gate.remove();
            return;
        }

        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            if (player.hasPermission("rpg-maker.gate.build-on") && event.hasItem()
                    && !MaterialUtil.isInteractable(block.getType())) {
                event.setCancelled(false);
            }
        }

        GatePhase phase = gate.getPhase();
        if (phase.getDecorativeBlocks().contains(block.getType())) {
            return;
        }

        RpgPlayerState playerState = plugin.getPlayerState(player);
        if (playerState.hasFlag("gate:click_cooldown")) {
            return;
        }
        playerState.addTemporaryFlag("gate:click_cooldown", phase.getClickCooldown());

        GateInteractEvent gateInteractEvent = new GateInteractEvent(player, action, block,
                gate.getOrigin(), gate.getName(), gate.getState().getCurrentPhase());
        Server.callEvent(gateInteractEvent);

        Context context = Context.builder(plugin)
                .withPlayer(player)
                .withPlayerState(playerState)
                .withAction(action)
                .withTool(player.getInventory().getItemInMainHand())
                .withBlock(block)
                .withEvent(gateInteractEvent)
                .with(Gate.class, gate)
                .build();

        if (!gateInteractEvent.isCancelled()) {
            Function clickFunction = phase.getClickFunction();
            if (clickFunction != null) {
                clickFunction.run(context);
            }

            if (action == Action.LEFT_CLICK_BLOCK) {
                Function leftClickFunction = phase.getLeftClickFunction();
                if (leftClickFunction != null) {
                    leftClickFunction.run(context);
                }
            }

            if (action == Action.RIGHT_CLICK_BLOCK) {
                Function rightClickFunction = phase.getRightClickFunction();
                if (rightClickFunction != null) {
                    rightClickFunction.run(context);
                }
            }
        }
    }

}
