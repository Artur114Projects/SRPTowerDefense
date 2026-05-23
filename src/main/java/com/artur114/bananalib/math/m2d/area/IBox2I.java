package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IBox2I {
    int minX();
    int minY();
    int maxX();
    int maxY();
    int size();
    IBox2I grow(double amount);
    IBox2I grow(double x, double y);
    IBox2I grow(int amount);
    IBox2I grow(int x, int y);
    IBox2I grow(IVec2I vec2D);
    IBox2I grow(IVec2D vec2D);
    IBox2I offset(int x, int y);
    IBox2I offset(double x, double y);
    IBox2I offset(IVec2D vec2D);
    IBox2I offset(IVec2I vec2D);
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
    IBox2IM toMutable();
    IBox2I toImmutable();
    IBox2D toDouble();
}
