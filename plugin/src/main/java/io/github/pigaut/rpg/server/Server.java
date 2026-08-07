package io.github.pigaut.rpg.server;

import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public final class Server {

    private Server() {
    }

    private static String versionName;
    private static Integer version;

    public static int getVersion() {
        if (version == null) {
            getVersionName();
            String versionId = "V" + versionName.replaceAll("\\.", "_");
            int version;
            try {
                version = Reflect.onClass(Version.class)
                        .field(versionId)
                        .get();
            } catch (ReflectException e) {
                version = Version.UNKNOWN;
            }
            Server.version = version;
        }
        return version;
    }

    public static String getVersionName() {
        if (versionName == null) {
            if (isPaper()) {
                versionName = Bukkit.getMinecraftVersion();
            } else {
                versionName = Bukkit.getBukkitVersion().split("-")[0];
            }
        }
        return versionName;
    }

    public static int getNMSVersion() {
        return Version.getNMSVersion(getVersion());
    }

    private static Boolean paper;
    private static Boolean folia;

    public static boolean isPaper() {
        if (paper == null) {
            paper = Reflect.matchClass("io.papermc.paper.configuration.Configuration") ||
                    Reflect.matchClass("com.destroystokyo.paper.PaperConfig");
        }
        return paper;
    }

    public static boolean isFolia() {
        if (folia == null) {
            folia = Reflect.matchClass("io.papermc.paper.threadedregions.RegionizedServer");
        }
        return folia;
    }

    private static World defaultWorld;

    public static @NotNull World getDefaultWorld() {
        if (defaultWorld == null) {
            defaultWorld = Bukkit.getWorlds().get(0);
        }
        return defaultWorld;
    }

    public static List<String> getOnlinePlayerNames() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName).toList();
    }

    public static List<String> getWorldNames() {
        return Bukkit.getWorlds().stream()
                .map(World::getName)
                .toList();
    }

    public static @Nullable EconomyHook getEconomyHook() {
        return isPluginLoaded("Vault") ? EconomyHook.newInstance() : null;
    }

    public static @Nullable PlaceholdersHook getPlaceholderAPIHook() {
        return isPluginLoaded("PlaceholderAPI") ? new PlaceholdersHook() : null;
    }

    public static @Nullable PacketEventsHook getPacketEventsHook() {
        return isPluginLoaded("packetevents") ? new PacketEventsHook() : null;
    }

    public static @Nullable Plugin getPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name);
    }

    public static @Nullable String getPluginVersion(String name) {
        final Plugin plugin = getPlugin(name);
        if (plugin == null) {
            return null;
        }
        return plugin.getDescription().getVersion();
    }

    public static boolean isPluginLoaded(String name) {
        return getPlugin(name) != null;
    }

    public static boolean isPluginEnabled(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    @Nullable
    public static <T> T getRegisteredService(Class<T> service) {
        RegisteredServiceProvider<T> registration = Bukkit.getServer().getServicesManager().getRegistration(service);
        return registration != null ? registration.getProvider() : null;
    }

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static List<String> getWorldFolderNames() {
        File worldFolder = Bukkit.getServer().getWorldContainer();

        List<String> worlds = new ArrayList<>();

        if (!worldFolder.isDirectory()) {
            return worlds;
        }

        File[] children = worldFolder.listFiles();
        if (children == null) {
            return worlds;
        }

        for (File child : children) {
            if (!child.isDirectory()) continue;

            File levelDat = new File(child, "level.dat");
            if (levelDat.exists() && levelDat.isFile()) {
                worlds.add(child.getName());
            }
        }

        return worlds;
    }

    public static void registerEventAtFirstOfLowestPriority(HandlerList handlerList, Listener listener,
                                                            EventExecutor executor, Plugin plugin, boolean ignoreCancelled) {

        try {
            RegisteredListener registeredListener = new RegisteredListener(listener, executor, EventPriority.LOWEST,
                    plugin, ignoreCancelled);

            synchronized (handlerList) {
                Field handlerslotsField = HandlerList.class.getDeclaredField("handlerslots");
                handlerslotsField.setAccessible(true);

                @SuppressWarnings("unchecked")
                EnumMap<EventPriority, ArrayList<RegisteredListener>> slots =
                        (EnumMap<EventPriority, ArrayList<RegisteredListener>>) handlerslotsField.get(handlerList);

                ArrayList<RegisteredListener> lowestList = slots.get(EventPriority.LOWEST);

                if (lowestList.contains(registeredListener)) {
                    throw new IllegalStateException("This listener is already registered to priority LOWEST");
                }

                lowestList.add(0, registeredListener);

                Field handlersField = HandlerList.class.getDeclaredField("handlers");
                handlersField.setAccessible(true);
                handlersField.set(handlerList, null);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
