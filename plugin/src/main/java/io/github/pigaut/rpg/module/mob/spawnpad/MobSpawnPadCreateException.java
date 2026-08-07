package io.github.pigaut.rpg.module.mob.spawnpad;

public class MobSpawnPadCreateException extends Exception {

    public MobSpawnPadCreateException(String message) {
        super(message);
    }

    public MobSpawnPadCreateException(String world, int x, int y, int z, String reason) {
        super(String.format("Failed to create mob spawn pad at %s, %d, %d, %d. Reason: %s.", world, x, y, z, reason));
    }

}

