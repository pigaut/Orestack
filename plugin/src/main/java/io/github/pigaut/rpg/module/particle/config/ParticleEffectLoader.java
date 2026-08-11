package io.github.pigaut.rpg.module.particle.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.particle.impl.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.particle.impl.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ParticleEffectLoader implements ConfigLoader.Line<ParticleEffect> {

    private final EnhancedPlugin plugin;

    public ParticleEffectLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid particle effect";
    }

    @Override
    public @NotNull ParticleEffect loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String particleName = scalar.toString(CaseStyle.SNAKE);
        ParticleEffect foundParticle = plugin.getParticle(particleName);
        if (foundParticle != null) {
            return foundParticle;
        }

        if (scalar.isInLine()) {
            throw new InvalidConfigException(scalar, "Could not find any particle effect with name: " + particleName);
        }

        return loadFromLine(scalar.toLine());
    }

    @Override
    public @NotNull ParticleEffect loadFromLine(ConfigLine line) throws InvalidConfigException {
        String particleName = line.getKey();
        String particleGroup = Group.byParticleFile(line.getRoot().getFile());

        String type = line.getString("type", CaseStyle.CONSTANT).withDefault("BASIC");
        Amount amount = line.get("count|amount", Amount.class)
                .withDefault(Amount.fixed(1));

        BlockRange range = line.get(BlockRange.class).withDefault(BlockRange.ZERO);
        boolean playerOnly = line.getBoolean("playerOnly").withDefault(false);

        Particle particle = line.get(0, Particle.class).withDefault(ParticleUtil.CRIT);

        ParticleEffect particleEffect;
        switch (type) {
            case "BASIC" -> {
                particleEffect = new GenericParticle(particleName, particleGroup,
                        particle, amount, range, playerOnly);
            }

            case "DUST" -> {
                if (!ParticleUtil.isDust(particle)) {
                    throw new InvalidConfigException(line, "particle", "particle is not a dust");
                }

                Amount red = line.get("colorRed|red", Amount.class)
                        .withDefault(Amount.ZERO);
                Amount green = line.get("colorGreen|green", Amount.class)
                        .withDefault(Amount.ZERO);
                Amount blue = line.get("colorBlue|blue", Amount.class)
                        .withDefault(Amount.ZERO);
                Amount size = line.get("size", Amount.class).withDefault(Amount.fixed(1));
                boolean uniform = line.getBoolean("uniform|iso")
                        .withDefault(true);
                particleEffect = new DustParticle(particleName, particleGroup, particle, amount, range, playerOnly,
                        red, green, blue, size, uniform);
            }

            case "DIRECTIONAL" -> {
                if (!ParticleUtil.isDirectional(particle)) {
                    throw new InvalidConfigException(line, "particle", "particle is not directional");
                }

                BlockRange direction = line.get("direction", BlockRange.class).withDefault(BlockRange.ZERO);
                Amount speed = line.get("speed", Amount.class).orElse(Amount.fixed(1));
                particleEffect = new DirectionalParticle(particleName, particleGroup, particle, amount,
                        direction, playerOnly, speed);
            }

            case "SPELL" -> {
                if (!ParticleUtil.isEffect(particle)) {
                    throw new InvalidConfigException(line, "particle", "particle is not a spell");
                }

                Amount red = line.get("colorRed", Amount.class).orElse(Amount.ZERO);
                Amount green = line.get("colorGreen", Amount.class).orElse(Amount.ZERO);
                Amount blue = line.get("colorBlue", Amount.class).orElse(Amount.ZERO);

                if (Server.getVersion() >= Version.V1_20_6) {
                    boolean uniform = line.getBoolean("uniform|iso")
                            .orElse(true);
                    particleEffect = new SpellParticle(particleName, particleGroup, particle, amount,
                            range, playerOnly, red, green, blue, uniform);
                } else {
                    particleEffect = new SpellParticle.Legacy(particleName, particleGroup, particle, amount, playerOnly,
                            red.transform(value -> value / 255D),
                            green.transform(value -> value / 255D),
                            blue.transform(value -> value / 255D));
                }
            }

            case "NOTE" -> {
                if (!ParticleUtil.isNote(particle)) {
                    throw new InvalidConfigException(line, "particle", "particle is not a note");
                }

                Amount note = line.get("note", Amount.class)
                        .orElse(Amount.fixed(6))
                        .transform(value -> value / 24D);
                particleEffect = new NoteParticle(particleName, particleGroup, particle, amount, playerOnly, note);
            }

            case "MATERIAL" -> {
                Material material = line.get("block|item", Material.class)
                        .orElse(Material.STONE);
                Particle materialParticle = line.get(0, Particle.class)
                        .orElse(material.isItem() ? ParticleUtil.ITEM : ParticleUtil.BLOCK);

                if (ParticleUtil.isItemBreak(materialParticle)) {
                    particleEffect = new ItemParticle(particleName, particleGroup,
                            materialParticle, amount, range, playerOnly, material);
                } else if (ParticleUtil.isBlock(materialParticle)) {
                    particleEffect = new BlockParticle(particleName, particleGroup,
                            materialParticle, amount, range, playerOnly, material);
                } else {
                    throw new InvalidConfigException(line, "particle", "particle is not an item or block");
                }
            }

            case "DUST_TRANSITION" -> {
                if (Server.getVersion() < Version.V1_20_6) {
                    throw new InvalidConfigException(line, "type", "Dust Transition can be used only in 1.20.6+");
                }

                if (!ParticleUtil.isDust(particle)) {
                    throw new InvalidConfigException(line, "particle", "particle is not a dust");
                }

                Color startColor = line.get("startColor", Color.class).orElse(Color.RED);
                Color endColor = line.get("endColor", Color.class).orElse(Color.WHITE);
                Amount size = line.get("size", Amount.class).orElse(Amount.fixed(1));
                particleEffect = new DustTransitionParticle(particleName, particleGroup,
                        particle, amount, range, playerOnly, startColor, endColor, size);
            }

            default -> throw new InvalidConfigException(line, "type", "Found unknown particle type: " + type);
        }

        Amount offsetX = line.get("offsetX", Amount.class).orElse(Amount.ZERO);
        Amount offsetY = line.get("offsetY", Amount.class).orElse(Amount.ZERO);
        Amount offsetZ = line.get("offsetZ", Amount.class).orElse(Amount.ZERO);
        if (offsetX != Amount.ZERO || offsetY != Amount.ZERO || offsetZ != Amount.ZERO) {
            particleEffect = new OffsetParticle(particleEffect, offsetX, offsetY, offsetZ);
        }

        Integer repetitions = line.getInteger("repeat|repetitions")
                .require(Requirements.positive())
                .withDefault(null);

        Integer interval = line.get("interval|period", Delay.class)
                .check(repetitions != null, "repetitions must be set to use interval delay")
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (interval != null) {
            particleEffect = new PeriodicParticle(plugin, particleEffect, interval, repetitions);
        } else if (repetitions != null) {
            particleEffect = new RepeatedParticle(particleEffect, repetitions);
        }

        Integer delay = line.get("delay", Delay.class)
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (delay != null) {
            particleEffect = new DelayedParticle(plugin, particleEffect, delay);
        }

        return particleEffect;
    }

    @Override
    public @NotNull ParticleEffect loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String particleName = section.getKey();
        String particleGroup = Group.byParticleFile(section.getRoot().getFile());

        String type = section.getString("type", CaseStyle.CONSTANT).withDefault("BASIC");
        Amount amount = section.get("count|amount", Amount.class).withDefault(Amount.fixed(1));
        BlockRange range = section.get("range", BlockRange.class).withDefault(BlockRange.ZERO);
        boolean playerOnly = section.getBoolean("player-only").withDefault(false);

        ParticleEffect particleEffect;
        switch (type) {
            case "BASIC" -> {
                Particle particle = section.get("particle", Particle.class).withDefault(ParticleUtil.CRIT);
                particleEffect = new GenericParticle(particleName, particleGroup,
                        particle, amount, range, playerOnly);
            }

            case "DUST" -> {
                Particle particle = section.get("particle", Particle.class).withDefault(ParticleUtil.DUST);
                if (!ParticleUtil.isDust(particle)) {
                    throw new InvalidConfigException(section, "particle", "particle is not a dust");
                }

                Amount red = section.get("color.red|r", Amount.class).withDefault(Amount.ZERO);
                Amount green = section.get("color.green|g", Amount.class).withDefault(Amount.ZERO);
                Amount blue = section.get("color.blue|b", Amount.class).withDefault(Amount.ZERO);
                Amount size = section.get("size", Amount.class).withDefault(Amount.fixed(1));
                boolean uniform = section.getBoolean("uniform|iso").withDefault(true);
                particleEffect = new DustParticle(particleName, particleGroup, particle, amount, range, playerOnly,
                        red, green, blue, size, uniform);
            }

            case "DIRECTIONAL" -> {
                Particle particle = section.get("particle", Particle.class).withDefault(ParticleUtil.FLAME);

                if (!ParticleUtil.isDirectional(particle)) {
                    throw new InvalidConfigException(section, "particle", "particle is not directional");
                }

                BlockRange direction = section.get("direction", BlockRange.class).withDefault(BlockRange.ZERO);
                Amount speed = section.get("speed", Amount.class).orElse(Amount.fixed(1));
                particleEffect = new DirectionalParticle(particleName, particleGroup, particle, amount,
                        direction, playerOnly, speed);
            }

            case "SPELL" -> {
                Particle particle = section.get("particle", Particle.class)
                        .withDefault(ParticleUtil.EFFECT);

                if (!ParticleUtil.isEffect(particle)) {
                    throw new InvalidConfigException(section, "particle", "particle is not a spell");
                }

                if (Server.getVersion() >= Version.V1_20_6) {
                    Amount red = section.get("color.red", Amount.class).orElse(Amount.ZERO);
                    Amount green = section.get("color.green", Amount.class).orElse(Amount.ZERO);
                    Amount blue = section.get("color.blue", Amount.class).orElse(Amount.ZERO);
                    boolean uniform = section.getBoolean("uniform|iso").orElse(true);
                    particleEffect = new SpellParticle(particleName, particleGroup, particle, amount,
                            range, playerOnly, red, green, blue, uniform);
                }
                else {
                    Amount red = section.get("color.red", Amount.class).orElse(Amount.ZERO);
                    Amount green = section.get("color.green", Amount.class).orElse(Amount.ZERO);
                    Amount blue = section.get("color.blue", Amount.class).orElse(Amount.ZERO);
                    particleEffect = new SpellParticle.Legacy(particleName, particleGroup, particle, amount, playerOnly,
                            red.transform(value -> value / 255D), green.transform(value -> value / 255D), blue.transform(value -> value / 255D));
                }
            }

            case "NOTE" -> {
                Particle particle = section.get("particle", Particle.class).withDefault(ParticleUtil.NOTE);
                if (!ParticleUtil.isNote(particle)) {
                    throw new InvalidConfigException(section, "particle", "particle is not a note");
                }

                Amount note = section.get("note", Amount.class).orElse(Amount.fixed(6))
                        .transform(value -> value / 24D);
                particleEffect = new NoteParticle(particleName, particleGroup, particle, amount, playerOnly, note);
            }

            case "MATERIAL" -> {
                Material material = section.get("block|item", Material.class).orElse(Material.STONE);
                Particle particle = section.get("particle", Particle.class)
                        .withDefault(material.isItem() ? ParticleUtil.ITEM : ParticleUtil.BLOCK);

                if (ParticleUtil.isItemBreak(particle)) {
                    particleEffect = new ItemParticle(particleName, particleGroup,
                            particle, amount, range, playerOnly, material);
                }
                else if (ParticleUtil.isBlock(particle)) {
                    particleEffect = new BlockParticle(particleName, particleGroup,
                            particle, amount, range, playerOnly, material);
                }
                else {
                    throw new InvalidConfigException(section, "particle", "particle is not an item or block");
                }
            }

            case "DUST_TRANSITION" -> {
                if (Server.getVersion() < Version.V1_20_6) {
                    throw new InvalidConfigException(section, "type", "Dust Transition can be used only in 1.20.6+");
                }

                Particle particle = section.get("particle", Particle.class).withDefault(ParticleUtil.DUST);
                if (!ParticleUtil.isDust(particle)) {
                    throw new InvalidConfigException(section, "particle", "particle is not a dust");
                }

                Color startColor = section.get("start-color", Color.class).orElse(Color.RED);
                Color endColor = section.get("end-color", Color.class).orElse(Color.WHITE);
                Amount size = section.get("size", Amount.class).orElse(Amount.fixed(1));
                particleEffect = new DustTransitionParticle(particleName, particleGroup,
                        particle, amount, range, playerOnly, startColor, endColor, size);
            }

            default -> throw new InvalidConfigException(section, "type", "Found unknown particle type: '" + type + "'");
        }

        Amount offsetX = section.get("offset.x", Amount.class).orElse(Amount.ZERO);
        Amount offsetY = section.get("offset.y", Amount.class).orElse(Amount.ZERO);
        Amount offsetZ = section.get("offset.z", Amount.class).orElse(Amount.ZERO);
        if (offsetX != Amount.ZERO || offsetY != Amount.ZERO || offsetZ != Amount.ZERO) {
            particleEffect = new OffsetParticle(particleEffect, offsetX, offsetY, offsetZ);
        }

        Integer repetitions = section.getInteger("repeat|repetitions")
                .require(Requirements.positive())
                .withDefault(null);

        Integer interval = section.get("interval|period", Delay.class)
                .check(repetitions != null, "repetitions must be set to use interval delay")
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (interval != null) {
            particleEffect = new PeriodicParticle(plugin, particleEffect, interval, repetitions);
        }
        else if (repetitions != null) {
            particleEffect = new RepeatedParticle(particleEffect, repetitions);
        }

        Integer delay = section.get("delay", Delay.class)
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (delay != null) {
            particleEffect = new DelayedParticle(plugin, particleEffect, delay);
        }

        return particleEffect;
    }

    @Override
    public @NotNull ParticleEffect loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        String particleName = sequence.getKey();
        String particleGroup = Group.byParticleFile(sequence.getRoot().getFile());
        return new MultiParticle(particleName, particleGroup, sequence.getAllRequired(ParticleEffect.class));
    }

}
