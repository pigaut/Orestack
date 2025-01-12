package io.github.pigaut.orestack.external;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.math.*;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public class WorldEditHook {

    public List<Location> getWorldSelection(Player player) {
        final BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
        final Region region;
        try {
            region = WorldEdit.getInstance().getSessionManager().get(bPlayer).getSelection(bPlayer.getWorld());
        } catch (IncompleteRegionException e) {
            return List.of();
        }

        final World world = region.getWorld() != null ? BukkitAdapter.adapt(region.getWorld()) : player.getWorld();

        final List<Location> selection = new ArrayList<>();
        for (BlockVector3 point : region) {
            selection.add(new Location(world, point.x(), point.y(), point.z()));
        }
        return selection;
    }

}
