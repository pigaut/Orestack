package io.github.pigaut.rpg.module.skill.exp;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.chance.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ExpAmountLoader implements ConfigLoader<ExpAmount> {

    private final RpgMakerPlugin plugin;

    public ExpAmountLoader(RpgMakerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid exp amount";
    }

    @Override
    public @NotNull ExpAmount loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        ConfigLine line = scalar.toLine();

        String expActivity = line.getRequiredString(0);
        ExpAmount expAmount = plugin.getSettings().getExpEarningActivity(expActivity);
        if (expAmount != null) {
            return expAmount;
        }

        Amount amount = line.getRequired(0, Amount.class);
        line.getString(1)
                .require(s -> StringUtil.isAnyEqualIgnoreCase(s, "exp", "xp"),
                "Expected format: <amount> exp (chance=5%)")
                .throwErrorIfAny();
        Chance chance = line.get("chance", Chance.class).withDefault(null);

        return new ExpAmount(amount, chance);
    }
}
