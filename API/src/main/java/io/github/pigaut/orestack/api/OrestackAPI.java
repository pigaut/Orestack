package io.github.pigaut.orestack.api;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface OrestackAPI {

    /**
     * Checks whether a generator with the given name exists.
     *
     * @param name the generator identifier
     * @return true if a generator with this name exists, otherwise false
     */
    boolean isGenerator(@NotNull String name);

    /**
     * Checks whether the specified location is a generator.
     *
     * @param location the location to check
     * @return true if the location is a generator, otherwise false
     */
    boolean isGenerator(@NotNull Location location);

//    /**
//     * Gets the maximum stage of the generator at the given location.
//     *
//     * @param location the generator location
//     * @return the maximum stage the generator can reach
//     * @throws IllegalArgumentException if the location is not a generator
//     */
//    int getGeneratorMaxStage(@NotNull Location location);

    /**
     * Sets the growth stage of the generator at the given location.
     *
     * @param location the generator location
     * @param stage the stage to set; must be between 0 and the generator's maximum stage
     * @throws IllegalArgumentException if the location is not a generator
     */
    void setGeneratorStage(@NotNull Location location, int stage) throws IllegalArgumentException;

    /**
     * Resets the generator at the given location to its initial state.
     *
     * @param location the generator location
     * @throws IllegalArgumentException if the location is not a generator
     */
    void regrow(@NotNull Location location) throws IllegalArgumentException;

    /**
     * Increases the generator stage by one.
     *
     * @param location the generator location
     * @throws IllegalArgumentException if the location is not a generator
     */
    void grow(@NotNull Location location) throws IllegalArgumentException;

    /**
     * Increases the generator stage by the specified amount.
     *
     * @param location the generator location
     * @param amount the number of stages to grow
     * @throws IllegalArgumentException if the location is not a generator
     */
    void grow(@NotNull Location location, int amount) throws IllegalArgumentException;

    /**
     * Harvests the generator at the given location.
     *
     * @param location the generator location
     * @throws IllegalArgumentException if the location is not a generator
     */
    void harvest(@NotNull Location location) throws IllegalArgumentException;

    /**
     * Applies damage to the generator on behalf of a player.
     *
     * @param location the generator location
     * @param player the player dealing the damage
     * @param amount the amount of damage to apply
     * @throws IllegalArgumentException if the location is not a generator
     */
    void damage(@NotNull Location location, @NotNull Player player, int amount) throws IllegalArgumentException;

}
