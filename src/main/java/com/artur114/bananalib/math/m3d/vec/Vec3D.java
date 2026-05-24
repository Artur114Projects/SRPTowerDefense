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

public class Vec3D implements IVec3D {
    public static final Vec3D ZERO = new Vec3D(0, 0, 0);
    private final double x, y, z;

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(IVec3D vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public Vec3D(IVec3I vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public Vec3D(IVec2D vec2D, double z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3D(IVec2I vec2D, int z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3D(IVec2I vec2D, double z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3D(IVec2D vec2D, int z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3D(IVec2D vec2D) {
        this(vec2D.x(), vec2D.y(), 0);
    }

    public Vec3D(IVec2I vec2D) {
        this(vec2D.x(), vec2D.y(), 0);
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public IVec2D xy() {
        return new Vec2D(this.x, this.y);
    }

    @Override
    public IVec2D xz() {
        return new Vec2D(this.x, this.z);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2D yx() {
        return new Vec2D(this.y, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2D yz() {
        return new Vec2D(this.y, this.z);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2D zx() {
        return new Vec2D(this.z, this.x);
    }

    @Override
    public IVec2D zy() {
        return new Vec2D(this.z, this.y);
    }

    @Override
    public IVec3D zyx() {
        return new Vec3D(this.z, this.y, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3D zxy() {
        return new Vec3D(this.z, this.x, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3D yzx() {
        return new Vec3D(this.y, this.z, this.x);
    }

    @Override
    public IVec3D xzy() {
        return new Vec3D(this.x, this.z, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3D yxz() {
        return new Vec3D(this.y, this.x, this.z);
    }

    @Override
    public IVec2I xyI() {
        return new Vec2I(this.x, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I xzI() {
        return new Vec2I(this.z, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I yxI() {
        return new Vec2I(this.y, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I yzI() {
        return new Vec2I(this.y, this.z);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I zxI() {
        return new Vec2I(this.z, this.x);
    }

    @Override
    public IVec2I zyI() {
        return new Vec2I(this.z, this.y);
    }

    @Override
    public IVec3I zyxI() {
        return new Vec3I(this.z, this.y, this.x);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3I zxyI() {
        return new Vec3I(this.z, this.x, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3I yzxI() {
        return new Vec3I(this.y, this.z, this.x);
    }

    @Override
    public IVec3I xzyI() {
        return new Vec3I(this.x, this.z, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3I yxzI() {
        return new Vec3I(this.y, this.x, this.z);
    }

    @Override
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Override
    public double lengthSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Override
    public double dot(IVec3I vec) {
        return this.x * vec.x() + this.y * vec.y() + this.z * vec.z();
    }

    @Override
    public double dot(IVec3D vec) {
        return this.x * vec.x() + this.y * vec.y() + this.z * vec.z();
    }

    @Override
    public IVec3D cross(IVec3I vec) {
        return new Vec3D(this.y * vec.z() - this.z * vec.y(), this.z * vec.x() - this.x * vec.z(), this.x * vec.y() - this.y * vec.x());
    }

    @Override
    public IVec3D cross(IVec3D vec) {
        return new Vec3D(this.y * vec.z() - this.z * vec.y(), this.z * vec.x() - this.x * vec.z(), this.x * vec.y() - this.y * vec.x());
    }

    @Override
    public double distance(int x, int y, int z) {
        double deltaX = x - this.x, deltaY = y - this.y, deltaZ = z - this.z;
        return Math.sqrt(deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ);
    }

    @Override
    public double distance(double x, double y, double z) {
        double deltaX = x - this.x, deltaY = y - this.y, deltaZ = z - this.z;
        return Math.sqrt(deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ);
    }

    @Override
    public double distance(IVec3I vec) {
        return this.distance(vec.x(), vec.y(), vec.z());
    }

    @Override
    public double distance(IVec3D vec) {
        return this.distance(vec.x(), vec.y(), vec.z());
    }

    @Override
    public double distanceSq(int x, int y, int z) {
        double deltaX = x - this.x, deltaY = y - this.y, deltaZ = z - this.z;
        return deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ;
    }

    @Override
    public double distanceSq(double x, double y, double z) {
        double deltaX = x - this.x, deltaY = y - this.y, deltaZ = z - this.z;
        return deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ;
    }

    @Override
    public double distanceSq(IVec3I vec) {
        return this.distanceSq(vec.x(), vec.y(), vec.z());
    }

    @Override
    public double distanceSq(IVec3D vec) {
        return this.distanceSq(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D add(int x, int y, int z) {
        return new Vec3D(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public IVec3D add(double x, double y, double z) {
        return new Vec3D(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public IVec3D add(IVec3I vec) {
        return this.add(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D add(IVec3D vec) {
        return this.add(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D add(IVec2I vec, int z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D add(IVec2I vec, double z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D add(IVec2D vec, int z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D add(IVec2D vec, double z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D add(IVec2I vec) {
        return this.add(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3D add(IVec2D vec) {
        return this.add(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3D subtract(int x, int y, int z) {
        return new Vec3D(this.x - x, this.y - y, this.z - z);
    }

    @Override
    public IVec3D subtract(double x, double y, double z) {
        return new Vec3D(this.x - x, this.y - y, this.z - z);
    }

    @Override
    public IVec3D subtract(IVec3I vec) {
        return this.subtract(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D subtract(IVec3D vec) {
        return this.subtract(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D subtract(IVec2I vec, int z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D subtract(IVec2I vec, double z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D subtract(IVec2D vec, int z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D subtract(IVec2D vec, double z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D subtract(IVec2I vec) {
        return this.subtract(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3D subtract(IVec2D vec) {
        return this.subtract(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3D scale(int val) {
        return new Vec3D(this.x * val, this.y * val, this.z * val);
    }

    @Override
    public IVec3D scale(double val) {
        return new Vec3D(this.x * val, this.y * val, this.z * val);
    }

    @Override
    public IVec3D scale(int x, int y, int z) {
        return new Vec3D(this.x * x, this.y * y, this.z * z);
    }

    @Override
    public IVec3D scale(double x, double y, double z) {
        return new Vec3D(this.x * x, this.y * y, this.z * z);
    }

    @Override
    public IVec3D scale(IVec3I vec) {
        return this.scale(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D scale(IVec3D vec) {
        return this.scale(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D scale(IVec2I vec, int z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D scale(IVec2I vec, double z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D scale(IVec2D vec, int z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D scale(IVec2D vec, double z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3D scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3D divide(int val) {
        return new Vec3D(this.x / val, this.y / val, this.z / val);
    }

    @Override
    public IVec3D divide(double val) {
        return new Vec3D(this.x / val, this.y / val, this.z / val);
    }

    @Override
    public IVec3D divide(int x, int y, int z) {
        return new Vec3D(this.x / x, this.y / y, this.z / z);
    }

    @Override
    public IVec3D divide(double x, double y, double z) {
        return new Vec3D(this.x / x, this.y / y, this.z / z);
    }

    @Override
    public IVec3D divide(IVec3I vec) {
        return this.divide(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D divide(IVec3D vec) {
        return this.divide(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3D divide(IVec2I vec, int z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D divide(IVec2I vec, double z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D divide(IVec2D vec, int z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D divide(IVec2D vec, double z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3D divide(IVec2I vec) {
        return this.divide(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3D divide(IVec2D vec) {
        return this.divide(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3D rotateX(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(this.x, this.y * cos + this.z * sin, this.z * cos - this.y * sin);
    }

    @Override
    public IVec3D rotateY(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(this.x * cos + this.z * sin, this.y, this.z * cos - this.x * sin);
    }

    @Override
    public IVec3D rotateZ(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(this.x * cos + this.y * sin, this.y * cos - this.x * sin, this.z);
    }

    @Override
    public IVec3D rotateXAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(this.x, ((this.y - y) * cos + (this.z - z) * sin) + y, ((this.z - z) * cos - (this.y - y) * sin) + z);
    }

    @Override
    public IVec3D rotateXAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(this.x, ((this.y - y) * cos + (this.z - z) * sin) + y, ((this.z - z) * cos - (this.y - y) * sin) + z);
    }

    @Override
    public IVec3D rotateXAround(IVec3I point, double degrees) {
        return this.rotateXAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3D rotateXAround(IVec3D point, double degrees) {
        return this.rotateXAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3D rotateYAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(((this.x - x) * cos + (this.z - z) * sin) + x, this.y, ((this.z - z) * cos - (this.x - x) * sin) + z);
    }

    @Override
    public IVec3D rotateYAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(((this.x - x) * cos + (this.z - z) * sin) + x, this.y, ((this.z - z) * cos - (this.x - x) * sin) + z);
    }

    @Override
    public IVec3D rotateYAround(IVec3I point, double degrees) {
        return this.rotateYAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3D rotateYAround(IVec3D point, double degrees) {
        return this.rotateYAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3D rotateZAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(((this.x - x) * cos + (this.y - y) * sin) + x, ((this.y - y) * cos - (this.x - x) * sin) + y, this.z);
    }

    @Override
    public IVec3D rotateZAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec3D(((this.x - x) * cos + (this.y - y) * sin) + x, ((this.y - y) * cos - (this.x - x) * sin) + y, this.z);
    }

    @Override
    public IVec3D rotateZAround(IVec3I point, double degrees) {
        return this.rotateZAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3D rotateZAround(IVec3D point, double degrees) {
        return this.rotateZAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3D wrap(IBox3I box) {
        return this.wrap(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3D wrap(IBox3D box) {
        return this.wrap(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3D wrap(IBox2I box, int minZ, int maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3D wrap(IBox2I box, double minZ, double maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3D wrap(IBox2D box, int minZ, int maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3D wrap(IBox2D box, double minZ, double maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3D wrap(IBox2I box) {
        return this.wrap(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 1);
    }

    @Override
    public IVec3D wrap(IBox2D box) {
        return this.wrap(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 1);
    }

    @Override
    public IVec3D wrap(int x, int y, int z) {
        return this.wrap(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3D wrap(double x, double y, double z) {
        return this.wrap(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3D wrap(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        double rangeX = (maxX - minX), rangeY = (maxY - minY), rangeZ = (maxZ - minZ);
        return new Vec3D(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY, minZ + ((this.z - minZ) % rangeZ + rangeZ) % rangeZ);
    }

    @Override
    public IVec3D wrap(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double rangeX = (maxX - minX), rangeY = (maxY - minY), rangeZ = (maxZ - minZ);
        return new Vec3D(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY, minZ + ((this.z - minZ) % rangeZ + rangeZ) % rangeZ);
    }

    @Override
    public IVec3D clamp(IBox3I box) {
        return this.clamp(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3D clamp(IBox3D box) {
        return this.clamp(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3D clamp(IBox2I box, int minZ, int maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3D clamp(IBox2I box, double minZ, double maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3D clamp(IBox2D box, int minZ, int maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3D clamp(IBox2D box, double minZ, double maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3D clamp(IBox2I box) {
        return this.clamp(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 0);
    }

    @Override
    public IVec3D clamp(IBox2D box) {
        return this.clamp(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 0);
    }

    @Override
    public IVec3D clamp(int x, int y, int z) {
        return this.clamp(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3D clamp(double x, double y, double z) {
        return this.clamp(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3D clamp(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return new Vec3D(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)), Math.max(minZ, Math.min(maxZ, this.z)));
    }

    @Override
    public IVec3D clamp(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new Vec3D(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)), Math.max(minZ, Math.min(maxZ, this.z)));
    }

    @Override
    public IVec3D normalize() {
        double l = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return l < 1.0E-4D ? ZERO : new Vec3D(this.x / l, this.y / l, this.z / l);
    }

    @Override
    public IVec3DM toMutable() {
        return new Vec3DM(this);
    }

    @Override
    public IVec3D toImmutable() {
        return this;
    }

    @Override
    public IVec3I floor() {
        return new Vec3I(this);
    }

    @Override
    public IVec3I round() {
        return new Vec3I(BananaMath.round(this.x), BananaMath.round(this.y), BananaMath.round(this.z));
    }

    @Override
    public IVec3I ceil() {
        return new Vec3I(BananaMath.ceil(this.x), BananaMath.ceil(this.y), BananaMath.ceil(this.z));
    }

    @Override
    public IVec3D copy() {
        return new Vec3D(this);
    }
}
