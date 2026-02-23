package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IBox2IM extends IBox2I {
    IBox2IM set(int minX, int minY, int maxX, int maxY);
    IBox2IM set(IVec2I from, IVec2I to);
    IBox2IM set(IBox2I area2D);
    IBox2IM grow(int x, int y);
    IBox2IM grow(IVec2I vec2D);
    IBox2IM offset(int x, int y);
    IBox2IM offset(IVec2I vec2D);
    IBox2DM toD();
}
