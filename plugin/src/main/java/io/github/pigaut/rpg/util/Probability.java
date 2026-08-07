package io.github.pigaut.rpg.util;

public class Probability {

    public static boolean test(double chance) {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }
        final double randomValue = Math.random();
        return randomValue <= chance;
    }

}
