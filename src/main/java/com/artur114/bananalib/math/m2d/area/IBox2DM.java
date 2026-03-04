package com.artur114.bananalib.math.m2d.area;

import com.artur114.bananalib.math.m2d.vec.IVec2D;

public interface IBox2DM extends IBox2D {
    IBox2DM set(double fx, double fy, double tx, double ty);
    IBox2DM set(IVec2D from, IVec2D to);
    IBox2DM set(IBox2D area2D);
    IBox2DM grow(double amount);
    IBox2DM grow(double x, double y);
    IBox2DM grow(IVec2D vec2D);
    IBox2DM offset(double x, double y);
    IBox2DM offset(IVec2D vec2D);
    IBox2IM toI();
}
