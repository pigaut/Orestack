package io.github.pigaut.rpg.module.particle.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.particle.type.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.line.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

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
            throw new InvalidConfigException(scalar, "Could not find any particle effect with name: " + scalar);
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

                ConfigLine colorLine = section.getLineOrEmpty("color", LineStyle.SPACED);
                Amount red = colorLine.get("red|r", Amount.class).withDefault(Amount.ZERO);
                Amount green = colorLine.get("green|g", Amount.class).withDefault(Amount.ZERO);
                Amount blue = colorLine.get("blue|b", Amount.class).withDefault(Amount.ZERO);

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
                    ConfigLine colorLine = section.getLineOrEmpty("color", LineStyle.SPACED);
                    Amount red = colorLine.get("red|r", Amount.class).withDefault(Amount.ZERO);
                    Amount green = colorLine.get("green|g", Amount.class).withDefault(Amount.ZERO);
                    Amount blue = colorLine.get("blue|b", Amount.class).withDefault(Amount.ZERO);

                    boolean uniform = section.getBoolean("uniform|iso").orElse(true);
                    particleEffect = new SpellParticle(particleName, particleGroup, particle, amount,
                            range, playerOnly, red, green, blue, uniform);
                }
                else {
                    ConfigLine colorLine = section.getLineOrEmpty("color", LineStyle.SPACED);
                    Amount red = colorLine.get("red|r", Amount.class).withDefault(Amount.ZERO);
                    Amount green = colorLine.get("green|g", Amount.class).withDefault(Amount.ZERO);
                    Amount blue = colorLine.get("blue|b", Amount.class).withDefault(Amount.ZERO);
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

        ConfigLine shapeLine = section.getLineOrEmpty("shape");
        String shape = shapeLine.getString(0, CaseStyle.CONSTANT).withDefault(null);

        ConfigLine offsetLine = section.getLineOrEmpty("offset");
        Amount offsetX = offsetLine.get("x", Amount.class).orElse(Amount.ZERO);
        Amount offsetY = offsetLine.get("y", Amount.class).orElse(Amount.ZERO);
        Amount offsetZ = offsetLine.get("z", Amount.class).orElse(Amount.ZERO);

        if (shape != null) {
            switch (shape) {
                case "CIRCLE" -> {
                    double radius = shapeLine.getDouble("radius").withDefault(3.0);
                    double density = shapeLine.getDouble("density").withDefault(0.5);
                    boolean filled = shapeLine.getBoolean("filled").withDefault(false);

                    List<ParticleEffect> circlePoints = new ArrayList<>();

                    if (filled) {
                        int ringCount = Math.max(1, (int) Math.round(radius / density) + 1);
                        for (int r = 0; r < ringCount; r++) {
                            double currentRadius = r * density;
                            addCircleRing(circlePoints, particleEffect, offsetX, offsetY, offsetZ, currentRadius, density);
                        }
                    } else {
                        addCircleRing(circlePoints, particleEffect, offsetX, offsetY, offsetZ, radius, density);
                    }

                    particleEffect = new MultiParticle(particleName, particleGroup, circlePoints);
                }

                case "LINE" -> {
                    double density = shapeLine.getDouble("density").withDefault(0.5);
                    double length = shapeLine.getDouble("length").withDefault(3.0);
                    double thickness = shapeLine.getDouble("thickness").withDefault(0.0);

                    int lengthPoints = Math.max(1, (int) Math.round(length / density) + 1);
                    int thicknessPoints = Math.max(1, (int) Math.round(thickness / density) + 1);

                    List<ParticleEffect> linePoints = new ArrayList<>();
                    for (int i = 0; i < lengthPoints; i++) {
                        double pointX = -length / 2 + i * density;

                        for (int j = 0; j < thicknessPoints; j++) {
                            double pointZ = thicknessPoints == 1 ? 0 : -thickness / 2 + j * density;

                            Amount pointOffsetX = offsetX.transform(v -> v + pointX);
                            Amount pointOffsetY = offsetY.transform(v -> v);
                            Amount pointOffsetZ = offsetZ.transform(v -> v + pointZ);

                            linePoints.add(new OffsetParticle(particleEffect, pointOffsetX, pointOffsetY, pointOffsetZ));
                        }
                    }

                    particleEffect = new MultiParticle(particleName, particleGroup, linePoints);
                }

                case "RING" -> {
                    double density = shapeLine.getDouble("density").withDefault(0.5);
                    double radius = shapeLine.getDouble("radius").withDefault(3.0);
                    double thickness = shapeLine.getDouble("thickness").withDefault(0.5);

                    double innerRadius = Math.max(0, radius - thickness / 2);
                    double outerRadius = radius + thickness / 2;
                    int ringCount = Math.max(1, (int) Math.round((outerRadius - innerRadius) / density) + 1);

                    List<ParticleEffect> ringPoints = new ArrayList<>();
                    for (int r = 0; r < ringCount; r++) {
                        double currentRadius = ringCount == 1 ? radius : innerRadius + r * density;
                        double circumference = 2 * Math.PI * currentRadius;
                        int points = Math.max(1, (int) Math.round(circumference / density));

                        for (int i = 0; i < points; i++) {
                            double theta = 2 * Math.PI * i / points;
                            double pointX = currentRadius * Math.cos(theta);
                            double pointZ = currentRadius * Math.sin(theta);

                            Amount pointOffsetX = offsetX.transform(v -> v + pointX);
                            Amount pointOffsetY = offsetY.transform(v -> v);
                            Amount pointOffsetZ = offsetZ.transform(v -> v + pointZ);

                            ringPoints.add(new OffsetParticle(particleEffect, pointOffsetX, pointOffsetY, pointOffsetZ));
                        }
                    }

                    particleEffect = new MultiParticle(particleName, particleGroup, ringPoints);
                }

                case "SQUARE" -> {
                    double density = shapeLine.getDouble("density").withDefault(0.5);
                    double length = shapeLine.getDouble("length").withDefault(3.0);
                    boolean filled = shapeLine.getBoolean("filled").withDefault(false);

                    double half = length / 2;
                    int pointsPerSide = Math.max(1, (int) Math.round(length / density) + 1);

                    List<ParticleEffect> squarePoints = new ArrayList<>();

                    if (filled) {
                        for (int i = 0; i < pointsPerSide; i++) {
                            double pointX = -half + i * density;

                            for (int j = 0; j < pointsPerSide; j++) {
                                double pointZ = -half + j * density;
                                addPoint(squarePoints, particleEffect, offsetX, offsetY, offsetZ, pointX, 0, pointZ);
                            }
                        }
                    } else {
                        for (int i = 0; i < pointsPerSide; i++) {
                            double offset = -half + i * density;
                            addPoint(squarePoints, particleEffect, offsetX, offsetY, offsetZ, offset, 0, -half);
                            addPoint(squarePoints, particleEffect, offsetX, offsetY, offsetZ, offset, 0, half);
                            addPoint(squarePoints, particleEffect, offsetX, offsetY, offsetZ, -half, 0, offset);
                            addPoint(squarePoints, particleEffect, offsetX, offsetY, offsetZ, half, 0, offset);
                        }
                    }

                    particleEffect = new MultiParticle(particleName, particleGroup, squarePoints);
                }

                case "SPHERE" -> {
                    double density = shapeLine.getDouble("density").withDefault(0.5);
                    double radius = shapeLine.getDouble("radius").withDefault(3.0);
                    boolean filled = shapeLine.getBoolean("filled").withDefault(false);

                    List<ParticleEffect> spherePoints = new ArrayList<>();

                    if (filled) {
                        int shellCount = Math.max(1, (int) Math.round(radius / density) + 1);
                        for (int s = 0; s < shellCount; s++) {
                            double shellRadius = shellCount == 1 ? radius : s * density;
                            addSphereShell(spherePoints, particleEffect, offsetX, offsetY, offsetZ, shellRadius, density);
                        }
                    } else {
                        addSphereShell(spherePoints, particleEffect, offsetX, offsetY, offsetZ, radius, density);
                    }

                    particleEffect = new MultiParticle(particleName, particleGroup, spherePoints);
                }

                case "CUBE" -> {
                    double density = shapeLine.getDouble("density").withDefault(0.5);
                    double length = shapeLine.getDouble("length").withDefault(3.0);
                    boolean filled = shapeLine.getBoolean("filled").withDefault(false);
                    boolean perimeter = shapeLine.getBoolean("perimeter").withDefault(false);

                    double half = length / 2;
                    int pointsPerAxis = Math.max(1, (int) Math.round(length / density) + 1);

                    List<ParticleEffect> cubePoints = new ArrayList<>();
                    for (int i = 0; i < pointsPerAxis; i++) {
                        double pointX = -half + i * density;
                        boolean xEdge = i == 0 || i == pointsPerAxis - 1;

                        for (int j = 0; j < pointsPerAxis; j++) {
                            double pointY = -half + j * density;
                            boolean yEdge = j == 0 || j == pointsPerAxis - 1;

                            for (int k = 0; k < pointsPerAxis; k++) {
                                double pointZ = -half + k * density;
                                boolean zEdge = k == 0 || k == pointsPerAxis - 1;

                                if (perimeter) {
                                    int edgeCount = (xEdge ? 1 : 0) + (yEdge ? 1 : 0) + (zEdge ? 1 : 0);
                                    if (edgeCount < 2) {
                                        continue;
                                    }
                                } else if (!filled && !xEdge && !yEdge && !zEdge) {
                                    continue;
                                }

                                addPoint(cubePoints, particleEffect, offsetX, offsetY, offsetZ, pointX, pointY, pointZ);
                            }
                        }
                    }

                    particleEffect = new MultiParticle(particleName, particleGroup, cubePoints);
                }
            }
        }
        else if (offsetX != Amount.ZERO || offsetY != Amount.ZERO || offsetZ != Amount.ZERO) {
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

    private void addPoint(List<ParticleEffect> points, ParticleEffect particleEffect,
                          Amount offsetX, Amount offsetY, Amount offsetZ,
                          double pointX, double pointY, double pointZ) {
        Amount pointOffsetX = offsetX.transform(v -> v + pointX);
        Amount pointOffsetY = offsetY.transform(v -> v + pointY);
        Amount pointOffsetZ = offsetZ.transform(v -> v + pointZ);

        points.add(new OffsetParticle(particleEffect, pointOffsetX, pointOffsetY, pointOffsetZ));
    }

    private void addSphereShell(List<ParticleEffect> points, ParticleEffect particleEffect,
                                Amount offsetX, Amount offsetY, Amount offsetZ,
                                double radius, double density) {
        if (radius <= 0) {
            addPoint(points, particleEffect, offsetX, offsetY, offsetZ, 0, 0, 0);
            return;
        }

        double surfaceArea = 4 * Math.PI * radius * radius;
        int pointCount = Math.max(2, (int) Math.round(surfaceArea / (density * density)));
        double goldenAngle = Math.PI * (3 - Math.sqrt(5));

        for (int i = 0; i < pointCount; i++) {
            double y = 1 - (i / (double) (pointCount - 1)) * 2;
            double radiusAtY = Math.sqrt(Math.max(0, 1 - y * y));
            double theta = goldenAngle * i;

            double pointX = Math.cos(theta) * radiusAtY * radius;
            double pointY = y * radius;
            double pointZ = Math.sin(theta) * radiusAtY * radius;

            addPoint(points, particleEffect, offsetX, offsetY, offsetZ, pointX, pointY, pointZ);
        }
    }

    private void addCircleRing(List<ParticleEffect> points, ParticleEffect particleEffect,
                               Amount offsetX, Amount offsetY, Amount offsetZ,
                               double radius, double density) {
        if (radius <= 0) {
            addPoint(points, particleEffect, offsetX, offsetY, offsetZ, 0, 0, 0);
            return;
        }

        double circumference = 2 * Math.PI * radius;
        int points_ = Math.max(1, (int) Math.round(circumference / density));

        for (int i = 0; i < points_; i++) {
            double theta = 2 * Math.PI * i / points_;
            double pointX = radius * Math.cos(theta);
            double pointZ = radius * Math.sin(theta);
            addPoint(points, particleEffect, offsetX, offsetY, offsetZ, pointX, 0, pointZ);
        }
    }

    @Override
    public @NotNull ParticleEffect loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        String particleName = sequence.getKey();
        String particleGroup = Group.byParticleFile(sequence.getRoot().getFile());
        return new MultiParticle(particleName, particleGroup, sequence.getAllRequired(ParticleEffect.class));
    }

}
