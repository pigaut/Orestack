package io.github.pigaut.rpg.plugin.manager;

import com.google.common.collect.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ContainerBackedManager<T extends Identifiable> extends Manager implements Container<T>, Iterable<T> {

    private final Map<String, T> elementsByName = new HashMap<>();
    private final Multimap<String, T> elementsByGroup = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);

    public ContainerBackedManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean contains(@NotNull String name) {
        return elementsByName.containsKey(CaseFormatter.toSnakeCase(name));
    }

    @Override
    public boolean containsGroup(@NotNull String group) {
        return elementsByGroup.containsKey(group);
    }

    @Override
    public @Nullable T get(@NotNull String name) {
        return elementsByName.get(CaseFormatter.toSnakeCase(name));
    }

    @Override
    public @NotNull List<T> getAll() {
        return new ArrayList<>(elementsByName.values());
    }

    @Override
    public @NotNull List<T> getAll(@NotNull String group) {
        return new ArrayList<>(elementsByGroup.get(group));
    }

    @Override
    public void add(@NotNull T value) throws DuplicateElementException {
        String name = CaseFormatter.toSnakeCase(value.getName());
        if (elementsByName.containsKey(name)) {
            throw new DuplicateElementException(name);
        }
        elementsByName.put(name, value);
        String group = value.getGroup();
        if (group != null) {
            elementsByGroup.put(CaseFormatter.toSnakeCase(group), value);
        }
    }

    @Override
    public void remove(@NotNull String name) {
        T value = elementsByName.remove(CaseFormatter.toSnakeCase(name));
        if (value != null) {
            String group = value.getGroup();
            if (group != null) {
                elementsByGroup.remove(group, value);
            }
        }
    }

    @Override
    public void removeAll(@NotNull String group) {
        for (T value : elementsByGroup.removeAll(group)) {
            elementsByName.remove(value.getName());
        }
    }

    @Override
    public @NotNull List<String> getAllNames() {
        return elementsByName.keySet().stream().toList();
    }

    @Override
    public @NotNull List<String> getAllNames(String group) {
        return elementsByGroup.get(group).stream()
                .map(Identifiable::getName)
                .toList();
    }

    @Override
    public @NotNull List<String> getAllGroups() {
        return new ArrayList<>(elementsByGroup.keySet());
    }

    @Override
    public void clear() {
        elementsByName.clear();
        elementsByGroup.clear();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return elementsByName.values().iterator();
    }

}
