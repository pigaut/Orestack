package io.github.pigaut.rpg.module.mob.model;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class MobModelTemplateLoader implements ConfigLoader<MobModelTemplate> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid mob model";
    }

    @Override
    public @NotNull MobModelTemplate loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        return new MobModelTemplate(scalar.toString());
    }

}
