package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2D;

public interface IBox2D {
    IBox2D grow(double x, double y);
    IBox2D grow(IVec2D vec2D);
    IBox2D offset(double x, double y);
    IBox2D offset(IVec2D vec2D);
    IBox2D toImmutable();
    IBox2I toI();
    boolean intersects(double fx, double fy, double tx, double ty);
    boolean intersects(IVec2D from, IVec2D to);
    boolean intersects(IBox2D area2D);
    boolean contains(double x, double y);
    boolean contains(IBox2D area2D);
    boolean contains(IVec2D vec2D);
    double size();
    double minX();
    double minY();
    double maxX();
    double maxY();
}
