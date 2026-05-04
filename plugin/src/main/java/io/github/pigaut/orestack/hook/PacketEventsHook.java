package io.github.pigaut.orestack.hook;

import com.github.retrooper.packetevents.*;
import com.github.retrooper.packetevents.event.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.listener.*;
import io.github.pigaut.voxel.listener.packets.*;
import org.jetbrains.annotations.*;

public class PacketEventsHook {

    public static void registerAllPacketListeners(@NotNull OrestackPlugin plugin) {
        EventManager events = PacketEvents.getAPI().getEventManager();
        events.registerListener(new PlayerPacketEventListener(plugin), PacketListenerPriority.NORMAL);
        events.registerListener(new VirtualBlockListener(plugin), PacketListenerPriority.NORMAL);
    }

}
