package com.artur114.bananalib.math.m2d.vec;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.IBox2D;
import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m3d.vec.IVec3D;
import com.artur114.bananalib.math.m3d.vec.IVec3I;
import com.artur114.bananalib.math.m3d.vec.Vec3D;
import com.artur114.bananalib.math.m3d.vec.Vec3I;

import java.util.Arrays;
import java.util.Objects;

public class Vec2IM implements IVec2IM {
    private static final ThreadLocal<Vec2IM[]> pool = ThreadLocal.withInitial(() -> new Vec2IM[16]);
    private static final ThreadLocal<Integer> poolCursor = ThreadLocal.withInitial(() -> -1);

    public static Vec2IM obtain() {
        Vec2IM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor < 0) {
            return new Vec2IM();
        }

        Vec2IM matrix = pol[polCursor];
        pol[polCursor--] = null;
        poolCursor.set(polCursor);

        if (matrix == null) {
            return new Vec2IM();
        }

        matrix.released = false;
        return matrix;
    }

    public static void release(Vec2IM matrix) {
        if (matrix == null) {
            return;
        }
        if (matrix.released) {
            throw new IllegalArgumentException("Double release!");
        }

        Vec2IM[] pol = pool.get();
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

    private int[][] stateStack = null;
    private int stateCursor = 0;
    private boolean released;
    private int x, y;

    public Vec2IM() {}

    public Vec2IM(double x, double y) {
        this.x = BananaMath.floor(x);
        this.y = BananaMath.floor(y);
    }

    public Vec2IM(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2IM(IVec2D vec2D) {
        this(vec2D.x(), vec2D.y());
    }

    public Vec2IM(IVec2I vec2D) {
        this(vec2D.x(), vec2D.y());
    }

    @Override
    public IVec2IM set(int[] pos) {
        if (pos.length < 2) {
            throw new IllegalArgumentException();
        }
        this.x = pos[0];
        this.y = pos[1];
        return this;
    }

    @Override
    public IVec2IM set(double x, double y) {
        this.x = BananaMath.floor(x);
        this.y = BananaMath.floor(y);
        return this;
    }

    @Override
    public IVec2IM set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public IVec2IM set(IVec2D vec) {
        return this.set(vec.x(), vec.y());
    }

    @Override
    public IVec2IM set(IVec2I vec) {
        return this.set(vec.x(), vec.y());
    }

    @Override
    public IVec2IM setX(int x) {
        this.x = x;
        return this;
    }

    @Override
    public IVec2IM setX(double x) {
        this.x = BananaMath.floor(x);
        return this;
    }

    @Override
    public IVec2IM setY(int y) {
        this.y = y;
        return this;
    }

    @Override
    public IVec2IM setY(double y) {
        this.y = BananaMath.floor(y);
        return this;
    }

    @Override
    public IVec2IM setZero() {
        this.x = 0;
        this.y = 0;
        return this;
    }

    @Override
    public IVec2IM collapseStack() {
        this.stateStack = null;
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IVec2IM resetStack() {
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IVec2IM pushPos() {
        if (this.stateStack == null) {
            this.stateStack = new int[1][];
        }
        if (this.stateCursor >= this.stateStack.length) {
            this.stateStack = Arrays.copyOf(this.stateStack, this.stateCursor + 1);
        }
        int[] arr = this.stateStack[this.stateCursor++];

        if (arr == null) {
            arr = new int[2];
        }

        arr[0] = this.x;
        arr[1] = this.y;

        this.stateStack[this.stateCursor - 1] = arr;

        return this;
    }

    @Override
    public IVec2IM popPos() {
        if (this.stateCursor - 1 < 0) {
            throw new IllegalStateException();
        }
        return this.set(this.stateStack[--this.stateCursor]);
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
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2I yx() {
        return new Vec2I(this.y, this.x);
    }

    @Override
    public IVec3I xyz(int z) {
        return new Vec3I(this.x, this.y, z);
    }

    @Override
    public IVec3I xzy(int z) {
        return new Vec3I(this.x, z, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3I zxy(int z) {
        return new Vec3I(z, this.x, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec2D yxD() {
        return new Vec2D(this.y, this.x);
    }

    @Override
    public IVec3D xyzD(double z) {
        return new Vec3D(this.x, this.y, z);
    }

    @Override
    public IVec3D xzyD(double z) {
        return new Vec3D(this.x, z, this.y);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public IVec3D zxyD(double z) {
        return new Vec3D(z, this.x, this.y);
    }

    @Override
    public float length() {
        return (float) Math.sqrt(this.lengthSq());
    }

    @Override
    public float lengthSq() {
        return this.x * this.x + this.y * this.y;
    }

    @Override
    public int dot(IVec2I vec) {
        return this.x * vec.x() + this.y * vec.y();
    }

    @Override
    public double dot(IVec2D vec) {
        return this.x * vec.x() + this.y * vec.y();
    }

    @Override
    public int cross(IVec2I vec) {
        return this.x * vec.y() - this.y * vec.x();
    }

    @Override
    public double cross(IVec2D vec) {
        return this.x * vec.y() - this.y * vec.x();
    }

    @Override
    public float distance(int x, int y) {
        long deltaX = x - this.x;
        long deltaY = y - this.y;
        return (float) Math.sqrt(deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public float distance(double x, double y) {
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        return (float) Math.sqrt(deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public float distance(IVec2I vec) {
        return this.distance(vec.x(), vec.y());
    }

    @Override
    public float distance(IVec2D vec) {
        return this.distance(vec.x(), vec.y());
    }

    @Override
    public float distanceSq(int x, int y) {
        long deltaX = x - this.x;
        long deltaY = y - this.y;
        return (float) (deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public float distanceSq(double x, double y) {
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        return (float) (deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public float distanceSq(IVec2I vec) {
        return this.distanceSq(vec.x(), vec.y());
    }

    @Override
    public float distanceSq(IVec2D vec) {
        return this.distanceSq(vec.x(), vec.y());
    }

    @Override
    public IVec2IM add(int x, int y) {
        return this.set(this.x + x, this.y + y);
    }

    @Override
    public IVec2IM add(double x, double y) {
        return this.set(this.x + x, this.y + y);
    }

    @Override
    public IVec2IM add(IVec2I vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2IM add(IVec2D vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2IM subtract(int x, int y) {
        return this.set(this.x - x, this.y - y);
    }

    @Override
    public IVec2IM subtract(double x, double y) {
        return this.set(this.x - x, this.y - y);
    }

    @Override
    public IVec2IM subtract(IVec2I vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2IM subtract(IVec2D vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2IM scale(int val) {
        return this.set(this.x * val, this.y * val);
    }

    @Override
    public IVec2IM scale(int x, int y) {
        return this.set(this.x * x, this.y * y);
    }

    @Override
    public IVec2IM scale(double x, double y) {
        return this.set(this.x * x, this.y * y);
    }

    @Override
    public IVec2IM scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2IM scale(double val) {
        return this.set(this.x * val, this.y * val);
    }

    @Override
    public IVec2IM scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2IM divide(int val) {
        return this.set(this.x / val, this.y / val);
    }

    @Override
    public IVec2IM divide(int x, int y) {
        return this.set(this.x / x, this.y / y);
    }

    @Override
    public IVec2IM divide(double val) {
        return this.set(this.x / val, this.y / val);
    }

    @Override
    public IVec2IM divide(double x, double y) {
        return this.set(this.x / x, this.y / y);
    }

    @Override
    public IVec2IM divide(IVec2I vec) {
        return this.divide(vec.x(), vec.y());
    }

    @Override
    public IVec2IM divide(IVec2D vec) {
        return this.divide(vec.x(), vec.y());
    }

    @Override
    public IVec2IM rotate(double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(BananaMath.round(this.x * cos + this.y * sin), BananaMath.round(this.y * cos - this.x * sin));
    }

    @Override
    public IVec2IM rotateAround(int x, int y, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(BananaMath.round(((this.x - x) * cos + (this.y - y) * sin) + x), BananaMath.round(((this.y - y) * cos - (this.x - x) * sin) + y));
    }

    @Override
    public IVec2IM rotateAround(double x, double y, double degrees) {
        if (Math.abs(degrees) < BananaMath.DOUBLE_EPS) {
            return this;
        }

        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        return this.set(BananaMath.round(((this.x - x) * cos + (this.y - y) * sin) + x), BananaMath.round(((this.y - y) * cos - (this.x - x) * sin) + y));
    }

    @Override
    public IVec2IM rotateAround(IVec2D point, double degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IVec2IM rotateAround(IVec2I point, double degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IVec2IM wrap(IBox2I box) {
        return this.wrap(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IVec2IM wrap(IBox2D box) {
        return this.wrap(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IVec2IM wrap(int x, int y) {
        return this.wrap(0, 0, x, y);
    }

    @Override
    public IVec2IM wrap(double x, double y) {
        return this.wrap(0.0D, 0.0D, x, y);
    }

    @Override
    public IVec2IM wrap(int minX, int minY, int maxX, int maxY) {
        int rangeX = (maxX - minX), rangeY = (maxY - minY);
        return this.set(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY);
    }

    @Override
    public IVec2IM wrap(double minX, double minY, double maxX, double maxY) {
        double rangeX = (maxX - minX), rangeY = (maxY - minY);
        return this.set(minX + ((this.x - minX) % rangeX + rangeX) % rangeX, minY + ((this.y - minY) % rangeY + rangeY) % rangeY);
    }

    @Override
    public IVec2IM clamp(IBox2I box) {
        return this.clamp(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IVec2IM clamp(IBox2D box) {
        return this.clamp(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IVec2IM clamp(int x, int y) {
        return this.clamp(0, 0, x, y);
    }

    @Override
    public IVec2IM clamp(double x, double y) {
        return this.clamp(0.0D, 0.0D, x, y);
    }

    @Override
    public IVec2IM clamp(int minX, int minY, int maxX, int maxY) {
        return this.set(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)));
    }

    @Override
    public IVec2IM clamp(double minX, double minY, double maxX, double maxY) {
        return this.set(Math.max(minX, Math.min(maxX, this.x)), Math.max(minY, Math.min(maxY, this.y)));
    }

    @Override
    public IVec2DM normalize() {
        double l = Math.sqrt(this.x * this.x + this.y * this.y);
        return l < 1.0E-4D ? new Vec2DM(0, 0) : new Vec2DM(this.x / l, this.y / l);
    }

    @Override
    public IVec2IM toMutable() {
        return this;
    }

    @Override
    public IVec2I toImmutable() {
        return new Vec2I(this);
    }

    @Override
    public IVec2DM toDouble() {
        return new Vec2DM(this);
    }

    @Override
    public IVec2IM copy() {
        return new Vec2IM(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IVec2I && ((IVec2I) obj).x() == this.x && ((IVec2I) obj).y() == this.y;
    }

    @Override
    public int hashCode() {
        return 31 * this.x + this.y;
    }

    @Override
    public String toString() {
        return "(" + ((float) this.x) + ", " + ((float) this.y) + ")";
    }
}
