package com.artur114.bananalib.math.m2d.vec;

import com.artur114.bananalib.math.BananaMath;

import java.util.Objects;

public class Vec2I implements IVec2I {
    private final int x, y;

    public Vec2I(double x, double y) {
        this.x = BananaMath.floor(x);
        this.y = BananaMath.floor(y);
    }

    public Vec2I(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2I(IVec2D vec2D) {
        this(vec2D.x(), vec2D.y());
    }

    public Vec2I(IVec2I vec2D) {
        this(vec2D.x(), vec2D.y());
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
    public float length() {
        return (float) Math.sqrt(this.lengthSq());
    }

    @Override
    public float lengthSq() {
        return this.x() * this.x() + this.y() * this.y();
    }

    @Override
    public float distance(int x, int y) {
        long deltaX = x - this.x();
        long deltaY = y - this.y();
        return (float) Math.sqrt(deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public float distance(double x, double y) {
        double deltaX = x - this.x();
        double deltaY = y - this.y();
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
        long deltaX = x - this.x();
        long deltaY = y - this.y();
        return deltaY * deltaY + deltaX * deltaX;
    }

    @Override
    public float distanceSq(double x, double y) {
        double deltaX = x - this.x();
        double deltaY = y - this.y();
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
    public IVec2I add(int x, int y) {
        return new Vec2I(this.x() + x, this.y() + y);
    }

    @Override
    public IVec2I add(double x, double y) {
        return new Vec2I(this.x() + x, this.y() + y);
    }

    @Override
    public IVec2I add(IVec2I vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2I add(IVec2D vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2I subtract(int x, int y) {
        return new Vec2I(this.x() - x, this.y() - y);
    }

    @Override
    public IVec2I subtract(double x, double y) {
        return new Vec2I(this.x() - x, this.y() - y);
    }

    @Override
    public IVec2I subtract(IVec2I vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2I subtract(IVec2D vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2I scale(int x, int y)  {
        return new Vec2I(this.x() * x, this.y() * y);
    }

    @Override
    public IVec2I scale(double val) {
        return new Vec2I(this.x() * val, this.y() * val);
    }

    @Override
    public IVec2I scale(double x, double y) {
        return new Vec2I(this.x() * x, this.y() * y);
    }

    @Override
    public IVec2I scale(int val) {
        return new Vec2I(this.x() * val, this.y() * val);
    }

    @Override
    public IVec2I scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2I scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2D normalize() {
        double l = Math.sqrt(this.x() * this.x() + this.y() * this.y());
        return l < 1.0E-4D ? Vec2D.ZERO : new Vec2D(this.x() / l, this.y() / l);
    }

    @Override
    public IVec2IM toMutable() {
        return new Vec2IM(this);
    }

    @Override
    public IVec2I toImmutable() {
        return this;
    }

    @Override
    public IVec2D toDouble() {
        return new Vec2D(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IVec2I && ((IVec2I) obj).x() == this.x() && ((IVec2I) obj).y() == this.y();
    }

    @Override
    public int hashCode() {
        return 31 * this.x() + this.y();
    }

    @Override
    public String toString() {
        return "(" + this.x() + ", " + this.y() + ")";
    }
}
