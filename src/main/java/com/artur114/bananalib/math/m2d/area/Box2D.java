package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

import java.util.Objects;

public class Box2D implements IBox2D {
    private final double minX, minY, maxX, maxY;

    public Box2D(double minX, double minY, double maxX, double maxY) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
    }

    public Box2D(int minX, int minY, int maxX, int maxY) {
        this(minX, minY, maxX, (double) maxY);
    }

    public Box2D(IVec2D from, IVec2D to) {
        this(from.x(), from.y(), to.x(), to.y());
    }

    public Box2D(IVec2I from, IVec2I to) {
        this(from.x(), from.y(), to.x(), to.y());
    }

    @Override
    public IBox2D grow(double x, double y) {
        return new Box2D(this.minX() - x, this.minY() - y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2D grow(IVec2D vec2D) {
        return this.grow(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2D offset(double x, double y) {
        return new Box2D(this.minX() + x, this.minY() + y, this.maxX() + x, this.maxY() + y);
    }

    @Override
    public IBox2D offset(IVec2D vec2D) {
        return this.offset(vec2D.x(), vec2D.y());
    }

    @Override
    public IBox2D toImmutable() {
        return this;
    }

    @Override
    public IBox2I toI() {
        return new Box2I(this.minX(), this.minY(), this.maxX(), this.maxY());
    }

    @Override
    public boolean intersects(double minX, double minY, double maxX, double maxY) {
        return this.minX() < maxX && this.maxX() > minX && this.minY() < maxY && this.maxY() > minY;
    }

    @Override
    public boolean intersects(IVec2D from, IVec2D to) {
        return this.intersects(from.x(), from.y(), to.x(), to.y());
    }

    @Override
    public boolean intersects(IBox2D area2D) {
        return this.intersects(area2D.minX(), area2D.minY(), area2D.maxX(), area2D.maxY());
    }

    @Override
    public boolean contains(double x, double y) {
        return x > this.minX() && y > this.minY() && x < this.maxX() && y < this.maxY();
    }

    @Override
    public boolean contains(IBox2D area2D) {
        return this.contains(area2D.minX(), area2D.minY()) && this.contains(area2D.maxX(), area2D.maxY());
    }

    @Override
    public boolean contains(IVec2D vec2D) {
        return this.contains(vec2D.x(), vec2D.y());
    }

    @Override
    public double size() {
        return (this.maxX - this.minX) * (this.maxY - this.minY);
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
    public boolean equals(Object obj) {
        return obj instanceof IBox2D && ((IBox2D) obj).minX() == this.minX() && ((IBox2D) obj).minY() == this.minY() && ((IBox2D) obj).maxX() == this.maxX() && ((IBox2D) obj).maxY() == this.maxY();
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
