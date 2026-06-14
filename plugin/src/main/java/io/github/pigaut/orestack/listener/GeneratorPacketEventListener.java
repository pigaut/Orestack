package io.github.pigaut.orestack.listener;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.*;
import com.github.retrooper.packetevents.protocol.player.*;
import com.github.retrooper.packetevents.util.*;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.instanced.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.hook.veinminer.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.data.structure.virtual.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.*;

public class GeneratorPacketEventListener implements PacketListener {

    private final OrestackPlugin plugin;
    private final Map<UUID, Long> startTimes = new HashMap<>();

    public GeneratorPacketEventListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);
            Player player = event.getPlayer();

            Vector3i blockPosition = wrapper.getBlockPosition();
            Location targetLocation = new Location(player.getWorld(), blockPosition.x, blockPosition.y, blockPosition.z);

            DiggingAction action = wrapper.getAction();
            if (!plugin.getGenerators().isVirtualGenerator(targetLocation)) {
                return;
            }

            if (action != DiggingAction.FINISHED_DIGGING && action != DiggingAction.START_DIGGING) {
                startTimes.remove(player.getUniqueId());
                return;
            }

            InstancedGenerator generator = plugin.getInstancedGenerator(player, targetLocation);
            if (generator == null) {
                return;
            }

            VirtualBlock virtualBlock = generator.getStructure().getVirtualBlock(targetLocation);
            if (virtualBlock == null) {
                return;
            }

            if (!PlayerUtil.canInstaMine(player, virtualBlock.getBlockData().getMaterial())) {
                long currentTime = System.currentTimeMillis();

                if (action == DiggingAction.START_DIGGING) {
                    event.setCancelled(true);
                    startTimes.put(player.getUniqueId(), currentTime);
                    return;
                }

                Long startTime = startTimes.get(player.getUniqueId());
                if (startTime == null) {
                    return;
                }

                long realBreakTime = currentTime - startTime;
                long calculatedBreakTime = MaterialUtil.calculateBreakTime(player, virtualBlock.getBlockData());

//                 player.sendMessage(String.format("§b[Debug] Timing: Real: %dms, Calc: %dms, Diff: %dms",
//                    realBreakTime, calculatedBreakTime, (calculatedBreakTime - realBreakTime)));

                if (calculatedBreakTime - realBreakTime > 150) {
                    return;
                }

                startTimes.put(player.getUniqueId(), currentTime);
            }

            GeneratorPhase generatorPhase = generator.getPhase();
            if (generatorPhase.getDecorativeBlocks().contains(virtualBlock.getType())) {
                return;
            }

            Location eyeLocation = player.getEyeLocation();
            Location blockCenter = LocationUtil.centered(targetLocation);
            double distanceSquared = eyeLocation.distanceSquared(blockCenter);
            if (distanceSquared > 36) {
                return;
            }

            Vector directionToBlock = blockCenter.toVector().subtract(eyeLocation.toVector()).normalize();
            Vector playerDirection = eyeLocation.getDirection();
            double dot = directionToBlock.dot(playerDirection);
            if (dot < 0.70) {
                return;
            }

            OrestackSettings settings = plugin.getSettings();
            if (settings.isVeinMiner()) {
                int maxVeinSize = settings.getToolMaxVeinSize(player.getInventory().getItemInMainHand());
                if (maxVeinSize > 1) {
                    plugin.getScheduler().runTask(() ->
                            GeneratorBlockVein.mineBlocks(generator, player, maxVeinSize, 0));
                    return;
                }
            }

            plugin.getScheduler().runTask(() -> generator.mineBlock(player, targetLocation.getBlock(), 0));
        }
    }

}
