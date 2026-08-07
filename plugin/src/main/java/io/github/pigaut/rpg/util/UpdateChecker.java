package io.github.pigaut.rpg.util;

import com.google.common.base.*;
import io.github.pigaut.rpg.plugin.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

public class UpdateChecker implements Listener {

    private static final Pattern VERSION_PATTER = Pattern.compile("\\d+\\.\\d+(\\.\\d+)?(\\.\\d+)?");

    private final EnhancedJavaPlugin plugin;
    private final int resourceId;
    private String updatedVersion = null;

    public UpdateChecker(@NotNull EnhancedJavaPlugin plugin, int resourceId) {
        Preconditions.checkArgument(isValidVersion(plugin.getVersion()), "Invalid plugin version format.");
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public static boolean isValidVersion(String version) {
        return version != null && VERSION_PATTER.matcher(version).matches();
    }

    public static boolean isNewerVersionAvailable(String currentVersion, String latestVersion) {
        if (!isValidVersion(latestVersion)) {
            return false;
        }

        String[] currentParts = currentVersion.split("\\.");
        String[] latestParts = latestVersion.split("\\.");

        int maxLength = Math.max(currentParts.length, latestParts.length);

        for (int i = 0; i < maxLength; i++) {
            int current = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int latest = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;

            if (latest > current) {
                return true;
            }
            else if (latest < current) {
                return false;
            }
        }

        return false;
    }

    public boolean isUpdateAvailable() {
        return updatedVersion != null;
    }

    public void checkForUpdates() {
        getVersion(latestVersion -> {
            if (!isValidVersion(latestVersion)) {
                plugin.getLogger().warning("Unable to check for updates: received an invalid version format (" + latestVersion + ")");
                return;
            }

            if (isNewerVersionAvailable(plugin.getVersion(), latestVersion)) {
                updatedVersion = latestVersion;
                plugin.getLogger().warning("Found " + plugin.getName() + " v" + updatedVersion + " update: https://www.spigotmc.org/resources/" + resourceId + "/updates");
            }
            else {
                plugin.getLogger().info("You are running the latest " + plugin.getName() + " version.");
            }
        });
    }

    public void getVersion(Consumer<String> consumer) {
        plugin.getScheduler().runTaskAsync(() -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext() && consumer != null) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().warning("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (updatedVersion == null) {
            return;
        }

        Player player = event.getPlayer();
        if (player.hasPermission(plugin.getPermission("update.notify"))) {
            plugin.getScheduler().runTaskLater(300, () -> {
                TextComponent message = new TextComponent("Found " + plugin.getName() + " v" + updatedVersion + " update. ");
                message.setColor(ChatColor.YELLOW);

                TextComponent download = new TextComponent("[Click to view]");
                download.setColor(ChatColor.AQUA);
                download.setClickEvent(new ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://www.spigotmc.org/resources/" + resourceId + "/updates"
                ));
                download.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Open the update page").create()
                ));

                player.spigot().sendMessage(message, download);
            });
        }
    }

}
