package io.github.pigaut.rpg.api;

import org.jetbrains.annotations.*;

public class RpgMaker {

    private static RpgMakerAPI API;

    public static RpgMakerAPI getAPI() {
        if (API == null) {
            throw new IllegalStateException("Api has not been initialized yet.");
        }
        return API;
    }

    public static void setApiInstance(@NotNull RpgMakerAPI API) {
        if (RpgMaker.API != null) {
            throw new UnsupportedOperationException("You cannot initialize the api instance after it was initialized.");
        }
        RpgMaker.API = API;
    }

}
