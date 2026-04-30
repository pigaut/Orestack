package io.github.pigaut.orestack.listener;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.*;
import com.github.retrooper.packetevents.protocol.player.*;
import com.github.retrooper.packetevents.util.*;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.instanced.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.hook.veinminer.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.data.structure.virtual.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class PlayerPacketEventListener implements PacketListener {

    private final OrestackPlugin plugin;

    public PlayerPacketEventListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);

            DiggingAction action = wrapper.getAction();
            if (action != DiggingAction.FINISHED_DIGGING && action != DiggingAction.START_DIGGING) {
                return;
            }

            Player player = event.getPlayer();
            Vector3i blockPos = wrapper.getBlockPosition();

            Location location = new Location(player.getWorld(), blockPos.x, blockPos.y, blockPos.z);
            if (!plugin.getGenerators().isVirtualGenerator(location)) {
                return;
            }

            InstancedGenerator generator = plugin.getInstancedGenerator(player, location);
            if (generator == null) {
                return;
            }

            VirtualBlock virtualBlock = generator.getStructure().getVirtualBlock(location);
            if (virtualBlock == null) {
                return;
            }

            if (action == DiggingAction.START_DIGGING && !PlayerUtil.canInstaMine(player, virtualBlock.getBlockData().getMaterial())) {
                return;
            }

            GeneratorPhase generatorPhase = generator.getPhase();
            if (generatorPhase.getDecorativeBlocks().contains(virtualBlock.getType())) {
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

            plugin.getScheduler().runTask(() -> generator.mineBlock(player, location.getBlock(), 0));
            return;
        }
    }

}
