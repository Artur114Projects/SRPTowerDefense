package com.artur114.bananalib.math.m3d.area;

import com.artur114.bananalib.math.EnumRotate;
import com.artur114.bananalib.math.m3d.vec.Pos3d;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BoundingBox3d implements IArea3d {
    private final BlockPos start;
    private final BlockPos end;
    private final BlockPos size;

    public BoundingBox3d(BlockPos start, BlockPos end) {
        this.end = new BlockPos(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));
        this.start = new BlockPos(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
        this.size = this.end.add(-this.start.getX(), -this.start.getY(), -this.start.getZ()).add(1, 1, 1);
    }

    public BoundingBox3d(Vec3d start, Vec3d end) {
        this(new BlockPos(start.x, start.y, start.z), new BlockPos(end.x, end.y, end.z));
    }

    public BoundingBox3d offset(int x, int y, int z) {
        return new BoundingBox3d(this.start.add(x, y, z), this.end.add(x, y, z));
    }

    public BoundingBox3d offset(Vec3i off) {
        return new BoundingBox3d(this.start.add(off.getX(), off.getY(), off.getZ()), this.end.add(off.getX(), off.getY(), off.getZ()));
    }

    public BoundingBox3d grow(int size) {
        return this.grow(size, size, size);
    }

    public BoundingBox3d grow(int x, int y, int z) {
        return new BoundingBox3d(this.start.add(-x, -y, -z), this.end.add(x, y, z));
    }

    public BoundingBox3d rotate(Vec3d center, EnumRotate rotate) {
        Pos3d vecStart = new Pos3d(this.start.getX(), this.start.getY(), this.start.getZ()).deduct(center).rotateY(rotate.toMc()).add(center);
        Pos3d vecEnd = new Pos3d(this.end.getX(), this.end.getY(), this.end.getZ()).deduct(center).rotateY(rotate.toMc()).add(center);
        return new BoundingBox3d(vecStart, vecEnd);
    }

    @Override
    public int areaSize() {
        return this.size.getX() * this.size.getY() * this.size.getZ();
    }

    @Override
    public boolean isCollide(double x, double y, double z) {
        return x >= this.start.getX() && y >= this.start.getY() && z >= this.start.getZ() && x <= this.end.getX() + 1 && y <= this.end.getY() + 1 && z <= this.end.getZ() + 1;
    }

    @Override
    public @Nullable BlockPos fromIndex(int index) {
        return this.start.add((index % size.getX()), ((index / size.getX()) % size.getY()), (((index / size.getX()) / size.getY()) % size.getZ()));
    }

    @Override
    public List<BlockPos> points() {
        List<BlockPos> ret = new ArrayList<>();
        for (BlockPos pos : BlockPos.getAllInBox(this.start, this.end)) ret.add(pos);
        return ret;
    }

    @Override
    public void renderArea(float alpha) {
        double x = Particle.interpPosX;
        double y = Particle.interpPosY;
        double z = Particle.interpPosZ;
        double d = 0.001;

        RenderGlobal.drawBoundingBox(this.start.getX() - x + d, this.start.getY() - y + d, this.start.getZ() - z + d, this.end.getX() + 1 - x - d, this.end.getY() + 1 - y - d, this.end.getZ() + 1 - z - d, 1.0F, 1.0F, 1.0F, alpha);
    }
}