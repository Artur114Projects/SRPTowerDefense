package com.artur114.bananalib.mc.math;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BoxMc3D extends AxisAlignedBB {
    public BoxMc3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
    }

    public BoxMc3D(BlockPos pos) {
        super(pos);
    }

    public BoxMc3D(BlockPos pos1, BlockPos pos2) {
        super(pos1, pos2);
    }

    public BoxMc3D(Vec3d min, Vec3d max) {
        super(min, max);
    }
}
