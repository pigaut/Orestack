package io.github.pigaut.rpg.config.deserializer;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.configurator.convert.deserialize.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ParticleDeserializer implements Deserializer<Particle> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid particle";
    }

    @Override
    public @NotNull Particle deserialize(@NotNull String particleName) throws StringParseException {
        Particle particle = ParticleUtil.getParticle(particleName);
        if (particle == null) {
            throw new StringParseException("Could not find particle with name: " + particleName);
        }
        return particle;
    }

}
