package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

import java.util.Arrays;
import java.util.Objects;

public class Box2DM implements IBox2DM {
    private static final ThreadLocal<Box2DM[]> pool = ThreadLocal.withInitial(() -> new Box2DM[16]);
    private static final ThreadLocal<Integer> poolCursor = ThreadLocal.withInitial(() -> -1);

    public static Box2DM obtain() {
        Box2DM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor < 0) {
            return new Box2DM();
        }

        Box2DM matrix = pol[polCursor];
        pol[polCursor--] = null;
        poolCursor.set(polCursor);

        if (matrix == null) {
            return new Box2DM();
        }

        matrix.released = false;
        return matrix;
    }

    public static void release(Box2DM matrix) {
        if (matrix == null) {
            return;
        }
        if (matrix.released) {
            throw new IllegalArgumentException("Double release!");
        }

        Box2DM[] pol = pool.get();
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

    private double minX, minY, maxX, maxY;
    private double[][] stateStack = null;
    private int stateCursor = 0;
    private boolean released;

    public Box2DM() {}

    public Box2DM(double minX, double minY, double maxX, double maxY) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
    }

    public Box2DM(int minX, int minY, int maxX, int maxY) {
        this(minX, minY, maxX, (double) maxY);
    }

    public Box2DM(IVec2D from, IVec2D to) {
        this(from.x(), from.y(), to.x(), to.y());
    }

    public Box2DM(IVec2I from, IVec2I to) {
        this(from.x(), from.y(), to.x(), to.y());
    }

    public Box2DM(IBox2D box) {
        this(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    public Box2DM(IBox2I box) {
        this(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IBox2DM set(double[] box) {
        if (box.length < 4) {
            throw new IllegalArgumentException();
        }
        this.minX = Math.min(box[0], box[2]);
        this.minY = Math.min(box[1], box[3]);
        this.maxX = Math.max(box[0], box[2]);
        this.maxY = Math.max(box[1], box[3]);
        return this;
    }

    @Override
    public IBox2DM set(int minX, int minY, int maxX, int maxY) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        return this;
    }

    @Override
    public IBox2DM set(double minX, double minY, double maxX, double maxY) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        return this;
    }

    @Override
    public IBox2DM set(IVec2D boxFrom, IVec2D boxTo) {
        return this.set(boxFrom.x(), boxFrom.y(), boxTo.x(), boxTo.y());
    }

    @Override
    public IBox2DM set(IVec2I boxFrom, IVec2I boxTo) {
        return this.set(boxFrom.x(), boxFrom.y(), boxTo.x(), boxTo.y());
    }

    @Override
    public IBox2DM set(IBox2D area2D) {
        return this.set(area2D.minX(), area2D.minY(), area2D.maxX(), area2D.maxY());
    }

    @Override
    public IBox2DM set(IBox2I area2D) {
        return this.set(area2D.minX(), area2D.minY(), area2D.maxX(), area2D.maxY());
    }

    @Override
    public IBox2DM setZero() {
        this.minX = 0.0D;
        this.minY = 0.0D;
        this.maxX = 0.0D;
        this.maxY = 0.0D;
        return this;
    }

    @Override
    public IBox2DM collapseStack() {
        this.stateStack = null;
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IBox2DM resetStack() {
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IBox2DM pushBox() {
        if (this.stateStack == null) {
            this.stateStack = new double[1][];
        }
        if (this.stateCursor >= this.stateStack.length) {
            this.stateStack = Arrays.copyOf(this.stateStack, this.stateCursor + 1);
        }
        double[] arr = this.stateStack[this.stateCursor++];

        if (arr == null) {
            arr = new double[4];
        }

        arr[0] = this.minX;
        arr[1] = this.minY;
        arr[2] = this.maxX;
        arr[3] = this.maxY;

        this.stateStack[this.stateCursor - 1] = arr;

        return this;
    }

    @Override
    public IBox2DM popBox() {
        if (this.stateCursor - 1 < 0) {
            throw new IllegalStateException();
        }
        return this.set(this.stateStack[--this.stateCursor]);
    }

    @Override
    public double minX() {
        return this.minX;
    }

    @Override
    public double minY() {
        return this.minY;
    }

    @Override
    public double maxX() {
        return this.maxX;
    }

    @Override
    public double maxY() {
        return this.maxY;
    }

    @Override
    public double size() {
        return (this.maxX - this.minX) * (this.maxY - this.minY);
    }

    @Override
    public IBox2DM grow(double amount) {
        return this.grow(amount, amount);
    }

    @Override
    public IBox2DM grow(double x, double y) {
        return this.set(this.minX() - x, this.minY() - y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2DM grow(int amount) {
        return this.grow(amount, amount);
    }

    @Override
    public IBox2DM grow(int x, int y) {
        return this.set(this.minX() - x, this.minY() - y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2DM grow(IVec2I vec2D) {
        return this.grow(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2DM grow(IVec2D vec2D) {
        return this.grow(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2DM offset(int x, int y) {
        return this.set(this.minX() + x, this.minY() + y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2DM offset(double x, double y) {
        return this.set(this.minX() + x, this.minY() + y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2DM offset(IVec2D vec2D) {
        return this.offset(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2DM offset(IVec2I vec2D) {
        return this.offset(vec2D.x(), vec2D.y());
    }

    @Override
    public boolean intersects(int minX, int minY, int maxX, int maxY) {
        return this.minX() <= maxX && this.maxX() >= minX && this.minY() <= maxY && this.maxY() >= minY;
    }

    @Override
    public boolean intersects(double minX, double minY, double maxX, double maxY) {
        return this.minX() <= maxX && this.maxX() >= minX && this.minY() <= maxY && this.maxY() >= minY;
    }

    @Override
    public boolean intersects(IVec2D boxFrom, IVec2D boxTo) {
        return this.intersects(boxFrom.x(), boxFrom.y(), boxTo.x(), boxTo.y());
    }

    @Override
    public boolean intersects(IVec2I boxFrom, IVec2I boxTo) {
        return this.intersects(boxFrom.x(), boxFrom.y(), boxTo.x(), boxTo.y());
    }

    @Override
    public boolean intersects(IBox2D area2D) {
        return this.intersects(area2D.minX(), area2D.minY(), area2D.maxX(), area2D.maxY());
    }

    @Override
    public boolean intersects(IBox2I area2D) {
        return this.intersects(area2D.minX(), area2D.minY(), area2D.maxX(), area2D.maxY());
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= this.minX() && y >= this.minY() && x <= this.maxX() && y <= this.maxY();
    }

    @Override
    public boolean contains(double x, double y) {
        return x >= this.minX() && y >= this.minY() && x <= this.maxX() && y <= this.maxY();
    }

    @Override
    public boolean contains(IBox2I area2D) {
        return this.contains(area2D.minX(), area2D.minY()) && this.contains(area2D.maxX(), area2D.maxY());
    }

    @Override
    public boolean contains(IBox2D area2D) {
        return this.contains(area2D.minX(), area2D.minY()) && this.contains(area2D.maxX(), area2D.maxY());
    }

    @Override
    public boolean contains(IVec2I vec2D) {
        return this.contains(vec2D.x(), vec2D.y());
    }

    @Override
    public boolean contains(IVec2D vec2D) {
        return this.contains(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2DM toMutable() {
        return this;
    }

    @Override
    public IBox2D toImmutable() {
        return new Box2D(this);
    }

    @Override
    public IBox2IM floor() {
        return new Box2IM(this);
    }

    @Override
    public IBox2IM round() {
        return new Box2IM(BananaMath.round(this.minX), BananaMath.round(this.minY), BananaMath.round(this.maxX), BananaMath.round(this.maxY));
    }

    @Override
    public IBox2IM ceil() {
        return new Box2IM(BananaMath.ceil(this.minX), BananaMath.ceil(this.minY), BananaMath.ceil(this.maxX), BananaMath.ceil(this.maxY));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IBox2D && ((IBox2D) obj).minX() == this.minX() && ((IBox2D) obj).minY() == this.minY() && ((IBox2D) obj).maxX() == this.maxX() && ((IBox2D) obj).maxY() == this.maxY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minX(), this.minY(), this.maxX(), this.maxY());
    }

    @Override
    public String toString() {
        return "[" + ((float) this.minX()) + ", " + ((float) this.minY()) + "] -> ["  + ((float) this.maxX()) + ", " + ((float) this.maxY()) + "]";
    }
}
