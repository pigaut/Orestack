package io.github.pigaut.rpg.listener.packets;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.*;
import com.github.retrooper.packetevents.util.*;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.retrooper.packetevents.util.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.bukkit.entity.*;

public class VirtualBlockListener implements PacketListener {

    private final EnhancedPlugin plugin;

    public VirtualBlockListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
            WrapperPlayServerBlockChange wrapper = new WrapperPlayServerBlockChange(event);
            Player player = event.getPlayer();
            World world = player.getWorld();
            Vector3i blockPos = wrapper.getBlockPosition();
            BlockData virtualBlockData = plugin.getVirtualStructures().getVirtualBlockData(player, world, blockPos.x, blockPos.y, blockPos.z);
            if (virtualBlockData != null) {
                wrapper.setBlockState(SpigotConversionUtil.fromBukkitBlockData(virtualBlockData));
                event.markForReEncode(true);
            }
        }
    }
}
