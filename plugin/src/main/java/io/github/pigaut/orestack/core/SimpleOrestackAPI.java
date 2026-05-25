package io.github.pigaut.orestack.core;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.context.*;
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
        GlobalGenerator generator = plugin.getGlobalGenerator(location);
        return generator != null;
    }

//    @Override
//    public int getGeneratorMaxPhase(@NotNull Location location) {
//        Generator generator = getGenerator(location);
//        return generator.getMaxPhase();
//    }

    @Override
    public void setGeneratorStage(@NotNull Location location, int stage) throws IllegalArgumentException {
        setGeneratorPhase(location, stage);
    }

    @Override
    public void setGeneratorPhase(@NotNull Location location, int phase) throws IllegalArgumentException {
        GlobalGenerator generator = getGenerator(location);
        if (phase < 0 || phase > generator.getMaxPhase()) {
            throw new IllegalArgumentException("Phase is out of bounds for this generator.");
        }
        generator.setPhase(phase, true);
    }

    private @NotNull GlobalGenerator getGenerator(@NotNull Location location) throws IllegalArgumentException {
        GlobalGenerator generator = plugin.getGlobalGenerator(location);
        if (generator == null) {
            throw new IllegalArgumentException("Location does not belong to a generator.");
        }
        return generator;
    }

    @Override
    public void regrow(@NotNull Location location) throws IllegalArgumentException {
        GlobalGenerator generator = getGenerator(location);
        generator.regrow();
    }

    @Override
    public void grow(@NotNull Location location) throws IllegalArgumentException {
        GlobalGenerator generator = getGenerator(location);
        generator.grow();
    }

    @Override
    public void grow(@NotNull Location location, int amount) throws IllegalArgumentException {
        GlobalGenerator generator = getGenerator(location);
        generator.grow(amount);
    }

    @Override
    public void harvest(@NotNull Location location) throws IllegalArgumentException {
        GlobalGenerator generator = getGenerator(location);
        generator.harvest();
    }

    @Override
    public void damage(@NotNull Location location, @NotNull Player player, int amount) throws IllegalArgumentException {
        GlobalGenerator generator = getGenerator(location);
        Context context = Context.builder(plugin)
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withTool(player.getInventory().getItemInMainHand())
                .withBlock(location.getBlock())
                .build();

        generator.damage(player, context, amount);
    }

    @Override
    public boolean isGate(@NotNull String name) {
        GateTemplate gateTemplate = plugin.getGateTemplate(name);
        return gateTemplate != null;
    }

    @Override
    public boolean isGate(@NotNull Location location) {
        Gate gate = plugin.getGate(location);
        return gate != null;
    }

    private @NotNull Gate getGate(@NotNull Location location) throws IllegalArgumentException {
        Gate gate = plugin.getGate(location);
        if (gate == null) {
            throw new IllegalArgumentException("Location does not belong to a gate.");
        }
        return gate;
    }

    @Override
    public void openGate(@NotNull Location location) {
        Gate gate = getGate(location);
        gate.open();
    }

    @Override
    public void closeGate(@NotNull Location location) {
        Gate gate = getGate(location);
        gate.close();
    }

    @Override
    public void damageGate(@NotNull Location location, @NotNull Player player, int amount) throws IllegalArgumentException {

    }

}
