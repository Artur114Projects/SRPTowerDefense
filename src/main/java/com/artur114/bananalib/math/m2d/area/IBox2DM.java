package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IBox2DM extends IBox2D {
    IBox2DM set(double[] box);
    IBox2DM set(int minX, int minY, int maxX, int maxY);
    IBox2DM set(double minX, double minY, double maxX, double maxY);
    IBox2DM set(IVec2D boxFrom, IVec2D boxTo);
    IBox2DM set(IVec2I boxFrom, IVec2I boxTo);
    IBox2DM set(IBox2D area2D);
    IBox2DM set(IBox2I area2D);
    IBox2DM setZero();
    IBox2DM collapseStack();
    IBox2DM resetStack();
    IBox2DM pushBox();
    IBox2DM popBox();
    IBox2DM grow(double amount);
    IBox2DM grow(double x, double y);
    IBox2DM grow(int amount);
    IBox2DM grow(int x, int y);
    IBox2DM grow(IVec2I vec2D);
    IBox2DM grow(IVec2D vec2D);
    IBox2DM offset(int x, int y);
    IBox2DM offset(double x, double y);
    IBox2DM offset(IVec2D vec2D);
    IBox2DM offset(IVec2I vec2D);
    IBox2IM floor();
    IBox2IM round();
    IBox2IM ceil();
}
