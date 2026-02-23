package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IBox2I {
    IBox2I grow(int x, int y);
    IBox2I grow(IVec2I vec2D);
    IBox2I offset(int x, int y);
    IBox2I offset(IVec2I vec2D);
    IBox2I toImmutable();
    IBox2D toD();
    boolean intersects(int fx, int fy, int tx, int ty);
    boolean intersects(IVec2I from, IVec2I to);
    boolean intersects(IBox2I area2D);
    boolean contains(int x, int y);
    boolean contains(IBox2I area2D);
    boolean contains(IVec2I vec2D);
    double size();
    int minX();
    int minY();
    int maxX();
    int maxY();
}
