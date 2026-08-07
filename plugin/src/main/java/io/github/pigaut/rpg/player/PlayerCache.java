package io.github.pigaut.rpg.player;

import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerCache {

    private final Map<String, Object> cache = new HashMap<>();

    @Nullable
    public Object get(String id) {
        return cache.get(id);
    }

    @Nullable
    public <T> T get(String id, Class<T> cacheType) {
        Object object = cache.get(id);
        return object != null && object.getClass().isAssignableFrom(cacheType) ? (T) object : null;
    }

    public void cache(String id, Object object) {
        cache.put(id, object);
    }

    public void flush(String id) {
        cache.remove(id);
    }

}
