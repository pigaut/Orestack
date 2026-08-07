package io.github.pigaut.rpg.server.version;

import java.util.*;

public final class Version {

    public static final int V1_8 = 0;
    public static final int V1_8_3 = 1;
    public static final int V1_8_4 = 2;
    public static final int V1_8_5 = 3;
    public static final int V1_8_6 = 4;
    public static final int V1_8_7 = 5;
    public static final int V1_8_8 = 6;
    public static final int V1_9 = 7;
    public static final int V1_9_2 = 8;
    public static final int V1_9_4 = 9;
    public static final int V1_10 = 10;
    public static final int V1_10_2 = 11;
    public static final int V1_11 = 12;
    public static final int V1_11_1 = 13;
    public static final int V1_11_2 = 14;
    public static final int V1_12 = 15;
    public static final int V1_12_1 = 16;
    public static final int V1_12_2 = 17;
    public static final int V1_13 = 18;
    public static final int V1_13_1 = 19;
    public static final int V1_13_2 = 20;
    public static final int V1_14 = 21;
    public static final int V1_14_1 = 22;
    public static final int V1_14_2 = 23;
    public static final int V1_14_3 = 24;
    public static final int V1_14_4 = 25;
    public static final int V1_15 = 26;
    public static final int V1_15_1 = 27;
    public static final int V1_15_2 = 28;
    public static final int V1_16 = 29;
    public static final int V1_16_1 = 30;
    public static final int V1_16_2 = 31;
    public static final int V1_16_3 = 32;
    public static final int V1_16_4 = 33;
    public static final int V1_16_5 = 34;
    public static final int V1_17 = 35;
    public static final int V1_17_1 = 36;
    public static final int V1_18 = 37;
    public static final int V1_18_1 = 38;
    public static final int V1_18_2 = 39;
    public static final int V1_19 = 40;
    public static final int V1_19_1 = 41;
    public static final int V1_19_2 = 42;
    public static final int V1_19_3 = 43;
    public static final int V1_19_4 = 44;
    public static final int V1_20 = 45;
    public static final int V1_20_1 = 46;
    public static final int V1_20_2 = 47;
    public static final int V1_20_3 = 48;
    public static final int V1_20_4 = 49;
    public static final int V1_20_5 = 50;
    public static final int V1_20_6 = 51;
    public static final int V1_21 = 52;
    public static final int V1_21_1 = 53;
    public static final int V1_21_2 = 54;
    public static final int V1_21_3 = 55;
    public static final int V1_21_4 = 56;
    public static final int V1_21_5 = 57;
    public static final int V1_21_6 = 58;
    public static final int V1_21_7 = 59;
    public static final int V1_21_8 = 60;
    public static final int V1_21_9 = 61;
    public static final int V1_21_10 = 62;
    public static final int V1_21_11 = 63;
    public static final int V26_1 = 64;
    public static final int V26_1_1 = 65;
    public static final int V26_1_2 = 66;
    public static final int V26_2 = 67;

    public static final int UNKNOWN = 100;

    private static final Map<Integer, Integer> VERSION_NMS = new LinkedHashMap<>();

