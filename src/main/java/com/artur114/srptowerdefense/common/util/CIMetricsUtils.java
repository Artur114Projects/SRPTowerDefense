package com.artur114.srptowerdefense.common.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class CIMetricsUtils {
    public static String formatMeters2(float val) {
        String[] vals = I18n.format("srptowerdefense.ci_metrics.meters2").split("<n>");

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000000.0F >= 0.5) {
                j /= 1000000.0F;
            } else {
                return String.format("%.1f ", j) + TextFormatting.WHITE + vals[i];
            }
        }

        return String.format("%.1f ", j) + TextFormatting.WHITE + vals[1];
    }

    public static String formatJoules(float val) {
        String[] vals = I18n.format("srptowerdefense.ci_metrics.joules/meter2").split("<n>");

        if (val < 0) {
            return "∞ §f" + vals[0];
        }

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000.0F >= 0.5) {
                j /= 1000.0F;
            } else {
                return String.format("%.1f ", j) + TextFormatting.WHITE + vals[i];
            }
        }

        return String.format("%.1f ", j) + TextFormatting.WHITE + vals[3];
    }

    public static String formatWatts(float val) {
        String[] vals = I18n.format("srptowerdefense.ci_metrics.watts/meter2").split("<n>");

        if (val < 0) {
            return "∞ §f" + vals[0];
        }

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000.0F >= 0.5) {
                j /= 1000.0F;
            } else {
                return String.format("%.1f ", j) + TextFormatting.WHITE + vals[i];
            }
        }

        return String.format("%.1f ", j) + TextFormatting.WHITE + vals[3];
    }
}
