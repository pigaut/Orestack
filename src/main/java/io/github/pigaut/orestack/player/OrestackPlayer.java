package io.github.pigaut.orestack.player;

import io.github.pigaut.voxel.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public class OrestackPlayer extends AbstractPluginPlayer {

    private Location firstSelection = null, secondSelection = null;

    public OrestackPlayer(UUID playerId) {
        super(playerId);
    }

    public OrestackPlayer(Player player) {
        super(player);
    }

    public Location getFirstSelection() {
        return firstSelection;
    }

    public Location getSecondSelection() {
        return secondSelection;
    }

    public void setFirstSelection(Location firstSelection) {
        this.firstSelection = firstSelection;
    }

    public void setSecondSelection(Location secondSelection) {
        this.secondSelection = secondSelection;
    }

}