    static {
        VERSION_NMS.put(Version.V1_8, NMSVersion.V1_8_R1);
        VERSION_NMS.put(Version.V1_8_3, NMSVersion.V1_8_R2);
        VERSION_NMS.put(Version.V1_8_4, NMSVersion.V1_8_R3);
        VERSION_NMS.put(Version.V1_8_5, NMSVersion.V1_8_R3);
        VERSION_NMS.put(Version.V1_8_6, NMSVersion.V1_8_R3);
        VERSION_NMS.put(Version.V1_8_7, NMSVersion.V1_8_R3);
        VERSION_NMS.put(Version.V1_8_8, NMSVersion.V1_8_R3);

        VERSION_NMS.put(Version.V1_9, NMSVersion.V1_9_R1);
        VERSION_NMS.put(Version.V1_9_2, NMSVersion.V1_9_R1);
        VERSION_NMS.put(Version.V1_9_4, NMSVersion.V1_9_R2);

        VERSION_NMS.put(Version.V1_10, NMSVersion.V1_10_R1);
        VERSION_NMS.put(Version.V1_10_2, NMSVersion.V1_10_R1);

        VERSION_NMS.put(Version.V1_11, NMSVersion.V1_11_R1);
        VERSION_NMS.put(Version.V1_11_1, NMSVersion.V1_11_R1);
        VERSION_NMS.put(Version.V1_11_2, NMSVersion.V1_11_R1);

        VERSION_NMS.put(Version.V1_12, NMSVersion.V1_12_R1);
        VERSION_NMS.put(Version.V1_12_1, NMSVersion.V1_12_R1);
        VERSION_NMS.put(Version.V1_12_2, NMSVersion.V1_12_R1);

        VERSION_NMS.put(Version.V1_13, NMSVersion.V1_13_R1);
        VERSION_NMS.put(Version.V1_13_1, NMSVersion.V1_13_R2);
        VERSION_NMS.put(Version.V1_13_2, NMSVersion.V1_13_R2);

        VERSION_NMS.put(Version.V1_14, NMSVersion.V1_14_R1);
        VERSION_NMS.put(Version.V1_14_1, NMSVersion.V1_14_R1);
        VERSION_NMS.put(Version.V1_14_2, NMSVersion.V1_14_R1);
        VERSION_NMS.put(Version.V1_14_3, NMSVersion.V1_14_R1);
        VERSION_NMS.put(Version.V1_14_4, NMSVersion.V1_14_R1);

        VERSION_NMS.put(Version.V1_15, NMSVersion.V1_15_R1);
        VERSION_NMS.put(Version.V1_15_1, NMSVersion.V1_15_R1);
        VERSION_NMS.put(Version.V1_15_2, NMSVersion.V1_15_R1);

        VERSION_NMS.put(Version.V1_16, NMSVersion.V1_16_R1);
        VERSION_NMS.put(Version.V1_16_1, NMSVersion.V1_16_R1);
        VERSION_NMS.put(Version.V1_16_2, NMSVersion.V1_16_R2);
        VERSION_NMS.put(Version.V1_16_3, NMSVersion.V1_16_R2);
        VERSION_NMS.put(Version.V1_16_4, NMSVersion.V1_16_R3);
        VERSION_NMS.put(Version.V1_16_5, NMSVersion.V1_16_R3);

        VERSION_NMS.put(Version.V1_17, NMSVersion.V1_17_R1);
        VERSION_NMS.put(Version.V1_17_1, NMSVersion.V1_17_R1);

        VERSION_NMS.put(Version.V1_18, NMSVersion.V1_18_R1);
        VERSION_NMS.put(Version.V1_18_1, NMSVersion.V1_18_R1);
        VERSION_NMS.put(Version.V1_18_2, NMSVersion.V1_18_R2);

        VERSION_NMS.put(Version.V1_19, NMSVersion.V1_19_R1);
        VERSION_NMS.put(Version.V1_19_1, NMSVersion.V1_19_R1);
        VERSION_NMS.put(Version.V1_19_2, NMSVersion.V1_19_R1);
        VERSION_NMS.put(Version.V1_19_3, NMSVersion.V1_19_R2);
        VERSION_NMS.put(Version.V1_19_4, NMSVersion.V1_19_R3);

        VERSION_NMS.put(Version.V1_20, NMSVersion.V1_20_R1);
        VERSION_NMS.put(Version.V1_20_1, NMSVersion.V1_20_R1);
        VERSION_NMS.put(Version.V1_20_2, NMSVersion.V1_20_R2);
        VERSION_NMS.put(Version.V1_20_3, NMSVersion.V1_20_R3);
        VERSION_NMS.put(Version.V1_20_4, NMSVersion.V1_20_R3);
        VERSION_NMS.put(Version.V1_20_5, NMSVersion.V1_20_R3);
        VERSION_NMS.put(Version.V1_20_6, NMSVersion.V1_20_R3);

        VERSION_NMS.put(Version.V1_21, NMSVersion.V1_21_R1);
        VERSION_NMS.put(Version.V1_21_1, NMSVersion.V1_21_R1);
        VERSION_NMS.put(Version.V1_21_2, NMSVersion.V1_21_R2);
        VERSION_NMS.put(Version.V1_21_3, NMSVersion.V1_21_R2);
        VERSION_NMS.put(Version.V1_21_4, NMSVersion.V1_21_R3);
        VERSION_NMS.put(Version.V1_21_5, NMSVersion.V1_21_R4);
        VERSION_NMS.put(Version.V1_21_6, NMSVersion.V1_21_R5);
        VERSION_NMS.put(Version.V1_21_7, NMSVersion.V1_21_R5);
        VERSION_NMS.put(Version.V1_21_8, NMSVersion.V1_21_R5);
        VERSION_NMS.put(Version.V1_21_9, NMSVersion.V1_21_R5);
        VERSION_NMS.put(Version.V1_21_10, NMSVersion.V1_21_R5);
        VERSION_NMS.put(Version.V1_21_11, NMSVersion.V1_21_R5);
        VERSION_NMS.put(Version.V26_1, NMSVersion.V26_1_R1);
        VERSION_NMS.put(Version.V26_1_1, NMSVersion.V26_1_R1);
        VERSION_NMS.put(Version.V26_1_2, NMSVersion.V26_1_R1);

        VERSION_NMS.put(Version.UNKNOWN, NMSVersion.UNKNOWN);
    }

    public static List<Integer> getAllVersions() {
        return new ArrayList<>(VERSION_NMS.keySet());
    }

    public static List<Integer> getAllNMSVersions() {
        return new ArrayList<>(VERSION_NMS.values());
    }

    public static Integer getNMSVersion(int version) {
        return VERSION_NMS.getOrDefault(version, NMSVersion.UNKNOWN);
    }

    public static List<Integer> getVersionsNewerOrEqualTo(int thresholdVersion) {
        List<Integer> versions = new ArrayList<>();
        for (Integer version : getAllVersions()) {
            if (version >= thresholdVersion) {
                versions.add(version);
            }
        }
        return versions;
    }

    public static List<Integer> getVersionsOlderThan(int thresholdVersion) {
        List<Integer> versions = new ArrayList<>();
        for (Integer version : getAllVersions()) {
            if (version < thresholdVersion) {
                versions.add(version);
            }
        }
        return versions;
    }

}
