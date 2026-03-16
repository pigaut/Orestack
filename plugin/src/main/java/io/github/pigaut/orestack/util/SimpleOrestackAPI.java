package io.github.pigaut.orestack.util;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SimpleOrestackAPI implements OrestackAPI {

    private final OrestackPlugin plugin;

    public SimpleOrestackAPI(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isGenerator(@NotNull String name) {
        GeneratorTemplate generator = plugin.getGeneratorTemplate(name);
        return generator != null;
    }

    @Override
    public boolean isGenerator(@NotNull Location location) {
        Generator generator = plugin.getGenerator(location);
        return generator != null;
    }

//    @Override
//    public int getGeneratorMaxStage(@NotNull Location location) {
//        Generator generator = getGenerator(location);
//        return generator.getMaxStage();
//    }

    @Override
    public void setGeneratorStage(@NotNull Location location, int stage) throws IllegalArgumentException {
        Generator generator = getGenerator(location);
        if (stage < 0 || stage > generator.getMaxStage()) {
            throw new IllegalArgumentException("Stage is out of bounds for this generator.");
        }
        generator.setStage(stage);
    }

    private @NotNull Generator getGenerator(@NotNull Location location) throws IllegalArgumentException {
        Generator generator = plugin.getGenerator(location);
        if (generator == null) {
            throw new IllegalArgumentException("Location does not belong to a generator.");
        }
        return generator;
    }

    @Override
    public void regrow(@NotNull Location location) throws IllegalArgumentException {
        Generator generator = getGenerator(location);
        generator.regrow();
    }

    @Override
    public void grow(@NotNull Location location) throws IllegalArgumentException {
        Generator generator = getGenerator(location);
        generator.grow();
    }

    @Override
    public void grow(@NotNull Location location, int amount) throws IllegalArgumentException {
        Generator generator = getGenerator(location);
        generator.grow(amount);
    }

    @Override
    public void harvest(@NotNull Location location) throws IllegalArgumentException {
        Generator generator = getGenerator(location);
        generator.harvest();
    }

    @Override
    public void damage(@NotNull Location location, @NotNull Player player, int amount) throws IllegalArgumentException {
        Generator generator = getGenerator(location);
        PlayerState playerState = plugin.getPlayerState(player);
        generator.damage(playerState, amount);
    }

}
