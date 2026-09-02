package io.github.pigaut.rpg.module.function.action.protagonist;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.util.*;
import org.jetbrains.annotations.*;

public class TeleportForward implements ProtagonistAction.Executor {

    private static final double WALL_MARGIN = 0.3;
    private static final double MIN_TRAVEL = 0.5;
    private static final int MAX_NUDGE_UP = 2;

    private final Amount distance;

    public TeleportForward(@NotNull Amount distance) {
        this.distance = distance;
    }

    @Override
    public void execute(@NotNull LivingEntity entity) {
        Location eye = entity.getEyeLocation();
        World world = eye.getWorld();
        Vector dir = eye.getDirection();

        double travel = distance.doubleValue();

        RayTraceResult hit = world.rayTraceBlocks(eye, dir, travel, FluidCollisionMode.NEVER, true);
        if (hit != null) {
            travel = eye.toVector().distance(hit.getHitPosition()) - WALL_MARGIN;
        }

        if (travel < MIN_TRAVEL) {
            return;
        }

        double eyeHeight = entity.getEyeHeight();
        double destX = eye.getX() + dir.getX() * travel;
        double destY = eye.getY() + dir.getY() * travel - eyeHeight;
        double destZ = eye.getZ() + dir.getZ() * travel;

        double safeY = findSafeY(world, destX, destY, destZ, entity.getHeight());
        if (Double.isNaN(safeY)) {
            return;
        }

        Location destination = new Location(world, destX, safeY, destZ, eye.getYaw(), eye.getPitch());
        entity.teleport(destination);
    }

    private double findSafeY(@NotNull World world, double x, double y, double z, double playerHeight) {
        for (int nudge = 0; nudge <= MAX_NUDGE_UP; nudge++) {
            double candidateY = y + nudge;
            if (hasClearance(world, x, candidateY, z, playerHeight)) {
                return candidateY;
            }
        }
        return Double.NaN;
    }

    private boolean hasClearance(@NotNull World world, double x, double y, double z, double playerHeight) {
        int blocksNeeded = (int) Math.ceil(playerHeight);

        for (int i = 0; i < blocksNeeded; i++) {
            Block block = world.getBlockAt((int) Math.floor(x), (int) Math.floor(y) + i, (int) Math.floor(z));
            if (!block.isPassable()) {
                return false;
            }
        }

        return true;
    }

}