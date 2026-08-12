package com.artur114.srptowerdefense.common.util;

public class CIMetricsUtils {//TODO: Переписать и локализовать
    public static String formatMeters2(float val) {
        String[] vals = new String[] {"%.1f §fм²", "%.1f §fкм²"};

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000000.0F >= 0.5) {
                j /= 1000000.0F;
            } else {
                return String.format(vals[i], j);
            }
        }

        return String.format(vals[1], j);
    }

    public static String formatJoules(float val) {
        String[] vals = new String[] {"%.1f §fДж/м²", "%.1f §fкДж/м²", "%.1f §fМДж/м²", "%.1f §fГДж/м²"};

        if (val < 0) {
            return "∞ §fДж/м²";
        }

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000.0F >= 0.5) {
                j /= 1000.0F;
            } else {
                return String.format(vals[i], j);
            }
        }

        return String.format(vals[3], j);
    }

    public static String formatWatts(float val) {
        String[] vals = new String[] {"%.1f §fВт/м²", "%.1f §fкВт/м²", "%.1f §fМВт/м²", "%.1f §fГВт/м²"};

        if (val < 0) {
            return "∞ §fВт/м²";
        }

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000.0F >= 0.5) {
                j /= 1000.0F;
            } else {
                return String.format(vals[i], j);
            }
        }

        return String.format(vals[3], j);
    }
}
