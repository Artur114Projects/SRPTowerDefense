package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

import java.util.Arrays;
import java.util.Objects;

public class Box2IM implements IBox2IM {
    private static final ThreadLocal<Box2IM[]> pool = ThreadLocal.withInitial(() -> new Box2IM[16]);
    private static final ThreadLocal<Integer> poolCursor = ThreadLocal.withInitial(() -> -1);

    public static Box2IM obtain() {
        Box2IM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor < 0) {
            return new Box2IM();
        }

        Box2IM matrix = pol[polCursor];
        pol[polCursor--] = null;
        poolCursor.set(polCursor);

        if (matrix == null) {
            return new Box2IM();
        }

        matrix.released = false;
        return matrix;
    }

    public static void release(Box2IM matrix) {
        if (matrix == null) {
            return;
        }
        if (matrix.released) {
            throw new IllegalArgumentException("Double release!");
        }

        Box2IM[] pol = pool.get();
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

    private int minX, minY, maxX, maxY;
    private int[][] stateStack = null;
    private int stateCursor = 0;
    private boolean released;

    public Box2IM() {}

    public Box2IM(int minX, int minY, int maxX, int maxY) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
    }

    public Box2IM(double minX, double minY, double maxX, double maxY) {
        this(BananaMath.floor(minX), BananaMath.floor(minY), BananaMath.floor(maxX), BananaMath.floor(maxY));
    }

    public Box2IM(IVec2D from, IVec2D to) {
        this(BananaMath.floor(from.x()), BananaMath.floor(from.y()), BananaMath.floor(to.x()), BananaMath.floor(to.y()));
    }

    public Box2IM(IVec2I from, IVec2I to) {
        this(from.x(), from.y(), to.x(), to.y());
    }

    public Box2IM(IBox2D box) {
        this(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    public Box2IM(IBox2I box) {
        this(box.minX(), box.minY(), box.maxX(), box.maxY());
    }

    @Override
    public IBox2IM set(int[] box) {
        this.minX = Math.min(box[0], box[2]);
        this.minY = Math.min(box[1], box[3]);
        this.maxX = Math.max(box[0], box[2]);
        this.maxY = Math.max(box[1], box[3]);
        return this;
    }

    @Override
    public IBox2IM set(int minX, int minY, int maxX, int maxY) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        return this;
    }

    @Override
    public IBox2IM set(double minX, double minY, double maxX, double maxY) {
        return this.set(BananaMath.floor(minX), BananaMath.floor(minY), BananaMath.floor(maxX), BananaMath.floor(maxY));
    }

    @Override
    public IBox2IM set(IVec2D boxFrom, IVec2D boxTo) {
        return this.set(boxFrom.x(), boxFrom.y(), boxTo.x(), boxTo.y());
    }

    @Override
    public IBox2IM set(IVec2I boxFrom, IVec2I boxTo) {
        return this.set(boxFrom.x(), boxFrom.y(), boxTo.x(), boxTo.y());
    }

    @Override
    public IBox2IM set(IBox2D area2D) {
        return this.set(area2D.minX(), area2D.minY(), area2D.maxX(), area2D.maxY());
    }

    @Override
    public IBox2IM set(IBox2I area2D) {
        return this.set(area2D.minX(), area2D.minY(), area2D.maxX(), area2D.maxY());
    }

    @Override
    public IBox2IM setZero() {
        this.minX = 0;
        this.minY = 0;
        this.maxX = 0;
        this.maxY = 0;
        return this;
    }

    @Override
    public IBox2IM collapseStack() {
        this.stateStack = null;
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IBox2IM resetStack() {
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IBox2IM pushBox() {
        if (this.stateStack == null) {
            this.stateStack = new int[1][];
        }
        if (this.stateCursor >= this.stateStack.length) {
            this.stateStack = Arrays.copyOf(this.stateStack, this.stateCursor + 1);
        }
        int[] arr = this.stateStack[this.stateCursor++];

        if (arr == null) {
            arr = new int[4];
        }

        arr[0] = this.minX;
        arr[1] = this.minY;
        arr[2] = this.maxX;
        arr[3] = this.maxY;

        this.stateStack[this.stateCursor - 1] = arr;

        return this;
    }

    @Override
    public IBox2IM popBox() {
        if (this.stateCursor - 1 < 0) {
            throw new IllegalStateException();
        }
        return this.set(this.stateStack[--this.stateCursor]);
    }

    @Override
    public int minX() {
        return this.minX;
    }

    @Override
    public int minY() {
        return this.minY;
    }

    @Override
    public int maxX() {
        return this.maxX;
    }

    @Override
    public int maxY() {
        return this.maxY;
    }

    @Override
    public int size() {
        return (this.maxX - this.minX) * (this.maxY - this.minY);
    }

    @Override
    public IBox2IM grow(double amount) {
        return this.grow(amount, amount);
    }

    @Override
    public IBox2IM grow(double x, double y) {
        return this.set(this.minX() - x, this.minY() - y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2IM grow(int amount) {
        return this.grow(amount, amount);
    }

    @Override
    public IBox2IM grow(int x, int y) {
        return this.set(this.minX() - x, this.minY() - y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2IM grow(IVec2I vec2D) {
        return this.grow(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2IM grow(IVec2D vec2D) {
        return this.grow(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2IM offset(int x, int y) {
        return this.set(this.minX() + x, this.minY() + y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2IM offset(double x, double y) {
        return this.set(this.minX() + x, this.minY() + y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2IM offset(IVec2D vec2D) {
        return this.offset(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2IM offset(IVec2I vec2D) {
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
    public IBox2IM toMutable() {
        return this;
    }

    @Override
    public IBox2I toImmutable() {
        return new Box2I(this);
    }

    @Override
    public IBox2DM toDouble() {
        return new Box2DM(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IBox2I && ((IBox2I) obj).minX() == this.minX() && ((IBox2I) obj).minY() == this.minY() && ((IBox2I) obj).maxX() == this.maxX() && ((IBox2I) obj).maxY() == this.maxY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minX(), this.minY(), this.maxX(), this.maxY());
    }

    @Override
    public String toString() {
        return "[" + this.minX() + ", " + minY() + "] -> ["  + this.maxX() + ", " + this.maxY() + "]";
    }
}
