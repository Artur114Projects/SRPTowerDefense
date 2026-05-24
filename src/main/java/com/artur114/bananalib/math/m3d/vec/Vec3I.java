package com.artur114.bananalib.math.m3d.vec;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.IBox2D;
import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;
import com.artur114.bananalib.math.m2d.vec.Vec2D;
import com.artur114.bananalib.math.m2d.vec.Vec2I;
import com.artur114.bananalib.math.m3d.box.IBox3D;
import com.artur114.bananalib.math.m3d.box.IBox3I;

public class Vec3I implements IVec3I {
    public static final Vec3D ZERO = new Vec3D(0, 0, 0);
    private final int x, y, z;

    public Vec3I(double x, double y, double z) {
        this.x = BananaMath.floor(x);
        this.y = BananaMath.floor(y);
        this.z = BananaMath.floor(z);
    }

    public Vec3I(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3I(IVec3D vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public Vec3I(IVec3I vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public Vec3I(IVec2D vec2D, double z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3I(IVec2I vec2D, int z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3I(IVec2I vec2D, double z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3I(IVec2D vec2D, int z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3I(IVec2D vec2D) {
        this(vec2D.x(), vec2D.y(), 0);
    }

    public Vec3I(IVec2I vec2D) {
        this(vec2D.x(), vec2D.y(), 0);
    }

    @Override
    public int x() {
        return this.x;
    }

    @Override
    public int y() {
        return this.y;
    }

    @Override
    public int z() {
        return this.z;
    }

    @Override
    public IVec2I xy() {
        return new Vec2I(this.x, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I xz() {
        return new Vec2I(this.z, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I yx() {
        return new Vec2I(this.y, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I yz() {
        return new Vec2I(this.y, this.z);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I zx() {
        return new Vec2I(this.z, this.x);
    }

    @Override
    public IVec2I zy() {
        return new Vec2I(this.z, this.y);
    }

    @Override
    public IVec3I zyx() {
        return new Vec3I(this.z, this.y, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3I zxy() {
        return new Vec3I(this.z, this.x, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3I yzx() {
        return new Vec3I(this.y, this.z, this.x);
    }

    @Override
    public IVec3I xzy() {
        return new Vec3I(this.x, this.z, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3I yxz() {
        return new Vec3I(this.y, this.x, this.z);
    }

    @Override
    public IVec2D xyD() {
        return new Vec2D(this.x, this.y);
    }

    @Override
    public IVec2D xzD() {
        return new Vec2D(this.x, this.z);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2D yxD() {
        return new Vec2D(this.y, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2D yzD() {
        return new Vec2D(this.y, this.z);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2D zxD() {
        return new Vec2D(this.z, this.x);
    }

    @Override
    public IVec2D zyD() {
        return new Vec2D(this.z, this.y);
    }

    @Override
    public IVec3D zyxD() {
        return new Vec3D(this.z, this.y, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3D zxyD() {
        return new Vec3D(this.z, this.x, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3D yzxD() {
        return new Vec3D(this.y, this.z, this.x);
    }

    @Override
    public IVec3D xzyD() {
        return new Vec3D(this.x, this.z, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3D yxzD() {
        return new Vec3D(this.y, this.x, this.z);
    }

    @Override
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Override
    public float lengthSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Override
    public int dot(IVec3I vec) {
        return this.x * vec.x() + this.y * vec.y() + this.z * vec.z();
    }

    @Override
    public double dot(IVec3D vec) {
        return this.x * vec.x() + this.y * vec.y() + this.z * vec.z();
    }

    @Override
    public IVec3I cross(IVec3I vec) {
        return new Vec3I(BananaMath.round((long) this.y * vec.z() - (long) this.z * vec.y()), BananaMath.round((long) this.z * vec.x() - (long) this.x * vec.z()), BananaMath.round((long) this.x * vec.y() - (long) this.y * vec.x()));
    }

    @Override
    public IVec3D cross(IVec3D vec) {
        return new Vec3D((long) this.y * vec.z() - (long) this.z * vec.y(), (long) this.z * vec.x() - (long) this.x * vec.z(), (long) this.x * vec.y() - (long) this.y * vec.x());
    }

    @Override
    public float distance(int x, int y, int z) {
        long deltaX = x - this.x, deltaY = y - this.y, deltaZ = z - this.z;
        return (float) Math.sqrt(deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ);
    }

    @Override
    public float distance(double x, double y, double z) {
        double deltaX = x - this.x, deltaY = y - this.y, deltaZ = z - this.z;
        return (float) Math.sqrt(deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ);
    }

    @Override
    public float distance(IVec3I vec) {
        return this.distance(vec.x(), vec.y(), vec.z());
    }

    @Override
    public float distance(IVec3D vec) {
        return this.distance(vec.x(), vec.y(), vec.z());
    }

    @Override
    public float distanceSq(int x, int y, int z) {
        long deltaX = x - this.x, deltaY = y - this.y, deltaZ = z - this.z;
        return deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ;
    }

    @Override
    public float distanceSq(double x, double y, double z) {
        double deltaX = x - this.x, deltaY = y - this.y, deltaZ = z - this.z;
        return (float) (deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ);
    }

    @Override
    public float distanceSq(IVec3I vec) {
        return this.distanceSq(vec.x(), vec.y(), vec.z());
    }

    @Override
    public float distanceSq(IVec3D vec) {
        return this.distanceSq(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I add(int x, int y, int z) {
        return new Vec3I(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public IVec3I add(double x, double y, double z) {
        return new Vec3I(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public IVec3I add(IVec3I vec) {
        return this.add(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I add(IVec3D vec) {
        return this.add(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I add(IVec2I vec, int z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I add(IVec2I vec, double z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I add(IVec2D vec, int z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I add(IVec2D vec, double z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I add(IVec2I vec) {
        return this.add(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3I add(IVec2D vec) {
        return this.add(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3I subtract(int x, int y, int z) {
        return new Vec3I(this.x - x, this.y - y, this.z - z);
    }

    @Override
    public IVec3I subtract(double x, double y, double z) {
        return new Vec3I(this.x - x, this.y - y, this.z - z);
    }

    @Override
    public IVec3I subtract(IVec3I vec) {
        return this.subtract(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I subtract(IVec3D vec) {
        return this.subtract(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I subtract(IVec2I vec, int z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I subtract(IVec2I vec, double z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I subtract(IVec2D vec, int z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I subtract(IVec2D vec, double z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I subtract(IVec2I vec) {
        return this.subtract(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3I subtract(IVec2D vec) {
        return this.subtract(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3I scale(int val) {
        return new Vec3I(this.x * val, this.y * val, this.z * val);
    }

    @Override
    public IVec3I scale(double val) {
        return new Vec3I(this.x * val, this.y * val, this.z * val);
    }

    @Override
    public IVec3I scale(int x, int y, int z) {
        return new Vec3I(this.x * x, this.y * y, this.z * z);
    }

    @Override
    public IVec3I scale(double x, double y, double z) {
        return new Vec3I(this.x * x, this.y * y, this.z * z);
    }

    @Override
    public IVec3I scale(IVec3I vec) {
        return this.scale(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I scale(IVec3D vec) {
        return this.scale(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I scale(IVec2I vec, int z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I scale(IVec2I vec, double z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I scale(IVec2D vec, int z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I scale(IVec2D vec, double z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3I scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3I divide(int val) {
        return new Vec3I(this.x / val, this.y / val, this.z / val);
    }

    @Override
    public IVec3I divide(double val) {
        return new Vec3I(this.x / val, this.y / val, this.z / val);
    }

    @Override
    public IVec3I divide(int x, int y, int z) {
        return new Vec3I(this.x / x, this.y / y, this.z / z);
    }

    @Override
    public IVec3I divide(double x, double y, double z) {
        return new Vec3I(this.x / x, this.y / y, this.z / z);
    }

    @Override
    public IVec3I divide(IVec3I vec) {
        return this.divide(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I divide(IVec3D vec) {
        return this.divide(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3I divide(IVec2I vec, int z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I divide(IVec2I vec, double z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I divide(IVec2D vec, int z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I divide(IVec2D vec, double z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3I divide(IVec2I vec) {
        return this.divide(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3I divide(IVec2D vec) {
        return this.divide(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3I rotateX(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(this.x, BananaMath.round(this.y * cos + this.z * sin), BananaMath.round(this.z * cos - this.y * sin));
    }

    @Override
    public IVec3I rotateY(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(BananaMath.round(this.x * cos + this.z * sin), this.y, BananaMath.round(this.z * cos - this.x * sin));
    }

    @Override
    public IVec3I rotateZ(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(BananaMath.round(this.x * cos + this.y * sin), BananaMath.round(this.y * cos - this.x * sin), this.z);
    }

    @Override
    public IVec3I rotateXAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(this.x, BananaMath.round(((this.y - y) * cos + (this.z - z) * sin) + y), BananaMath.round(((this.z - z) * cos - (this.y - y) * sin) + z));
    }

    @Override
    public IVec3I rotateXAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(this.x, BananaMath.round(((this.y - y) * cos + (this.z - z) * sin) + y), BananaMath.round(((this.z - z) * cos - (this.y - y) * sin) + z));
    }

    @Override
    public IVec3I rotateXAround(IVec3I point, double degrees) {
        return this.rotateXAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3I rotateXAround(IVec3D point, double degrees) {
        return this.rotateXAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3I rotateYAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(((this.x - x) * cos + (this.z - z) * sin) + x, this.y, ((this.z - z) * cos - (this.x - x) * sin) + z);
    }

    @Override
    public IVec3I rotateYAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(((this.x - x) * cos + (this.z - z) * sin) + x, this.y, ((this.z - z) * cos - (this.x - x) * sin) + z);
    }

    @Override
    public IVec3I rotateYAround(IVec3I point, double degrees) {
        return this.rotateYAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3I rotateYAround(IVec3D point, double degrees) {
        return this.rotateYAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3I rotateZAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(((this.x - x) * cos + (this.y - y) * sin) + x, ((this.y - y) * cos - (this.x - x) * sin) + y, this.z);
    }

    @Override
    public IVec3I rotateZAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3I(((this.x - x) * cos + (this.y - y) * sin) + x, ((this.y - y) * cos - (this.x - x) * sin) + y, this.z);
    }

    @Override
    public IVec3I rotateZAround(IVec3I point, double degrees) {
        return this.rotateZAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3I rotateZAround(IVec3D point, double degrees) {
        return this.rotateZAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3I wrap(IBox3I box) {
        return this.wrap(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3I wrap(IBox3D box) {
        return this.wrap(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3I wrap(IBox2I box, int minZ, int maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3I wrap(IBox2I box, double minZ, double maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3I wrap(IBox2D box, int minZ, int maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3I wrap(IBox2D box, double minZ, double maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3I wrap(IBox2I box) {
        return this.wrap(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 1);
    }

    @Override
    public IVec3I wrap(IBox2D box) {
        return this.wrap(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 1);
    }

    @Override
    public IVec3I wrap(int x, int y, int z) {
        return this.wrap(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3I wrap(double x, double y, double z) {
        return this.wrap(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3I wrap(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        int rangeX = (maxX - minX), rangeY = (maxY - minY), rangeZ = (maxZ - minZ);
        return new Vec3I(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY, minZ + ((this.z - minZ) % rangeZ + rangeZ) % rangeZ);
    }

    @Override
    public IVec3I wrap(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double rangeX = (maxX - minX), rangeY = (maxY - minY), rangeZ = (maxZ - minZ);
        return new Vec3I(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY, minZ + ((this.z - minZ) % rangeZ + rangeZ) % rangeZ);
    }

    @Override
    public IVec3I clamp(IBox3I box) {
        return this.clamp(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3I clamp(IBox3D box) {
        return this.clamp(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3I clamp(IBox2I box, int minZ, int maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3I clamp(IBox2I box, double minZ, double maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3I clamp(IBox2D box, int minZ, int maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3I clamp(IBox2D box, double minZ, double maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3I clamp(IBox2I box) {
        return this.clamp(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 0);
    }

    @Override
    public IVec3I clamp(IBox2D box) {
        return this.clamp(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 0);
    }

    @Override
    public IVec3I clamp(int x, int y, int z) {
        return this.clamp(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3I clamp(double x, double y, double z) {
        return this.clamp(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3I clamp(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return new Vec3I(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)), Math.max(minZ, Math.min(maxZ, this.z)));
    }

    @Override
    public IVec3I clamp(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new Vec3I(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)), Math.max(minZ, Math.min(maxZ, this.z)));
    }

    @Override
    public IVec3D normalize() {
        double l = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return l < 1.0E-4D ? ZERO : new Vec3D(this.x / l, this.y / l, this.z / l);
    }

    @Override
    public IVec3IM toMutable() {
        return new Vec3IM(this);
    }

    @Override
    public IVec3I toImmutable() {
        return this;
    }

    @Override
    public IVec3D toDouble() {
        return new Vec3D(this);
    }

    @Override
    public IVec3I copy() {
        return new Vec3I(this);
    }
}
