package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IBox2IM extends IBox2I {
    IBox2IM set(int[] box);
    IBox2IM set(int minX, int minY, int maxX, int maxY);
    IBox2IM set(double minX, double minY, double maxX, double maxY);
    IBox2IM set(IVec2D boxFrom, IVec2D boxTo);
    IBox2IM set(IVec2I boxFrom, IVec2I boxTo);
    IBox2IM set(IBox2D area2D);
    IBox2IM set(IBox2I area2D);
    IBox2IM setZero();
    IBox2IM collapseStack();
    IBox2IM resetStack();
    IBox2IM pushBox();
    IBox2IM popBox();
    IBox2IM grow(double amount);
    IBox2IM grow(double x, double y);
    IBox2IM grow(int amount);
    IBox2IM grow(int x, int y);
    IBox2IM grow(IVec2I vec2D);
    IBox2IM grow(IVec2D vec2D);
    IBox2IM offset(int x, int y);
    IBox2IM offset(double x, double y);
    IBox2IM offset(IVec2D vec2D);
    IBox2IM offset(IVec2I vec2D);
    IBox2DM toDouble();
}
