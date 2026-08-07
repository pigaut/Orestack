package io.github.pigaut.rpg.core.menu;

public class MenuSize {

    public static final int TINY = 9;
    public static final int VERY_SMALL = 18;
    public static final int SMALL = 27;
    public static final int MEDIUM = 36;
    public static final int BIG = 45;
    public static final int LARGE = 54;

    public static boolean isValid(int size) {
        return size >= TINY && size <= LARGE && size % 9 == 0;
    }

}
