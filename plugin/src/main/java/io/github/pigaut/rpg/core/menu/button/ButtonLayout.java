package io.github.pigaut.rpg.core.menu.button;

public class ButtonLayout {

    public static void apply(Button[] buttons, Button template, int... layout) {
        for (int index : layout) {
            buttons[index] = template;
        }
    }

    public static final int[] SMALL_FRAME = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    public static final int[] MEDIUM_FRAME = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
    public static final int[] BIG_FRAME = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    public static final int[] LARGE_FRAME = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};

    public static final int[] SMALL_BOX = {10, 11, 12, 13, 14, 15, 16};
    public static final int[] MEDIUM_BOX = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24};
    public static final int[] BIG_BOX = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    public static final int[] LARGE_BOX = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

}
