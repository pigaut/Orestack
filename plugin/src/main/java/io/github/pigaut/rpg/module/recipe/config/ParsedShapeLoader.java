package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ParsedShapeLoader implements ConfigLoader<ParsedShape> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid recipe shape";
    }

    @Override
    public @NotNull ParsedShape loadFromScalar(@NotNull ConfigScalar scalar) throws InvalidConfigException {
        String raw = scalar.toString();

        String[] lines = raw.strip().split("\\R");
        List<String> rows = new ArrayList<>();
        String result = null;
        int amount = 1;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String shapePart = line;

            int arrowIndex = line.indexOf("->");
            if (arrowIndex != -1) {
                shapePart = line.substring(0, arrowIndex).trim();
                String resultPart = line.substring(arrowIndex + 2).trim();

                String[] resultTokens = resultPart.split("\\s+", 2);
                if (resultTokens.length == 2 && resultTokens[0].matches("\\d+")) {
                    amount = Integer.parseInt(resultTokens[0]);
                    result = resultTokens[1].trim();
                } else {
                    result = resultPart;
                }
            }

            String[] symbols = shapePart.split("\\s+");
            if (symbols.length != 3) {
                throw new InvalidConfigException(scalar, "Expected a 3x3 grid recipe");
            }

            StringBuilder row = new StringBuilder(3);
            for (String symbol : symbols) {
                char c = symbol.charAt(0);
                row.append(c == '~' ? ' ' : c);
            }
            rows.add(row.toString());
        }

        if (rows.size() != 3) {
            throw new InvalidConfigException(scalar, "Expected a 3x3 grid recipe");
        }

        if (result == null || result.isEmpty()) {
            throw new InvalidConfigException(scalar, "Expected a recipe result");
        }

        return new ParsedShape(rows, result, amount);
    }

}
