package io.github.pigaut.rpg.util;

import org.jetbrains.annotations.*;

import java.util.*;

public class StringUtil {

    public static @NotNull String randomName() {
        return UUID.randomUUID().toString();
    }

    public static boolean isAnyEqual(@NotNull String str, @NotNull String... others) {
        for (String other : others) {
            if (str.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnyEqualIgnoreCase(@NotNull String str, @NotNull String... others) {
        for (String other : others) {
            if (str.equalsIgnoreCase(other)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isParenthesized(String str, String parenthesis) {
        return isParenthesized(str, parenthesis, parenthesis);
    }

    public static boolean isParenthesized(String str, String prefix, String suffix) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.startsWith(prefix) && str.endsWith(suffix);
    }

    public static String addParentheses(String str, char parenthesis) {
        return addParentheses(str, parenthesis, parenthesis);
    }

    public static String addParentheses(String str, char prefix, char suffix) {
        return prefix + str + suffix;
    }

    public static String removeParentheses(String str) {
        return str.substring(1, str.length() - 1);
    }

    public static String removeTag(String str, String tag) {
        return str.replace(tag, "").trim();
    }

    public static String buildString(Object... elements) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(elements)
                .filter(Objects::nonNull)
                .forEach(builder::append);
        return builder.toString();
    }

    public static List<String> splitByLength(String string, int length) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < string.length(); i += length) {
            result.add(string.substring(i, Math.min(i + length, string.length())));
        }
        return result;
    }

    public static String prefixDash(String str) {
        return "- " + str;
    }

    public static @NotNull String insertInto(@NotNull String text, @NotNull String insert, int width, @NotNull BarAlignment align) {
        String block = insert.length() <= width ? pad(insert, width, align) : insert;

        return switch (align) {
            case LEFT -> insertLeft(text, block);
            case RIGHT -> insertRight(text, block);
            case CENTER -> insertCenter(text, block);
        };
    }

    private static @NotNull String insertLeft(@NotNull String text, @NotNull String block) {
        int neededLength = block.length();
        int consumed = Math.min(neededLength, text.length());

        String remainder = text.substring(consumed);
        String separator = remainder.isEmpty() ? "" : " ";

        return block + separator + remainder;
    }

    private static @NotNull String insertRight(@NotNull String text, @NotNull String block) {
        int neededLength = block.length();
        int textLength = text.length();
        int consumed = Math.min(neededLength, textLength);

        String remainder = text.substring(0, textLength - consumed);
        String separator = remainder.isEmpty() ? "" : " ";

        return remainder + separator + block;
    }

    private static @NotNull String insertCenter(@NotNull String text, @NotNull String block) {
        int neededLength = block.length();
        int textLength = text.length();
        int anchor = textLength / 2;

        int leftNeeded = neededLength / 2;
        int rightNeeded = neededLength - leftNeeded;

        int leftConsumed = Math.min(leftNeeded, anchor);
        int rightConsumed = Math.min(rightNeeded, textLength - anchor);

        String leftRemainder = text.substring(0, anchor - leftConsumed);
        String rightRemainder = text.substring(anchor + rightConsumed);

        String leftSeparator = leftRemainder.isEmpty() ? "" : " ";
        String rightSeparator = rightRemainder.isEmpty() ? "" : " ";

        return leftRemainder + leftSeparator + block + rightSeparator + rightRemainder;
    }

    private static @NotNull String pad(@NotNull String text, int width, @NotNull BarAlignment align) {
        int totalPadding = width - text.length();

        return switch (align) {
            case LEFT -> text + " ".repeat(totalPadding);
            case RIGHT -> " ".repeat(totalPadding) + text;
            case CENTER -> {
                int leftPadding = totalPadding / 2;
                int rightPadding = totalPadding - leftPadding;
                yield " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
            }
        };
    }

}
