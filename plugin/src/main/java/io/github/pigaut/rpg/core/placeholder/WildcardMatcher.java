package io.github.pigaut.rpg.core.placeholder;

import org.jetbrains.annotations.*;

import java.util.*;

public final class WildcardMatcher {

    private WildcardMatcher() {}

    public static String[] splitPattern(@NotNull String pattern) {
        List<String> parts = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '*') {
                parts.add(pattern.substring(start, i));
                start = i + 1;
            }
        }
        parts.add(pattern.substring(start));
        return parts.toArray(new String[0]);
    }

    public static String @Nullable [] tryMatch(String @NotNull [] parts, @NotNull String input) {
        if (parts.length == 1) {
            return input.equals(parts[0]) ? new String[0] : null;
        }

        String prefix = parts[0];
        String suffix = parts[parts.length - 1];

        if (!input.startsWith(prefix) || !input.endsWith(suffix)) {
            return null;
        }
        if (input.length() < prefix.length() + suffix.length()) {
            return null;
        }

        String[] groups = new String[parts.length - 1];
        int pos = prefix.length();

        for (int i = 1; i < parts.length - 1; i++) {
            String literal = parts[i];
            int next = literal.isEmpty() ? pos : input.indexOf(literal, pos);
            if (next == -1) {
                return null;
            }
            groups[i - 1] = input.substring(pos, next);
            pos = next + literal.length();
        }

        int suffixStart = input.length() - suffix.length();
        if (suffixStart < pos) {
            return null;
        }
        groups[groups.length - 1] = input.substring(pos, suffixStart);

        return groups;
    }

}