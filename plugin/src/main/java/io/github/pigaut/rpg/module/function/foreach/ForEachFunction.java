package io.github.pigaut.rpg.module.function.foreach;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ForEachFunction implements Function {

    private final ForEachSource<?> source;
    private final Function function;

    public ForEachFunction(@NotNull ForEachSource<?> source, @NotNull Function function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Collection<?> elements = source.getElements(context);

        List<Object> returnValues = new ArrayList<>();
        for (Object element : elements) {
            Context loopContext = context.withForEachElement(element);
            FunctionResponse response = function.dispatch(loopContext);

            if (response == FunctionResponse.CONTINUE) {
                continue;
            }

            if (response == FunctionResponse.RETURN) {
                return response;
            }

            if (response == FunctionResponse.STOP) {
                return response;
            }

            if (response instanceof YieldValueResponse yieldResponse) {
                returnValues.add(yieldResponse.getValue());
            }
        }

        if (!returnValues.isEmpty()) {
            return new YieldValueResponse(returnValues);
        }

        return FunctionResponse.NONE;
    }

}
