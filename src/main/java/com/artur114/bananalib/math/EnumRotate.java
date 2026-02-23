package com.artur114.bananalib.math;

import net.minecraft.util.Rotation;

public enum EnumRotate {
    NON, C90, C180, C270;

    public double radians() {
        return Math.toRadians(this.degrees());
    }

    public float degrees() {
        switch (this) {
            case C90:
                return 90.0F;
            case C180:
                return 180.0F;
            case C270:
                return 270.0F;
            default:
                return 0.0F;
        }
    }

    public Rotation toMc() {
        switch (this) {
            case C90:
                return Rotation.CLOCKWISE_90;
            case C180:
                return Rotation.CLOCKWISE_180;
            case C270:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }
}
