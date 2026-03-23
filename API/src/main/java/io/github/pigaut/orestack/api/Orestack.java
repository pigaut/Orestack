package io.github.pigaut.orestack.api;

import org.jetbrains.annotations.*;

public class Orestack {

    private static OrestackAPI API;

    public static OrestackAPI getAPI() {
        if (API == null) {
            throw new IllegalStateException("Api has not been initialized yet.");
        }
        return API;
    }

    public static void setApiInstance(@NotNull OrestackAPI API) {
        if (Orestack.API != null) {
            throw new UnsupportedOperationException("You cannot initialize the api instance after it was initialized.");
        }
        Orestack.API = API;
    }

}
