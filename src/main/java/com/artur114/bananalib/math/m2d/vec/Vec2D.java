package com.artur114.bananalib.math.m2d.vec;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.IBox2D;
import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m3d.vec.IVec3D;
import com.artur114.bananalib.math.m3d.vec.IVec3I;
import com.artur114.bananalib.math.m3d.vec.Vec3D;
import com.artur114.bananalib.math.m3d.vec.Vec3I;

public class Vec2D implements IVec2D {
    public static final Vec2D ZERO = new Vec2D(0, 0);
    private final double x, y;

    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2D(IVec2D vec2D) {
        this(vec2D.x(), vec2D.y());
    }

    public Vec2D(IVec2I vec2D) {
        this(vec2D.x(), vec2D.y());
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
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2D yx() {
        return new Vec2D(this.y, this.x);
    }

    @Override
    public IVec3D xyz(double z) {
        return new Vec3D(this.x, this.y, z);
    }

    @Override
    public IVec3D xzy(double z) {
        return new Vec3D(this.x, z, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3D zxy(double z) {
        return new Vec3D(z, this.x, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I yxI() {
        return new Vec2I(this.y, this.x);
    }

    @Override
    public IVec3I xyzI(int z) {
        return new Vec3I(this.x, this.y, z);
    }

    @Override
    public IVec3I xzyI(int z) {
        return new Vec3I(this.x, z, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3I zxyI(int z) {
        return new Vec3I(z, this.x, this.y);
    }

    @Override
    public double length() {
        return Math.sqrt(this.lengthSq());
    }

    @Override
    public double lengthSq() {
        return this.x * this.x + this.y * this.y;
    }

    @Override
    public double dot(IVec2I vec) {
        return this.x * vec.x() + this.y * vec.y();
    }

    @Override
    public double dot(IVec2D vec) {
        return this.x * vec.x() + this.y * vec.y();
    }

    @Override
    public double cross(IVec2I vec) {
        return this.x * vec.y() - this.y * vec.x();
    }

    @Override
    public double cross(IVec2D vec) {
        return this.x * vec.y() - this.y * vec.x();
    }

    @Override
    public double distance(int x, int y) {
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        return Math.sqrt(deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public double distance(double x, double y) {
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        return Math.sqrt(deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public double distance(IVec2I vec) {
        return this.distance(vec.x(), vec.y());
    }

    @Override
    public double distance(IVec2D vec) {
        return this.distance(vec.x(), vec.y());
    }

    @Override
    public double distanceSq(int x, int y) {
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        return deltaY * deltaY + deltaX * deltaX;
    }

    @Override
    public double distanceSq(double x, double y) {
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        return deltaY * deltaY + deltaX * deltaX;
    }

    @Override
    public double distanceSq(IVec2I vec) {
        return this.distanceSq(vec.x(), vec.y());
    }

    @Override
    public double distanceSq(IVec2D vec) {
        return this.distanceSq(vec.x(), vec.y());
    }

    @Override
    public IVec2D add(int x, int y) {
        return new Vec2D(this.x + x, this.y + y);
    }

    @Override
    public IVec2D add(double x, double y) {
        return new Vec2D(this.x + x, this.y + y);
    }

    @Override
    public IVec2D add(IVec2I vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2D add(IVec2D vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2D subtract(int x, int y) {
        return new Vec2D(this.x - x, this.y - y);
    }

    @Override
    public IVec2D subtract(double x, double y) {
        return new Vec2D(this.x - x, this.y - y);
    }

    @Override
    public IVec2D subtract(IVec2I vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2D subtract(IVec2D vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2D scale(int val) {
        return new Vec2D(this.x * val, this.y * val);
    }

    @Override
    public IVec2D scale(int x, int y) {
        return new Vec2D(this.x * x, this.y * y);
    }

    @Override
    public IVec2D scale(double val) {
        return new Vec2D(this.x * val, this.y * val);
    }

    @Override
    public IVec2D scale(double x, double y) {
        return new Vec2D(this.x * x, this.y * y);
    }

    @Override
    public IVec2D scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2D scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2D divide(int val) {
        return new Vec2D(this.x / val, this.y / val);
    }

    @Override
    public IVec2D divide(int x, int y) {
        return new Vec2D(this.x / x, this.y / y);
    }

    @Override
    public IVec2D divide(double val) {
        return new Vec2D(this.x / val, this.y / val);
    }

    @Override
    public IVec2D divide(double x, double y) {
        return new Vec2D(this.x / x, this.y / y);
    }

    @Override
    public IVec2D divide(IVec2I vec) {
        return this.divide(vec.x(), vec.y());
    }

    @Override
    public IVec2D divide(IVec2D vec) {
        return this.divide(vec.x(), vec.y());
    }

    @Override
    public IVec2D rotate(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec2D(this.x * cos + this.y * sin, this.y * cos - this.x * sin);
    }

    @Override
    public IVec2D rotateAround(int x, int y, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec2D(((this.x - x) * cos + ((this.y - y) * sin)) + x, ((this.y - y) * cos - (this.x - x) * sin) + y);
    }

    @Override
    public IVec2D rotateAround(double x, double y, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return new Vec2D(((this.x - x) * cos + ((this.y - y) * sin)) + x, ((this.y - y) * cos - (this.x - x) * sin) + y);
    }

    @Override
    public IVec2D rotateAround(IVec2D point, double degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IVec2D rotateAround(IVec2I point, double degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IVec2D wrap(IBox2I box) {
        return this.wrap(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IVec2D wrap(IBox2D box) {
        return this.wrap(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IVec2D wrap(int x, int y) {
        return this.wrap(0, 0, x, y);
    }

    @Override
    public IVec2D wrap(double x, double y) {
        return this.wrap(0.0D, 0.0D, x, y);
    }

    @Override
    public IVec2D wrap(int minX, int minY, int maxX, int maxY) {
        double rangeX = (maxX - minX), rangeY = (maxY - minY);
        return new Vec2D(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY);
    }

    @Override
    public IVec2D wrap(double minX, double minY, double maxX, double maxY) {
        double rangeX = (maxX - minX), rangeY = (maxY - minY);
        return new Vec2D(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY);
    }

    @Override
    public IVec2D clamp(IBox2I box) {
        return this.clamp(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IVec2D clamp(IBox2D box) {
        return this.clamp(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IVec2D clamp(int x, int y) {
        return this.clamp(0, 0, x, y);
    }

    @Override
    public IVec2D clamp(double x, double y) {
        return this.clamp(0.0D, 0.0D, x, y);
    }

    @Override
    public IVec2D clamp(int minX, int minY, int maxX, int maxY) {
        return new Vec2D(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)));
    }

    @Override
    public IVec2D clamp(double minX, double minY, double maxX, double maxY) {
        return new Vec2D(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)));
    }

    @Override
    public IVec2D normalize() {
        double l = Math.sqrt(this.x * this.x + this.y * this.y);
        return l < 1.0E-4D ? ZERO : new Vec2D(this.x / l, this.y / l);
    }

    @Override
    public IVec2DM toMutable() {
        return new Vec2DM(this);
    }

    @Override
    public IVec2D toImmutable() {
        return this;
    }

    @Override
    public IVec2I floor() {
        return new Vec2I(this);
    }

    @Override
    public IVec2I round() {
        return new Vec2I(BananaMath.round(this.x), BananaMath.round(this.y));
    }

    @Override
    public IVec2I ceil() {
        return new Vec2I(BananaMath.ceil(this.x), BananaMath.ceil(this.y));
    }

    @Override
    public IVec2D copy() {
        return new Vec2D(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IVec2D && ((IVec2D) obj).x() == this.x && ((IVec2D) obj).y() == this.y;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(this.x) + Double.hashCode(this.y);
    }

    @Override
    public String toString() {
        return "(" + ((float) this.x) + ", " + ((float) this.y) + ")";
    }
}
