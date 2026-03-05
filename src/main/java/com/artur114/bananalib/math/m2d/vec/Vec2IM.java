package com.artur114.bananalib.math.m2d.vec;

import com.artur114.bananalib.math.BananaMath;

import java.util.Objects;

public class Vec2IM implements IVec2IM {
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
    public float distance(int x, int y) {
        int deltaX = x - this.x();
        int deltaY = y - this.y();
        return (float) Math.sqrt(deltaY * deltaY + deltaX * deltaX);
    }

    @Override
    public float distance(IVec2I vec) {
        return this.distance(vec.x(), vec.y());
    }

    @Override
    public float distanceSq(int x, int y) {
        int deltaX = x - this.x();
        int deltaY = y - this.y();
        return deltaY * deltaY + deltaX * deltaX;
    }

    @Override
    public float distanceSq(IVec2I vec) {
        return this.distanceSq(vec.x(), vec.y());
    }

    @Override
    public IVec2IM set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public IVec2IM set(IVec2I vec) {
        return this.set(vec.x(), vec.y());
    }

    @Override
    public IVec2IM set(IVec2D vec) {
        return this.set(BananaMath.floor(vec.x()), BananaMath.floor(vec.y()));
    }

    @Override
    public IVec2IM add(int x, int y) {
        return this.set(this.x() + x, this.y() + y);
    }

    @Override
    public IVec2IM add(IVec2I vec) {
        return this.add(vec.x(), vec.y());
    }

    @Override
    public IVec2IM subtract(int x, int y) {
        return this.add(-x, -y);
    }

    @Override
    public IVec2IM subtract(IVec2I vec) {
        return this.subtract(vec.x(), vec.y());
    }

    @Override
    public IVec2IM scale(int x, int y)  {
        return this.set(this.x() * x, this.y() * y);
    }

    @Override
    public IVec2I scale(int val) {
        return this.scale(val, val);
    }

    @Override
    public IVec2IM scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IVec2I toImmutable() {
        return this;
    }

    @Override
    public IVec2DM normalize() {
        double l = Math.sqrt(this.x() * this.x() + this.y() * this.y());
        return l < 1.0E-4D ? new Vec2DM(0, 0) : new Vec2DM(this.x() / l, this.y() / l);
    }

    @Override
    public IVec2DM toD() {
        return new Vec2DM(this);
    }

    @Override
    public float lengthSq() {
        return this.x() * this.x() + this.y() * this.y();
    }

    @Override
    public float length() {
        return (float) Math.sqrt(this.lengthSq());
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
    public boolean equals(Object obj) {
        return obj instanceof IVec2I && ((IVec2I) obj).x() == this.x() && ((IVec2I) obj).y() == this.y();
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
