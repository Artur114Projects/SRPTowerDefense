package com.artur114.bananalib.math.m2d.vec;

import java.util.Objects;

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
    public double distance(double x, double y) {
        double deltaX = x - this.x();
        double deltaY = y - this.y();
        return Math.sqrt(deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public double distance(IVec2D vec) {
        return this.distance(vec.x(), vec.y());
    }

    @Override
    public double distanceSq(double x, double y) {
        double deltaX = x - this.x();
        double deltaY = y - this.y();
        return deltaY * deltaY + deltaX * deltaX;
    }

    @Override
    public double distanceSq(IVec2D vec) {
        return this.distanceSq(vec.x(), vec.y());
    }

    @Override
    public IVec2D add(double x, double y) {
        return new Vec2D(this.x() + x, this.y() + y);
    }

    @Override
    public IVec2D add(IVec2D vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2D subtract(double x, double y) {
        return this.add(-x, -y);
    }

    @Override
    public IVec2D subtract(IVec2D vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2D scale(double x, double y) {
        return new Vec2D(this.x() * x, this.y() * y);
    }

    @Override
    public IVec2D scale(double val) {
        return this.scale(val, val);
    }

    @Override
    public IVec2D scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2D toImmutable() {
        return this;
    }

    @Override
    public IVec2D normalize() {
        double l = Math.sqrt(this.x() * this.x() + this.y() * this.y());
        return l < 1.0E-4D ? ZERO : new Vec2D(this.x() / l, this.y() / l);
    }

    @Override
    public IVec2I toI() {
        return new Vec2I(this);
    }

    @Override
    public double lengthSq() {
        return this.x() * this.x() + this.y() * this.y();
    }

    @Override
    public double length() {
        return Math.sqrt(this.lengthSq());
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
    public boolean equals(Object obj) {
        return obj instanceof IVec2D && ((IVec2D) obj).x() == this.x() && ((IVec2D) obj).y() == this.y();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x(), this.y());
    }

    @Override
    public String toString() {
        return "(" + this.x() + ", " + this.y() + ")";
    }
}
