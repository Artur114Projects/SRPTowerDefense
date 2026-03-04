package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

import java.util.Objects;

public class Box2IM implements IBox2IM {
    private int minX, minY, maxX, maxY;

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

    @Override
    public IBox2IM set(int minX, int minY, int maxX, int maxY) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        return this;
    }

    @Override
    public IBox2IM set(IVec2I from, IVec2I to) {
        return this.set(from.x(), from.y(), to.x(), to.y());
    }

    @Override
    public IBox2IM set(IBox2I area2D) {
        return this.set(area2D.minX(), area2D.minY(), area2D.maxX(), area2D.maxY());
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
    public IBox2IM offset(int x, int y) {
        return this.set(this.minX() + x, this.minY() + y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2IM offset(IVec2I vec2D) {
        return this.offset(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2I toImmutable() {
        return this;
    }

    @Override
    public IBox2DM toD() {
        return new Box2DM(this.minX(), this.minY(), this.maxX(), this.maxY());
    }

    @Override
    public boolean intersects(int minX, int minY, int maxX, int maxY) {
        return this.minX() < maxX && this.maxX() > minX && this.minY() < maxY && this.maxY() > minY;
    }

    @Override
    public boolean intersects(IVec2I from, IVec2I to) {
        return this.intersects(from.x(), from.y(), to.x(), to.y());
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
    public boolean contains(IBox2I area2D) {
        return this.contains(area2D.minX(), area2D.minY()) && this.contains(area2D.maxX(), area2D.maxY());
    }

    @Override
    public boolean contains(IVec2I vec2D) {
        return this.contains(vec2D.x(), vec2D.y());
    }

    @Override
    public double size() {
        return (this.maxX - this.minX) * (this.maxY - this.minY);
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
