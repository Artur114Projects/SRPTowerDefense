package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IBox2D {
    double minX();
    double minY();
    double maxX();
    double maxY();
    double size();
    IBox2D grow(double amount);
    IBox2D grow(double x, double y);
    IBox2D grow(int amount);
    IBox2D grow(int x, int y);
    IBox2D grow(IVec2I vec2D);
    IBox2D grow(IVec2D vec2D);
    IBox2D offset(int x, int y);
    IBox2D offset(double x, double y);
    IBox2D offset(IVec2D vec2D);
    IBox2D offset(IVec2I vec2D);
    boolean intersects(int minX, int minY, int maxX, int maxY);
    boolean intersects(double minX, double minY, double maxX, double maxY);
    boolean intersects(IVec2D boxFrom, IVec2D boxTo);
    boolean intersects(IVec2I boxFrom, IVec2I boxTo);
    boolean intersects(IBox2D area2D);
    boolean intersects(IBox2I area2D);
    boolean contains(int x, int y);
    boolean contains(double x, double y);
    boolean contains(IBox2I area2D);
    boolean contains(IBox2D area2D);
    boolean contains(IVec2I vec2D);
    boolean contains(IVec2D vec2D);
    IBox2DM toMutable();
    IBox2D toImmutable();
    IBox2I floor();
    IBox2I round();
    IBox2I ceil();
}
