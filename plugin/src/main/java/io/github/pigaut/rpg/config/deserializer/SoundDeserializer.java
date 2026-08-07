package io.github.pigaut.rpg.config.deserializer;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.configurator.convert.deserialize.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class SoundDeserializer implements Deserializer<Sound> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid sound";
    }

    @Override
    public @NotNull Sound deserialize(@NotNull String soundName) throws StringParseException {
        Sound sound = SoundUtil.getSound(soundName);
        if (sound == null) {
            throw new StringParseException("Could not find sound with name: " + soundName);
        }
        return sound;
    }

}
