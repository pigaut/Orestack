package io.github.pigaut.rpg.module.mob.disguise;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import me.libraryaddict.disguise.disguisetypes.*;
import me.libraryaddict.disguise.utilities.parser.*;
import org.jetbrains.annotations.*;

public class MobDisguiseTemplateLoader implements ConfigLoader<MobDisguiseTemplate> {

    private final EnhancedPlugin plugin;

    public MobDisguiseTemplateLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid mob disguise";
    }

    @Override
    public @NotNull MobDisguiseTemplate loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        DisguiseType disguiseType = scalar.getRequired(DisguiseType.class);
        Disguise disguise = new MobDisguise(disguiseType);
        return new SimpleMobDisguiseTemplate(disguise);
    }

    @Override
    public @NotNull MobDisguiseTemplate loadFromSection(ConfigSection section) throws InvalidConfigException {
        DisguiseType disguiseType = section.getRequired("type", DisguiseType.class);
        MobDisguise disguise = new MobDisguise(disguiseType);

        StringBuilder optionsBuilder = new StringBuilder(disguiseType.name());
        for (ConfigScalar scalar : section.getNestedScalars()) {
            String key = scalar.getKey();
            String value = scalar.toString();

            if (scalar.getKey().equalsIgnoreCase("type")) {
                continue;
            }

            optionsBuilder.append(" ").append(CaseFormatter.toCamelCase(key));
            optionsBuilder.append(" ").append(value);
        }

        plugin.getScheduler().runTask(() -> {
            try {
                Disguise parsed = DisguiseParser.parseDisguise(optionsBuilder.toString());
                if (parsed != null) {
                    disguise.setWatcher(parsed.getWatcher());
                }
            } catch (Throwable ignored) {
            }
        });

        return new SimpleMobDisguiseTemplate(disguise);
    }

}
