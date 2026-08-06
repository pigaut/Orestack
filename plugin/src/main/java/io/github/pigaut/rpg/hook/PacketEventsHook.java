package io.github.pigaut.rpg.hook;

import com.github.retrooper.packetevents.*;
import com.github.retrooper.packetevents.event.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.listener.generator.*;
import io.github.pigaut.rpg.listener.packets.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.listener.generator.*;
import io.github.pigaut.rpg.listener.packets.*;
import org.jetbrains.annotations.*;

public class PacketEventsHook {

    public void registerPacketListener(@NotNull PacketListener packetListener, @NotNull PacketListenerPriority priority) {
        EventManager events = PacketEvents.getAPI().getEventManager();
        events.registerListener(packetListener, priority);
    }

    public static void registerAllPacketListeners(@NotNull RpgMakerPlugin plugin) {
        EventManager events = PacketEvents.getAPI().getEventManager();
        events.registerListener(new GeneratorPacketEventListener(plugin), PacketListenerPriority.NORMAL);
        events.registerListener(new VirtualBlockListener(plugin), PacketListenerPriority.NORMAL);
    }

}
