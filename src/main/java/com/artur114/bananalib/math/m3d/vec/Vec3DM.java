package com.artur114.bananalib.math.m3d.vec;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.IBox2D;
import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.vec.*;
import com.artur114.bananalib.math.m3d.box.IBox3D;
import com.artur114.bananalib.math.m3d.box.IBox3I;

import java.util.Arrays;

public class Vec3DM implements IVec3DM {
    private static final ThreadLocal<Vec3DM[]> pool = ThreadLocal.withInitial(() -> new Vec3DM[16]);
    private static final ThreadLocal<Integer> poolCursor = ThreadLocal.withInitial(() -> -1);

    public static Vec3DM obtain() {
        Vec3DM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor < 0) {
            return new Vec3DM();
        }

        Vec3DM matrix = pol[polCursor];
        pol[polCursor--] = null;
        poolCursor.set(polCursor);

        if (matrix == null) {
            return new Vec3DM();
        }

        matrix.released = false;
        return matrix;
    }

    public static void release(Vec3DM matrix) {
        if (matrix == null) {
            return;
        }
        if (matrix.released) {
            throw new IllegalArgumentException("Double release!");
        }

        Vec3DM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor + 1 >= pol.length) {
            pol = Arrays.copyOf(pol, pol.length * 2);
            pool.set(pol);
        }

        matrix.released = true;
        matrix.resetStack().setZero();
        pol[++polCursor] = matrix;
        poolCursor.set(polCursor);
    }

    private double[][] stateStack = null;
    private int stateCursor = 0;
    private boolean released;
    private double x, y, z;

    public Vec3DM() {}

    public Vec3DM(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3DM(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3DM(IVec3D vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public Vec3DM(IVec3I vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public Vec3DM(IVec2D vec2D, double z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3DM(IVec2I vec2D, int z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3DM(IVec2I vec2D, double z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3DM(IVec2D vec2D, int z) {
        this(vec2D.x(), vec2D.y(), z);
    }

    public Vec3DM(IVec2D vec2D) {
        this(vec2D.x(), vec2D.y(), 0);
    }

    public Vec3DM(IVec2I vec2D) {
        this(vec2D.x(), vec2D.y(), 0);
    }

    @Override
    public IVec3DM set(double[] pos) {
        if (pos.length < 3) {
            throw new IllegalArgumentException();
        }
        this.x = pos[0];
        this.y = pos[1];
        this.z = pos[2];
        return this;
    }

    @Override
    public IVec3DM set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public IVec3DM set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public IVec3DM set(IVec3I vec) {
        return this.set(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM set(IVec3D vec) {
        return this.set(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM set(IVec2D vec, double z) {
        return this.set(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM set(IVec2D vec, int z) {
        return this.set(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM set(IVec2I vec, double z) {
        return this.set(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM set(IVec2I vec, int z) {
        return this.set(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM set(IVec2D vec) {
        return this.set(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM set(IVec2I vec) {
        return this.set(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM setZero() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
        return this;
    }

    @Override
    public IVec3DM collapseStack() {
        this.stateStack = null;
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IVec3DM resetStack() {
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IVec3DM pushPos() {
        if (this.stateStack == null) {
            this.stateStack = new double[1][];
        }
        if (this.stateCursor >= this.stateStack.length) {
            this.stateStack = Arrays.copyOf(this.stateStack, this.stateCursor + 1);
        }
        double[] arr = this.stateStack[this.stateCursor++];

        if (arr == null) {
            arr = new double[] {this.x, this.y, this.z};
        } else {
            arr[0] = this.x;
            arr[1] = this.y;
            arr[2] = this.z;
        }

        this.stateStack[this.stateCursor - 1] = arr;

        return this;
    }

    @Override
    public IVec3DM popPos() {
        if (this.stateCursor - 1 < 0) {
            throw new IllegalStateException();
        }
        return this.set(this.stateStack[--this.stateCursor]);
    }

    @Override
    public IVec3DM setX(int x) {
        this.x = x;
        return this;
    }

    @Override
    public IVec3DM setX(double x) {
        this.x = x;
        return this;
    }

    @Override
    public IVec3DM setY(int y) {
        this.y = y;
        return this;
    }

    @Override
    public IVec3DM setY(double y) {
        this.y = y;
        return this;
    }

    @Override
    public IVec3DM setZ(int z) {
        this.z = z;
        return this;
    }

    @Override
    public IVec3DM setZ(double z) {
        this.z = z;
        return this;
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
    public IVec3DM add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public IVec3DM add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public IVec3DM add(IVec3I vec) {
        return this.add(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM add(IVec3D vec) {
        return this.add(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM add(IVec2I vec, int z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM add(IVec2I vec, double z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM add(IVec2D vec, int z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM add(IVec2D vec, double z) {
        return this.add(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM add(IVec2I vec) {
        return this.add(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM add(IVec2D vec) {
        return this.add(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM subtract(int x, int y, int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    @Override
    public IVec3DM subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    @Override
    public IVec3DM subtract(IVec3I vec) {
        return this.subtract(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM subtract(IVec3D vec) {
        return this.subtract(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM subtract(IVec2I vec, int z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM subtract(IVec2I vec, double z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM subtract(IVec2D vec, int z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM subtract(IVec2D vec, double z) {
        return this.subtract(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM subtract(IVec2I vec) {
        return this.subtract(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM subtract(IVec2D vec) {
        return this.subtract(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM scale(int val) {
        this.x *= val;
        this.y *= val;
        this.z *= val;
        return this;
    }

    @Override
    public IVec3DM scale(double val) {
        this.x *= val;
        this.y *= val;
        this.z *= val;
        return this;
    }

    @Override
    public IVec3DM scale(int x, int y, int z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    @Override
    public IVec3DM scale(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    @Override
    public IVec3DM scale(IVec3I vec) {
        return this.scale(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM scale(IVec3D vec) {
        return this.scale(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM scale(IVec2I vec, int z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM scale(IVec2I vec, double z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM scale(IVec2D vec, int z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM scale(IVec2D vec, double z) {
        return this.scale(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM divide(int val) {
        this.x /= val;
        this.y /= val;
        this.z /= val;
        return this;
    }

    @Override
    public IVec3DM divide(double val) {
        this.x /= val;
        this.y /= val;
        this.z /= val;
        return this;
    }

    @Override
    public IVec3DM divide(int x, int y, int z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    @Override
    public IVec3DM divide(double x, double y, double z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    @Override
    public IVec3DM divide(IVec3I vec) {
        return this.divide(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM divide(IVec3D vec) {
        return this.divide(vec.x(), vec.y(), vec.z());
    }

    @Override
    public IVec3DM divide(IVec2I vec, int z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM divide(IVec2I vec, double z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM divide(IVec2D vec, int z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM divide(IVec2D vec, double z) {
        return this.divide(vec.x(), vec.y(), z);
    }

    @Override
    public IVec3DM divide(IVec2I vec) {
        return this.divide(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM divide(IVec2D vec) {
        return this.divide(vec.x(), vec.y(), 0);
    }

    @Override
    public IVec3DM rotateX(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(this.x, this.y * cos + this.z * sin, this.z * cos - this.y * sin);
    }

    @Override
    public IVec3DM rotateY(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(this.x * cos + this.z * sin, this.y, this.z * cos - this.x * sin);
    }

    @Override
    public IVec3DM rotateZ(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(this.x * cos + this.y * sin, this.y * cos - this.x * sin, this.z);
    }

    @Override
    public IVec3DM rotateXAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(this.x, ((this.y - y) * cos + (this.z - z) * sin) + y, ((this.z - z) * cos - (this.y - y) * sin) + z);
    }

    @Override
    public IVec3DM rotateXAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(this.x, ((this.y - y) * cos + (this.z - z) * sin) + y, ((this.z - z) * cos - (this.y - y) * sin) + z);
    }

    @Override
    public IVec3DM rotateXAround(IVec3I point, double degrees) {
        return this.rotateXAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3DM rotateXAround(IVec3D point, double degrees) {
        return this.rotateXAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3DM rotateYAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(((this.x - x) * cos + (this.z - z) * sin) + x, this.y, ((this.z - z) * cos - (this.x - x) * sin) + z);
    }

    @Override
    public IVec3DM rotateYAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(((this.x - x) * cos + (this.z - z) * sin) + x, this.y, ((this.z - z) * cos - (this.x - x) * sin) + z);
    }

    @Override
    public IVec3DM rotateYAround(IVec3I point, double degrees) {
        return this.rotateYAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3DM rotateYAround(IVec3D point, double degrees) {
        return this.rotateYAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3DM rotateZAround(int x, int y, int z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(((this.x - x) * cos + (this.y - y) * sin) + x, ((this.y - y) * cos - (this.x - x) * sin) + y, this.z);
    }

    @Override
    public IVec3DM rotateZAround(double x, double y, double z, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(((this.x - x) * cos + (this.y - y) * sin) + x, ((this.y - y) * cos - (this.x - x) * sin) + y, this.z);
    }

    @Override
    public IVec3DM rotateZAround(IVec3I point, double degrees) {
        return this.rotateZAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3DM rotateZAround(IVec3D point, double degrees) {
        return this.rotateZAround(point.x(), point.y(), point.z(), degrees);
    }

    @Override
    public IVec3DM wrap(IBox3I box) {
        return this.wrap(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3DM wrap(IBox3D box) {
        return this.wrap(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3DM wrap(IBox2I box, int minZ, int maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3DM wrap(IBox2I box, double minZ, double maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3DM wrap(IBox2D box, int minZ, int maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3DM wrap(IBox2D box, double minZ, double maxZ) {
        return this.wrap(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3DM wrap(IBox2I box) {
        return this.wrap(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 1);
    }

    @Override
    public IVec3DM wrap(IBox2D box) {
        return this.wrap(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 1);
    }

    @Override
    public IVec3DM wrap(int x, int y, int z) {
        return this.wrap(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3DM wrap(double x, double y, double z) {
        return this.wrap(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3DM wrap(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        double rangeX = (maxX - minX), rangeY = (maxY - minY), rangeZ = (maxZ - minZ);
        return this.set(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY, minZ + ((this.z - minZ) % rangeZ + rangeZ) % rangeZ);
    }

    @Override
    public IVec3DM wrap(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double rangeX = (maxX - minX), rangeY = (maxY - minY), rangeZ = (maxZ - minZ);
        return this.set(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY, minZ + ((this.z - minZ) % rangeZ + rangeZ) % rangeZ);
    }

    @Override
    public IVec3DM clamp(IBox3I box) {
        return this.clamp(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3DM clamp(IBox3D box) {
        return this.clamp(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public IVec3DM clamp(IBox2I box, int minZ, int maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3DM clamp(IBox2I box, double minZ, double maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3DM clamp(IBox2D box, int minZ, int maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3DM clamp(IBox2D box, double minZ, double maxZ) {
        return this.clamp(box.minX(), box.minY(), minZ, box.maxX(), box.maxY(), maxZ);
    }

    @Override
    public IVec3DM clamp(IBox2I box) {
        return this.clamp(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 0);
    }

    @Override
    public IVec3DM clamp(IBox2D box) {
        return this.clamp(box.minX(), box.minY(), 0, box.maxX(), box.maxY(), 0);
    }

    @Override
    public IVec3DM clamp(int x, int y, int z) {
        return this.clamp(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3DM clamp(double x, double y, double z) {
        return this.clamp(0, 0, 0, x, y, z);
    }

    @Override
    public IVec3DM clamp(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return this.set(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)), Math.max(minZ, Math.min(maxZ, this.z)));
    }

    @Override
    public IVec3DM clamp(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.set(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)), Math.max(minZ, Math.min(maxZ, this.z)));
    }

    @Override
    public IVec3DM normalize() {
        double l = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return l < 1.0E-4D ? this.setZero() : this.set(this.x / l, this.y / l, this.z / l);
    }

    @Override
    public IVec3DM toMutable() {
        return this;
    }

    @Override
    public IVec3D toImmutable() {
        return new Vec3D(this);
    }

    @Override
    public IVec3IM floor() {
        return new Vec3IM(this);
    }

    @Override
    public IVec3IM round() {
        return new Vec3IM(BananaMath.round(this.x), BananaMath.round(this.y), BananaMath.round(this.z));
    }

    @Override
    public IVec3IM ceil() {
        return new Vec3IM(BananaMath.ceil(this.x), BananaMath.ceil(this.y), BananaMath.ceil(this.z));
    }

    @Override
    public IVec3DM copy() {
        return new Vec3DM(this);
    }
}