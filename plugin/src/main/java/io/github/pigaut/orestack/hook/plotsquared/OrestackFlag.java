package io.github.pigaut.orestack.hook.plotsquared;

import com.plotsquared.core.configuration.caption.*;
import com.plotsquared.core.plot.flag.types.BooleanFlag;
import org.checkerframework.checker.nullness.qual.*;

public class OrestackFlag extends BooleanFlag<OrestackFlag> {
    public static final OrestackFlag GENERATORS_TRUE = new OrestackFlag(true);
    public static final OrestackFlag GENERATORS_FALSE = new OrestackFlag(false);

    private OrestackFlag(boolean value) {
        super(value, TranslatableCaption.of("flags.flag_description_orestack_resources"));
    }

    @Override
    protected OrestackFlag flagOf(@NonNull Boolean value) {
        return value ? GENERATORS_TRUE : GENERATORS_FALSE;
    }

}
