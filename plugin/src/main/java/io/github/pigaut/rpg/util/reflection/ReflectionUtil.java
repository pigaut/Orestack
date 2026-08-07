package io.github.pigaut.rpg.util.reflection;

import org.jetbrains.annotations.*;

import java.lang.reflect.*;

public class ReflectionUtil {

    @NotNull
    public static Class<?> getClass(@NotNull String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not find class: " + className, e);
        }
    }

    public static <T> boolean findConstructor(@NotNull Class<T> clazz, @NotNull Class<?>... parameterTypes) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @NotNull
    public static <T> Constructor<T> getConstructor(@NotNull Class<T> clazz, @NotNull Class<?>... parameterTypes) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Could not find constructor in class: " + clazz.getName(), e);
        }
    }

    @NotNull
    public static <T> T newInstance(@NotNull Class<T> clazz, @NotNull Object... args) {
        try {
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }
            Constructor<T> constructor = getConstructor(clazz, paramTypes);
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not create instance of class: " + clazz.getName(), e);
        }
    }

    @NotNull
    public static Method getMethod(@NotNull Class<?> clazz, @NotNull String name, @NotNull Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Could not find method " + name + " in class: " + clazz.getName(), e);
        }
    }

    @NotNull
    public static Object invoke(@NotNull Method method, @NotNull Object instance, @NotNull Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not invoke method: " + method.getName(), e);
        }
    }

    @NotNull
    public static Field getField(@NotNull Class<?> clazz, @NotNull String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Could not find field " + name + " in class: " + clazz.getName(), e);
        }
    }

    @NotNull
    public static Object getStaticFieldValue(@NotNull Field field) {
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not access field: " + field.getName(), e);
        }
    }

    @NotNull
    public static Object getFieldValue(@NotNull Field field, @NotNull Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not access field: " + field.getName(), e);
        }
    }

    public static void setFieldValue(@NotNull Field field, @NotNull Object instance, @NotNull Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not set field: " + field.getName(), e);
        }
    }

    @Nullable
    public static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException | SecurityException e) {
            return null;
        }
    }

    @Nullable
    public static Object accessField(Field field, Object instance) {
        if (field == null) return null;
        field.setAccessible(true);
        try {
            return field.get(instance);
        } catch (IllegalAccessException | NullPointerException | IllegalArgumentException e) {
            return null;
        }
    }

    @Nullable
    public static <T> T accessField(Field field, Class<T> type, Object instance) {
        if (field == null || type == null) return null;
        field.setAccessible(true);
        try {
            return type.cast(field.get(instance));
        } catch (IllegalAccessException | NullPointerException | IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public static Method getMethod(Class<?> clazz, String name) {
        try {
            return clazz.getMethod(name);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Nullable
    public static Parameter getMethodParameter(Method method, String parameterName) {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.getName().equalsIgnoreCase(parameterName))
                return parameter;
        }
        return null;
    }

    @Nullable
    public static Integer getMethodParameterIndex(Method method, String parameterName) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equalsIgnoreCase(parameterName))
                return i;
        }
        return null;
    }

    @Nullable
    public static Class<?> getInnerClass(Class<?> clazz, String name) {
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            if (innerClass.getSimpleName().equals(name)) {
                return innerClass;
            }
        }
        return null;
    }

}
