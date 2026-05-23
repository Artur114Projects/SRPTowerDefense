package com.artur114.bananalib.math.m2d.vec;

import java.util.Arrays;

public class Vec2DM implements IVec2DM {
    private static final ThreadLocal<Vec2DM[]> pool = ThreadLocal.withInitial(() -> new Vec2DM[16]);
    private static final ThreadLocal<Integer> poolCursor = ThreadLocal.withInitial(() -> -1);

    public static Vec2DM obtain() {
        Vec2DM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor < 0) {
            return new Vec2DM();
        }

        Vec2DM matrix = pol[polCursor];
        pol[polCursor--] = null;
        poolCursor.set(polCursor);

        if (matrix == null) {
            return new Vec2DM();
        }

        matrix.released = false;
        return matrix;
    }

    public static void release(Vec2DM matrix) {
        if (matrix == null) {
            return;
        }
        if (matrix.released) {
            throw new IllegalArgumentException("Double release!");
        }

        Vec2DM[] pol = pool.get();
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
    private double x, y;

    public Vec2DM() {}

    public Vec2DM(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2DM(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2DM(IVec2D vec2D) {
        this(vec2D.x(), vec2D.y());
    }

    public Vec2DM(IVec2I vec2D) {
        this(vec2D.x(), vec2D.y());
    }

    @Override
    public IVec2DM set(double[] pos) {
        this.x = pos[0];
        this.y = pos[1];
        return this;
    }

    @Override
    public IVec2DM set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public IVec2DM set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public IVec2DM set(IVec2D vec) {
        return this.set(vec.x(), vec.y());
    }

    @Override
    public IVec2DM set(IVec2I vec) {
        return this.set(vec.x(), vec.y());
    }

    @Override
    public IVec2DM setZero() {
        this.x = 0;
        this.y = 0;
        return this;
    }

    @Override
    public IVec2DM collapseStack() {
        this.stateStack = null;
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IVec2DM resetStack() {
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IVec2DM pushPos() {
        if (this.stateStack == null) {
            this.stateStack = new double[1][];
        }
        if (this.stateCursor >= this.stateStack.length) {
            this.stateStack = Arrays.copyOf(this.stateStack, this.stateCursor + 1);
        }
        double[] arr = this.stateStack[this.stateCursor++];

        if (arr == null) {
            arr = new double[2];
        }

        arr[0] = this.x;
        arr[1] = this.y;

        this.stateStack[this.stateCursor - 1] = arr;

        return this;
    }

    @Override
    public IVec2DM popPos() {
        if (this.stateCursor - 1 < 0) {
            throw new IllegalStateException();
        }
        return this.set(this.stateStack[--this.stateCursor]);
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
    public double length() {
        return Math.sqrt(this.lengthSq());
    }

    @Override
    public double lengthSq() {
        return this.x() * this.x() + this.y() * this.y();
    }

    @Override
    public double distance(int x, int y) {
        double deltaX = x - this.x();
        double deltaY = y - this.y();
        return Math.sqrt(deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public double distance(double x, double y) {
        double deltaX = x - this.x();
        double deltaY = y - this.y();
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
        double deltaX = x - this.x();
        double deltaY = y - this.y();
        return deltaY * deltaY + deltaX * deltaX;
    }

    @Override
    public double distanceSq(double x, double y) {
        double deltaX = x - this.x();
        double deltaY = y - this.y();
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
    public IVec2DM add(int x, int y) {
        return this.set(this.x() + x, this.y() + y);
    }

    @Override
    public IVec2DM add(double x, double y) {
        return this.set(this.x() + x, this.y() + y);
    }

    @Override
    public IVec2DM add(IVec2I vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2DM add(IVec2D vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2DM subtract(int x, int y) {
        return this.set(this.x() - x, this.y() - y);
    }

    @Override
    public IVec2DM subtract(double x, double y) {
        return this.set(this.x() - x, this.y() - y);
    }

    @Override
    public IVec2DM subtract(IVec2I vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2DM subtract(IVec2D vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2DM scale(int val) {
        return this.set(this.x() * val, this.y() * val);
    }

    @Override
    public IVec2DM scale(int x, int y) {
        return this.set(this.x() * x, this.y() * y);
    }

    @Override
    public IVec2DM scale(double x, double y) {
        return this.set(this.x() * x, this.y() * y);
    }

    @Override
    public IVec2DM scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2DM scale(double val) {
        return this.set(this.x() * val, this.y() * val);
    }

    @Override
    public IVec2DM scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2DM normalize() {
        double l = Math.sqrt(this.x() * this.x() + this.y() * this.y());
        return l < 1.0E-4D ? this.set(0, 0) : this.set(this.x() / l, this.y() / l);
    }

    @Override
    public IVec2DM toMutable() {
        return this;
    }

    @Override
    public IVec2D toImmutable() {
        return new Vec2D(this.x(), this.y());
    }

    @Override
    public IVec2IM toInt() {
        return new Vec2IM(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IVec2D && ((IVec2D) obj).x() == this.x() && ((IVec2D) obj).y() == this.y();
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(this.x()) + Double.hashCode(this.y());
    }

    @Override
    public String toString() {
        return "(" + ((float) this.x()) + ", " + ((float) this.y()) + ")";
    }
}
