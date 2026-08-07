package io.github.pigaut.rpg.util;

public class Percentage {

    public static int asInteger(double progress, double total) {
        if (total <= 0) {
            return 0;
        }
        double ratio = progress / total;
        int percent = (int) Math.round(ratio * 100.0);
        return Math.max(0, Math.min(100, percent));
    }

    public static double asDouble(double progress, double total, int decimals) {
        if (total <= 0) {
            return 0.0;
        }
        double ratio = progress / total;
        double percent = ratio * 100.0;
        double scale = Math.pow(10, decimals);
        percent = Math.round(percent * scale) / scale;
        return Math.max(0.0, Math.min(100.0, percent));
    }

    public static double asDouble(double progress, double total) {
        return asDouble(progress, total, Integer.MAX_VALUE);
    }

    public static String asString(double progress, double total, int precision) {
        String formatRule = "%." + precision + "f";
        if (total <= 0) {
            return String.format(formatRule, 0.0);
        }
        double safeProgress = Math.max(0.0, progress);
        double percentage = (safeProgress / total) * 100.0;
        double cappedPercentage = Math.min(100.0, percentage);
        return String.format(formatRule, cappedPercentage);
    }

    public static String asString(double progress, double total) {
        return asString(progress, total, Integer.MAX_VALUE);
    }

}
