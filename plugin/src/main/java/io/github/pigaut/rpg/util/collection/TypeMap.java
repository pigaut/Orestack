package io.github.pigaut.rpg.util.collection;

import com.google.common.collect.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TypeMap<K, V> {

    private final ListMultimap<K, TypeElement> byKey = ArrayListMultimap.create();

    public int size() {
        return byKey.size();
    }

    public boolean isEmpty() {
        return byKey.isEmpty();
    }

    public boolean containsKey(K key) {
        return byKey.containsKey(key);
    }

    public boolean containsValue(V value) {
        return byKey.values().stream()
                .map(TypeElement::getValue)
                .anyMatch(val -> val.equals(value));
    }

    @Nullable
    public <T extends V> T get(K key, Class<T> type) {
        return byKey.get(key).stream()
                .filter(element -> element.isOfType(type))
                .map(element -> element.get(type))
                .findFirst()
                .orElse(null);
    }

    public boolean put(K key, Class<? extends V> type, V value) {
        return byKey.put(key, new TypeElement(type, value));
    }

    public boolean remove(K key, Class<? extends V> type) {
        Collection<TypeElement> elements = byKey.get(key);
        for (TypeElement element : elements) {
            if (element.isOfType(type)) {
                return elements.remove(element);
            }
        }
        return false;
    }

    public void clear() {
        byKey.clear();
    }


    private class TypeElement {

        private final Class<? extends V> type;
        private final V value;

        private TypeElement(Class<? extends V> type, V value) {
            this.type = type;
            this.value = value;
        }

        public Class<? extends V> getType() {
            return type;
        }

        public V getValue() {
            return value;
        }

        boolean isOfType(Class<? extends V> type) {
            return this.type.isAssignableFrom(type);
        }

        @Nullable
        <T extends V> T get(Class<T> type) {
            if (isOfType(type)) {
                return type.cast(value);
            }
            return null;
        }
    }
}
