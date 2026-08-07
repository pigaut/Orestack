package io.github.pigaut.rpg.util;

import org.jetbrains.annotations.*;

public class ObjectUtil {

    public static boolean isAnyNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnyEqual(@NotNull Object object, @NotNull Object... others) {
        for (Object other : others) {
            if (object.equals(other)) {
                return true;
            }
        }
        return false;
    }

}
