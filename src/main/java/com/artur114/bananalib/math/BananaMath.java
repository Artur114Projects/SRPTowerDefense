package com.artur114.bananalib.math;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;

public class BananaMath {
    public static AxisAlignedBB createBoundingBox(int x, int y, int z, int x1, int y1, int z1) {
        return new AxisAlignedBB(x / 16.0F, y / 16.0F, z / 16.0F, x1 / 16.0F, y1 / 16.0F, z1 / 16.0F);
    }

    public static boolean arrayContainsAny(byte[] array, byte... params) {
        for (int i : array) {
            for (int j : params) {
                if (i == j) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean arrayContainsAny(int[] array, int... params) {
        for (int i : array) {
            for (int j : params) {
                if (i == j) {
                    return true;
                }
            }
        }
        return false;
    }

    @SafeVarargs
    public static <T> boolean arrayContainsAny(T[] array, T... params) {
        for (T i : array) {
            for (T j : params) {
                if (i.equals(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean arrayContains(byte[] array, byte param) {
        for (int i : array) {
            if (i == param) {
                return true;
            }
        }
        return false;
    }

    public static boolean arrayContains(int[] array, int param) {
        for (int i : array) {
            if (i == param) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean arrayContains(T[] array, T value) {
        for (T obj : array) {
            if (obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCollideManhattan(int pointX, int pointY, int point1X, int point1Y, int radius) {
        if (radius == 0) {
            return point1X == pointX && point1Y == pointY;
        }
        return Math.abs(point1X - pointX) <= radius && Math.abs(point1Y - pointY) <= radius;
    }

    public static float interpolate(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    public static double interpolate(double start, double end, float pct) {
        return start + (end - start) * pct;
    }

    public static long chunkPosAsLong(ChunkPos chunkPos) {
        return (long) chunkPos.x & 4294967295L | ((long) chunkPos.z & 4294967295L) << 32;
    }

    public static ChunkPos chunkPosFromLong(long data) {
        return new ChunkPos((int) (data), (int) (data >> 32));
    }

    public static int mod(double v) {
        if (v < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public static int mod(float v) {
        if (v < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public static int mod(int v) {
        if (v < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public static int round(double d) {
        int i = (int) d;
        return d - i > 0.5 ? i + 1 : i;
    }

    public static int round(float d) {
        int i = (int) d;
        return d - i > 0.5F ? i + 1 : i;
    }

    public static int floor(double d) {
        int i = (int) d;
        return d < (double) i ? i - 1 : i;
    }

    public static int floor(float f) {
        int i = (int) f;
        return f < (float) i ? i - 1 : i;
    }
}
